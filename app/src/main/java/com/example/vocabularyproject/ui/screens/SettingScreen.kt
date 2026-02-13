package com.example.vocabularyproject.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.util.DatabaseBackupManager
import com.example.vocabularyproject.util.buttonGradientClolor
import com.example.vocabularyproject.viewmodels.BackupViewModel

@Composable
fun SettingScreen(
    viewModel: BackupViewModel = hiltViewModel()
){
    val backupManager = viewModel.backupManager
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        uri?.let {
            backupManager.importDatabaseFromUri(it)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HomeScreenButtonStyles(
            "Export Database",
            onClick = { viewModel.export()  },
            modifier = Modifier.fillMaxWidth(),
            gradientColors = buttonGradientClolor
        )

        HomeScreenButtonStyles(
            "Import Database",
            onClick = {launcher.launch(arrayOf("*/*"))},
            modifier = Modifier.fillMaxWidth(),
            gradientColors = buttonGradientClolor
        )
    }

}