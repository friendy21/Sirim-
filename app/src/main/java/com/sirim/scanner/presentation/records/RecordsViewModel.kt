package com.sirim.scanner.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.usecase.DeleteRecordUseCase
import com.sirim.scanner.domain.usecase.GetRecordsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordsViewModel(
    getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ViewModel() {

    private val recordsFlow = getRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<RecordsUiState> = recordsFlow
        .map { RecordsUiState(records = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecordsUiState(records = emptyList())
        )

    fun delete(record: SirimRecord) {
        viewModelScope.launch {
            deleteRecord(record)
        }
    }
}

data class RecordsUiState(
    val records: List<SirimRecord>
)
