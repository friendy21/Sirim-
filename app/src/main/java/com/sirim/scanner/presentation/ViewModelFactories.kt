package com.sirim.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sirim.scanner.di.AppContainer
import com.sirim.scanner.presentation.export.ExportViewModel
import com.sirim.scanner.presentation.records.RecordsViewModel
import com.sirim.scanner.presentation.scan.ScanViewModel

class ScanViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel(container.saveRecord) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class RecordsViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordsViewModel(container.getRecords, container.deleteRecord) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class ExportViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExportViewModel(container.exportRecords) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
