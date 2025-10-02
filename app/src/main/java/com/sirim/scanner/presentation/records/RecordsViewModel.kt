package com.sirim.scanner.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.usecase.CreateOrUpdateRecordUseCase
import com.sirim.scanner.domain.usecase.DeleteRecordUseCase
import com.sirim.scanner.domain.usecase.ExportRecordsUseCase
import com.sirim.scanner.domain.usecase.ObserveRecordsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordsViewModel(
    observeRecords: ObserveRecordsUseCase,
    private val saveRecordUseCase: CreateOrUpdateRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val exportRecordsUseCase: ExportRecordsUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val recordsFlow = observeRecords()

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(recordsFlow, searchQuery) { records, query ->
                if (query.isBlank()) {
                    records
                } else {
                    records.filter { record ->
                        record.sirimSerialNo.contains(query, ignoreCase = true) ||
                            (record.brandTrademark?.contains(query, ignoreCase = true) == true) ||
                            (record.model?.contains(query, ignoreCase = true) == true)
                    }
                }
            }.collect { filtered ->
                _uiState.update { state ->
                    state.copy(records = filtered, isLoading = false)
                }
            }
        }
    }

    fun onSearchQueryChange(value: String) {
        searchQuery.value = value
        _uiState.update { state -> state.copy(searchQuery = value) }
    }

    fun saveRecord(record: SirimRecord) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            saveRecordUseCase(record)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Record saved") }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message ?: "Unable to save record") }
                }
        }
    }

    fun deleteRecord(recordId: Long) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            deleteRecordUseCase(recordId)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Record deleted") }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message ?: "Unable to delete record") }
                }
        }
    }

    fun exportRecordsExcel() {
        val records = _uiState.value.records
        viewModelScope.launch {
            exportRecordsUseCase.asExcel(records).onSuccess { bytes ->
                _uiState.update { it.copy(exportedFile = ExportedFile("records.xlsx", bytes)) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(error = throwable.message ?: "Unable to export Excel") }
            }
        }
    }

    fun exportRecordsPdf() {
        val records = _uiState.value.records
        viewModelScope.launch {
            exportRecordsUseCase.asPdf(records).onSuccess { bytes ->
                _uiState.update { it.copy(exportedFile = ExportedFile("records.pdf", bytes)) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(error = throwable.message ?: "Unable to export PDF") }
            }
        }
    }

    fun exportRecordsBundle() {
        val records = _uiState.value.records
        viewModelScope.launch {
            val excel = exportRecordsUseCase.asExcel(records)
            val pdf = exportRecordsUseCase.asPdf(records)
            if (excel.isSuccess && pdf.isSuccess) {
                val files = mapOf(
                    "records.xlsx" to excel.getOrThrow(),
                    "records.pdf" to pdf.getOrThrow()
                )
                exportRecordsUseCase.asZip(files).onSuccess { bytes ->
                    _uiState.update { it.copy(exportedFile = ExportedFile("records.zip", bytes)) }
                }.onFailure { throwable ->
                    _uiState.update { it.copy(error = throwable.message ?: "Unable to create archive") }
                }
            } else {
                _uiState.update { it.copy(error = "Unable to export files") }
            }
        }
    }

    fun clearTransientMessages() {
        _uiState.update { it.copy(error = null, successMessage = null, exportedFile = null) }
    }
}

data class RecordsUiState(
    val records: List<SirimRecord> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val exportedFile: ExportedFile? = null,
)

data class ExportedFile(
    val fileName: String,
    val bytes: ByteArray,
)
