package daildubath.benjch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsDialog(appState: AppState) {
    var pendingSettings by remember { mutableStateOf(appState.settings.copy()) }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = { appState.showSettingsDialog = false },
        title = { Text("Settings", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(8.dp)) {

                // --- DEFAULTS SECTION ---
                Text("Defaults", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp, top = 8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                Text("Default Save Extension:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = pendingSettings.defaultSaveExtension == "txt", onClick = { pendingSettings = pendingSettings.copy(defaultSaveExtension = "txt") })
                    Text("Standard Text (.txt)")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = pendingSettings.defaultSaveExtension == "md", onClick = { pendingSettings = pendingSettings.copy(defaultSaveExtension = "md") })
                    Text("Markdown (.md)")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Default View Mode:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = pendingSettings.defaultViewMode == ViewMode.TXT, onClick = { pendingSettings = pendingSettings.copy(defaultViewMode = ViewMode.TXT) })
                    Text("TXT")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = pendingSettings.defaultViewMode == ViewMode.MD, onClick = { pendingSettings = pendingSettings.copy(defaultViewMode = ViewMode.MD) })
                    Text("MD")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = pendingSettings.defaultViewMode == ViewMode.SPLIT, onClick = { pendingSettings = pendingSettings.copy(defaultViewMode = ViewMode.SPLIT) })
                    Text("Split (T|M)")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = pendingSettings.shuffleFlashcardsDefault, onCheckedChange = { pendingSettings = pendingSettings.copy(shuffleFlashcardsDefault = it) })
                    Text("Shuffle flashcards by default")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Base Font Size: ${pendingSettings.baseFontSize}px", modifier = Modifier.width(150.dp))
                    Slider(
                        value = pendingSettings.baseFontSize.toFloat(),
                        onValueChange = { pendingSettings = pendingSettings.copy(baseFontSize = it.toInt()) },
                        valueRange = 12f..32f,
                        steps = 20,
                        modifier = Modifier.weight(1f)
                    )
                }

                // --- COLORS SECTION ---
                Spacer(modifier = Modifier.height(16.dp))
                Text("Colors", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                val linkText = buildAnnotatedString {
                    append("Need inspiration? ")
                    pushLink(LinkAnnotation.Url(
                        url = "https://drive.google.com/file/d/1mELxeDCo_n4AxoQgWMtIA1MkGjSyN-m9/view?usp=sharing",
                        styles = TextLinkStyles(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
                    ))
                    append("View organized hex colors here.")
                    pop()
                }

                Text(text = linkText, modifier = Modifier.padding(bottom = 16.dp))

                ColorInputRow("Text Color", pendingSettings.textColor) { pendingSettings = pendingSettings.copy(textColor = it) }
                ColorInputRow("Note Background", pendingSettings.noteBackgroundColor) { pendingSettings = pendingSettings.copy(noteBackgroundColor = it) }
                ColorInputRow("Primary Bar", pendingSettings.primaryBarColor) { pendingSettings = pendingSettings.copy(primaryBarColor = it) }
                ColorInputRow("Secondary Bar", pendingSettings.secondaryBarColor) { pendingSettings = pendingSettings.copy(secondaryBarColor = it) }
                ColorInputRow("Selection/Highlight", pendingSettings.selectionColor) { pendingSettings = pendingSettings.copy(selectionColor = it) }
                ColorInputRow("Accent (Buttons)", pendingSettings.accentColor) { pendingSettings = pendingSettings.copy(accentColor = it) }
                ColorInputRow("Flashcard Front", pendingSettings.flashcardFrontColor) { pendingSettings = pendingSettings.copy(flashcardFrontColor = it) }
                ColorInputRow("Flashcard Back", pendingSettings.flashcardBackColor) { pendingSettings = pendingSettings.copy(flashcardBackColor = it) }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { appState.showResetSettingsDialog = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset to Default Settings")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                appState.updateSettings(pendingSettings)
                appState.showSettingsDialog = false
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = { appState.showSettingsDialog = false }) { Text("Cancel") }
        }
    )
}

@Composable
fun ColorInputRow(label: String, hexValue: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, modifier = Modifier.weight(1f))

        val isValid = hexValue.isValidHexColor()

        OutlinedTextField(
            value = hexValue,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.width(120.dp).padding(end = 8.dp),
            textStyle = LocalTextStyle.current.copy(color = if (isValid) LocalContentColor.current else Color.Red)
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isValid) hexValue.toComposeColor() else Color.Transparent, RoundedCornerShape(4.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        )
    }
}