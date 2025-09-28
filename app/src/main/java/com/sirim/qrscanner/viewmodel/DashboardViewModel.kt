package com.sirim.qrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.usecase.GetLastSynchronizationTimestampUseCase
import com.sirim.qrscanner.core.domain.usecase.LogoutUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.SynchronizeRecordsUseCase
import com.sirim.qrscanner.ui.state.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeRecordsUseCase: ObserveRecordsUseCase,
    private val synchronizeRecordsUseCase: SynchronizeRecordsUseCase,
    private val getLastSynchronizationTimestampUseCase: GetLastSynchronizationTimestampUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm")
        .withZone(ZoneId.systemDefault())

    private val _state = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            observeRecordsUseCase().collectLatest { records ->
                _state.value = _state.value.copy(
                    recentRecords = records.take(3),
                    totalRecords = records.size
                )
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            getLastSynchronizationTimestampUseCase().onSuccess { timestamp ->
                val formatted = timestamp?.let { formatter.format(Instant.ofEpochMilli(it)) }
                _state.value = _state.value.copy(lastSyncAt = formatted)
            }
        }
    }

    fun synchronize() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.value = _state.value.copy(isSyncing = true)
            synchronizeRecordsUseCase()
                .onSuccess {
                    val formatted = formatter.format(Instant.now())
                    _state.value = _state.value.copy(lastSyncAt = formatted, isSyncing = false)
                }
                .onFailure {
                    _state.value = _state.value.copy(isSyncing = false)
                }
        }
    }

    fun logout() {
        viewModelScope.launch(dispatcherProvider.io) { logoutUseCase() }
    }
}
