package com.example.vocabularyproject.util

import com.example.vocabularyproject.database.VocabularyDatabase
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.InputStream

@Singleton
class DatabaseBackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: VocabularyDatabase
) {

    private val DATABASE_NAME = "vocabulary.db"
    private val BACKUP_NAME = "vocabulary_backup.db"

    // ✅ EXPORT
    fun exportDatabase(): Boolean {
        return try {

            val dbFile = context.getDatabasePath(DATABASE_NAME)
            if (!dbFile.exists()) return false

            val backupDir = context.getExternalFilesDir("backup")
            if (backupDir?.exists() == false) backupDir.mkdirs()

            val backupFile = File(backupDir, BACKUP_NAME)

            FileInputStream(dbFile).use { input ->
                FileOutputStream(backupFile).use { output ->
                    input.copyTo(output)
                }
            }
            Toast.makeText(context, "Database exported successfully", Toast.LENGTH_SHORT).show()

            true

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExportManager", "Error exporting database: ${e.message}")
            Toast.makeText(context, "Export Failed", Toast.LENGTH_SHORT).show()
            false

        }
    }
    fun exportAndShareDatabase() {

        try {
            val success = exportDatabase()
            if (!success) {
                Toast.makeText(context, "Export Failed", Toast.LENGTH_SHORT).show()
                return
            }

            val backupDir = context.getExternalFilesDir("backup")
            val backupFile = File(backupDir, BACKUP_NAME)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                backupFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share Database")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(chooser)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Share Failed", Toast.LENGTH_SHORT).show()
        }
    }


    // ✅ IMPORT (WITH db.close())
    fun importDatabase(): Boolean {
        return try {

            val backupDir = context.getExternalFilesDir("backup")
            val backupFile = File(backupDir, BACKUP_NAME)

            if (!backupFile.exists()) return false

            // 🔥 VERY IMPORTANT
            database.close()

            val dbFile = context.getDatabasePath(DATABASE_NAME)

            // Delete old database
            context.deleteDatabase(DATABASE_NAME)

            FileInputStream(backupFile).use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    fun importDatabaseFromUri(uri: Uri): Boolean {
        return try {

            // 🔥 Close Room database before replacing
            database.close()

            val dbFile = context.getDatabasePath(DATABASE_NAME)

            // Delete old database
            context.deleteDatabase(DATABASE_NAME)

            // Open selected file
            val inputStream: InputStream? =
                context.contentResolver.openInputStream(uri)

            if (inputStream == null) return false

            FileOutputStream(dbFile).use { output ->
                inputStream.copyTo(output)
            }

            inputStream.close()

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
