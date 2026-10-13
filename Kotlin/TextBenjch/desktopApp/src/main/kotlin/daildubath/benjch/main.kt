package daildubath.benjch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun App(onExit: () -> Unit) {
    val appState = remember { AppState() }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().background(appState.settings.noteBackgroundColor.toComposeColor())) {

            // Top Bar
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("TEXT BENJCH", fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = appState.settings.textColor.toComposeColor())
                    }
                },
                backgroundColor = appState.settings.primaryBarColor.toComposeColor(),
                contentColor = appState.settings.textColor.toComposeColor(),
                elevation = 4.dp,
                actions = {
                    Row(modifier = Modifier.padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {

                        if (!appState.isFlashcardMode) {
                            IconButton(onClick = {
                                if (appState.currentNoteContent.isNotEmpty()) {
                                    val selection = StringSelection(appState.currentNoteContent)
                                    Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, selection)
                                }
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Raw Markdown", tint = appState.settings.textColor.toComposeColor())
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            val btnBgActive = appState.settings.selectionColor.toComposeColor()
                            val btnText = appState.settings.textColor.toComposeColor()

                            Button(onClick = { appState.viewMode = ViewMode.TXT }, colors = ButtonDefaults.buttonColors(backgroundColor = if (appState.viewMode == ViewMode.TXT) btnBgActive else Color.Transparent, contentColor = btnText), elevation = ButtonDefaults.elevation(0.dp)) { Text("TXT") }
                            Button(onClick = { appState.viewMode = ViewMode.MD }, colors = ButtonDefaults.buttonColors(backgroundColor = if (appState.viewMode == ViewMode.MD) btnBgActive else Color.Transparent, contentColor = btnText), elevation = ButtonDefaults.elevation(0.dp)) { Text("MD") }
                            Button(onClick = { appState.viewMode = ViewMode.SPLIT }, colors = ButtonDefaults.buttonColors(backgroundColor = if (appState.viewMode == ViewMode.SPLIT) btnBgActive else Color.Transparent, contentColor = btnText), elevation = ButtonDefaults.elevation(0.dp)) { Text("T|M") }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                appState.isFlashcardMode = !appState.isFlashcardMode
                                if (appState.isFlashcardMode) appState.selectedNoteId = null else appState.selectedDeckId = null
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = appState.settings.accentColor.toComposeColor(), contentColor = Color.White)
                        ) {
                            Text(if (appState.isFlashcardMode) "Notes" else "Flash Cards")
                        }
                    }
                }
            )

            Row(modifier = Modifier.fillMaxSize()) {
                // LEFT BAR
                Column(modifier = Modifier.weight(1f).fillMaxHeight().background(appState.settings.secondaryBarColor.toComposeColor())) {
                    Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Note Books", fontWeight = FontWeight.Bold, fontSize = (appState.settings.baseFontSize + 2).sp, color = appState.settings.textColor.toComposeColor())
                            IconButton(onClick = { appState.addNotebook() }) { Icon(Icons.Default.Add, contentDescription = "Add Notebook", tint = appState.settings.textColor.toComposeColor()) }
                        }
                        Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn {
                            items(appState.notebooks, key = { it.id }) { nb ->
                                NotebookItem(notebook = nb, appState = appState)
                            }
                        }
                    }

                    Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth().background(appState.settings.selectionColor.toComposeColor()).padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { appState.showSettingsDialog = true }) { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = appState.settings.textColor.toComposeColor()) }
                        Row {
                            IconButton(onClick = { appState.importFile() }) { Icon(Icons.Default.Folder, contentDescription = "Import File", tint = appState.settings.textColor.toComposeColor()) }
                            IconButton(onClick = { appState.requestDelete() }) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f)) }
                        }
                    }
                }

                // RIGHT BAR: Dynamic View
                Column(modifier = Modifier.weight(3f).fillMaxHeight().padding(16.dp)) {
                    if (appState.isFlashcardMode) {
                        if (appState.selectedDeckId != null) {

                            TextField(
                                value = appState.getSelectedDeckName(),
                                onValueChange = { appState.updateSelectedDeckName(it) },
                                textStyle = LocalTextStyle.current.copy(fontSize = (appState.settings.baseFontSize + 8).sp, fontWeight = FontWeight.Bold, color = appState.settings.textColor.toComposeColor()),
                                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, textColor = appState.settings.textColor.toComposeColor()),
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Deck Name", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.5f)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val cards = appState.getActiveDeckCards()

                            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Row {
                                        IconButton(
                                            onClick = { appState.isEditingCard = !appState.isEditingCard },
                                            enabled = cards.isNotEmpty()
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Edit Card",
                                                tint = if (appState.isEditingCard) appState.settings.accentColor.toComposeColor() else appState.settings.textColor.toComposeColor().copy(alpha = if (cards.isNotEmpty()) 1f else 0.5f)
                                            )
                                        }
                                        IconButton(
                                            onClick = { appState.deleteFlashcard() },
                                            enabled = cards.isNotEmpty()
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete Card",
                                                tint = if (cards.isNotEmpty()) Color.Red.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.3f)
                                            )
                                        }
                                    }
                                    Button(onClick = { appState.addFlashcard() }, colors = ButtonDefaults.buttonColors(backgroundColor = appState.settings.accentColor.toComposeColor(), contentColor = Color.White)) {
                                        Text("+ New Card")
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                if (cards.isNotEmpty()) {
                                    val currentCard = cards[appState.currentCardIndex]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .aspectRatio(1.6f)
                                            .clickable(enabled = !appState.isEditingCard) { appState.isCardFlipped = !appState.isCardFlipped },
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        backgroundColor = if (appState.isCardFlipped) appState.settings.flashcardBackColor.toComposeColor() else appState.settings.flashcardFrontColor.toComposeColor()
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(24.dp)) {
                                            if (appState.isEditingCard) {
                                                TextField(
                                                    value = if (appState.isCardFlipped) currentCard.back else currentCard.front,
                                                    onValueChange = { newValue ->
                                                        if (newValue.length <= 200) {
                                                            if (appState.isCardFlipped) {
                                                                appState.updateFlashcard(currentCard.front, newValue)
                                                            } else {
                                                                appState.updateFlashcard(newValue, currentCard.back)
                                                            }
                                                        }
                                                    },
                                                    textStyle = LocalTextStyle.current.copy(fontSize = (appState.settings.baseFontSize + 12).sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                                                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, textColor = Color.Black),
                                                    modifier = Modifier.fillMaxSize(),
                                                    placeholder = { Text(if (appState.isCardFlipped) "Back of Card" else "Front of Card", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
                                                )
                                            } else {
                                                Text(
                                                    text = if (appState.isCardFlipped) currentCard.back else currentCard.front,
                                                    fontSize = (appState.settings.baseFontSize + 12).sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    color = Color.Black // Typically dark text for flashcards
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (appState.isEditingCard) {
                                            Button(
                                                onClick = { appState.isCardFlipped = !appState.isCardFlipped },
                                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                                            ) {
                                                Text("Flip Card to Edit ${if (appState.isCardFlipped) "Front" else "Back"}")
                                            }
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                        Text("Card ${appState.currentCardIndex + 1} of ${cards.size}", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.7f))
                                    }

                                } else {
                                    Text("This deck has no flashcards. Click '+ New Card' to start!", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.7f))
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (cards.isNotEmpty() && appState.currentCardIndex > 0) {
                                                appState.currentCardIndex--
                                                appState.isCardFlipped = false
                                            }
                                        },
                                        enabled = cards.isNotEmpty() && appState.currentCardIndex > 0
                                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Card", modifier = Modifier.size(40.dp), tint = appState.settings.textColor.toComposeColor()) }

                                    Spacer(modifier = Modifier.width(32.dp))

                                    IconButton(onClick = { appState.shuffleDeck() }, enabled = cards.isNotEmpty()) {
                                        Icon(Icons.Default.Shuffle, contentDescription = "Shuffle Deck", modifier = Modifier.size(32.dp), tint = appState.settings.textColor.toComposeColor())
                                    }

                                    Spacer(modifier = Modifier.width(32.dp))

                                    IconButton(
                                        onClick = {
                                            if (cards.isNotEmpty() && appState.currentCardIndex < cards.size - 1) {
                                                appState.currentCardIndex++
                                                appState.isCardFlipped = false
                                            }
                                        },
                                        enabled = cards.isNotEmpty() && appState.currentCardIndex < cards.size - 1
                                    ) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Card", modifier = Modifier.size(40.dp), tint = appState.settings.textColor.toComposeColor()) }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Select a deck on the left or create a new one.", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.6f))
                            }
                        }
                    } else {
                        if (appState.selectedNoteId != null) {
                            TextField(
                                value = appState.getSelectedNoteTitle(),
                                onValueChange = { appState.updateSelectedNoteTitle(it) },
                                textStyle = LocalTextStyle.current.copy(fontSize = (appState.settings.baseFontSize + 8).sp, fontWeight = FontWeight.Bold, color = appState.settings.textColor.toComposeColor()),
                                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, textColor = appState.settings.textColor.toComposeColor()),
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Note Title", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.5f)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val editorTextStyle = LocalTextStyle.current.copy(fontSize = appState.settings.baseFontSize.sp, color = appState.settings.textColor.toComposeColor())

                            when (appState.viewMode) {
                                ViewMode.TXT -> {
                                    TextField(
                                        value = appState.currentNoteContent,
                                        onValueChange = { appState.updateSelectedNoteContent(it) },
                                        modifier = Modifier.fillMaxSize(),
                                        textStyle = editorTextStyle,
                                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, textColor = appState.settings.textColor.toComposeColor()),
                                        placeholder = { Text("Start typing...", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.5f)) }
                                    )
                                }
                                ViewMode.MD -> {
                                    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                        MarkdownViewer(text = appState.currentNoteContent, processor = appState.markdownProcessor)
                                    }
                                }
                                ViewMode.SPLIT -> {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        TextField(
                                            value = appState.currentNoteContent,
                                            onValueChange = { appState.updateSelectedNoteContent(it) },
                                            modifier = Modifier.weight(1f).fillMaxHeight(),
                                            textStyle = editorTextStyle,
                                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, textColor = appState.settings.textColor.toComposeColor()),
                                            placeholder = { Text("Start typing...", color = appState.settings.textColor.toComposeColor().copy(alpha = 0.5f)) }
                                        )
                                        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.LightGray)
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
                                            MarkdownViewer(text = appState.currentNoteContent, processor = appState.markdownProcessor)
                                        }
                                    }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (appState.selectedNotebookId != null) "Select a note or add a new one." else "Select a notebook to begin.",
                                    color = appState.settings.textColor.toComposeColor().copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Dialogs ---
        if (appState.showSettingsDialog) {
            SettingsDialog(appState)
        }

        if (appState.showResetSettingsDialog) {
            AlertDialog(
                onDismissRequest = { appState.showResetSettingsDialog = false },
                title = { Text("Reset Settings", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to reset all settings back to their default values? This cannot be undone.") },
                confirmButton = {
                    Button(onClick = { appState.resetSettingsToDefault() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)) {
                        Text("Reset")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { appState.showResetSettingsDialog = false }) { Text("Cancel") }
                }
            )
        }

        if (appState.showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { appState.showDeleteConfirmDialog = false },
                title = { Text("Confirm Deletion") },
                text = {
                    val msg = when {
                        appState.isFlashcardMode && appState.selectedDeckId != null -> "Are you sure you want to delete this Deck?"
                        !appState.isFlashcardMode && appState.selectedNoteId != null -> "Are you sure you want to delete this Note?"
                        else -> "Are you sure you want to delete this entire Notebook and all its contents?"
                    }
                    Text(msg)
                },
                confirmButton = { Button(onClick = { appState.confirmDelete() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)) { Text("Delete") } },
                dismissButton = { TextButton(onClick = { appState.showDeleteConfirmDialog = false }) { Text("Cancel") } }
            )
        }

        if (appState.showErrorDialog) {
            AlertDialog(
                onDismissRequest = { appState.showErrorDialog = false },
                title = { Text("Import Error", fontWeight = FontWeight.Bold, color = Color.Red) },
                text = { Text(appState.errorMessage) },
                confirmButton = { Button(onClick = { appState.showErrorDialog = false }) { Text("OK") } }
            )
        }

        if (appState.showCorruptFileDialog) {
            AlertDialog(
                onDismissRequest = { /* Must actively choose */ },
                title = { Text("Save File Error", fontWeight = FontWeight.Bold, color = Color.Red) },
                text = { Text("Your save file (structure.json) is either corrupted or from an older, incompatible version of the app.\n\nContinuing will completely overwrite the file and start fresh. Would you like to overwrite it, or exit the app to manually backup the file first?") },
                confirmButton = {
                    Button(
                        onClick = { appState.overwriteSaveFile() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)
                    ) { Text("Overwrite & Start Fresh") }
                },
                dismissButton = {
                    TextButton(onClick = { onExit() }) { Text("Exit App") }
                }
            )
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Text Benjch") {
        App(onExit = ::exitApplication)
    }
}