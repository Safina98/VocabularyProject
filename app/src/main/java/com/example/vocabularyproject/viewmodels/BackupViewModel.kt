package com.example.vocabularyproject.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vocabularyproject.util.DatabaseBackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    val backupManager: DatabaseBackupManager
) : ViewModel() {

    fun export() = backupManager.exportAndShareDatabase()

    //fun import() = backupManager.importDatabaseFromUri()
}
