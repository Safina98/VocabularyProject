package com.example.vocabularyproject.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddWordDialog(
    id: Long?,
    initialWord: String?,
    onDismiss: () -> Unit,
    onAddClick: (Long?, String) -> Unit
) {
    var dialogText by remember(initialWord) {
        mutableStateOf(initialWord.orEmpty())
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(min = 280.dp)
            ) {
                Text(
                    text = if (id == null) "Add word" else "Edit word",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = dialogText,
                    onValueChange = { dialogText = it },
                    label = { Text("Word") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (dialogText.isNotBlank()) {
                                onAddClick(id, dialogText)
                            }
                        }
                    ) {
                        Text(if (id == null) "Add" else "Save")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddWordDialogPreview() {
    AddWordDialog(
        null,null,
        onDismiss = {},
        onAddClick = {_,word -> }
    )
}

