package com.sirim.qrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.usecase.DeleteRecordUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.RefreshRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.SearchRecordsUseCase
import com.sirim.qrscanner.ui.state.RecordListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val observeRecordsUseCase: ObserveRecordsUseCase,
    private val refreshRecordsUseCase: RefreshRecordsUseCase,
    private val searchRecordsUseCase: SearchRecordsUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(RecordListUiState())
    val uiState: StateFlow<RecordListUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            observeRecordsUseCase().collectLatest { records ->
                _state.value = _state.value.copy(records = records, isLoading = false)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.value = _state.value.copy(isLoading = true)
            runCatching { refreshRecordsUseCase() }
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to refresh"
                    )
                }
        }
    }

    fun updateQuery(query: String) {
        _state.value = _state.value.copy(query = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcherProvider.io) {
            runCatching { searchRecordsUseCase(query) }
                .onSuccess { results ->
                    _state.value = _state.value.copy(records = results, errorMessage = null)
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(errorMessage = throwable.message)
                }
        }
    }

    fun deleteRecord(record: SirimRecord) {
        viewModelScope.launch(dispatcherProvider.io) {
            runCatching { deleteRecordUseCase(record) }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(errorMessage = throwable.message)
                }
        }
    }
}
