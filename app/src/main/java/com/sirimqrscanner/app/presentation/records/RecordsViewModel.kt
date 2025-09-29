package com.sirimqrscanner.app.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.usecase.DeleteRecordUseCase
import com.sirimqrscanner.app.domain.usecase.ObserveRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordsUiState(
    val isLoading: Boolean = true,
    val records: List<SirimRecord> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class RecordsViewModel @Inject constructor(
    observeRecordsUseCase: ObserveRecordsUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecordsUiState())
    val state: StateFlow<RecordsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeRecordsUseCase().collect { records ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        records = records,
                        error = null
                    )
                }
            }
        }
    }

    fun delete(record: SirimRecord) {
        viewModelScope.launch {
            deleteRecordUseCase(record)
        }
    }
}
