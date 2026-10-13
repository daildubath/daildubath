package daildubath.benjch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DraggableSidebarItem(
    id: String,
    name: String,
    isSelected: Boolean,
    isBeingDragged: Boolean,
    appState: AppState,
    notebookId: String,
    onClick: () -> Unit,
    onMove: (String, String) -> Unit,
    setDraggedId: (String?) -> Unit
) {
    var globalPosition by remember { mutableStateOf(Offset.Zero) }

    Text(
        text = name.ifEmpty { "Untitled" },
        color = appState.settings.textColor.toComposeColor(),
        fontSize = appState.settings.baseFontSize.sp,
        modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 2.dp, bottom = 2.dp).background(
            when {
                isBeingDragged -> appState.settings.primaryBarColor.toComposeColor().copy(alpha = 0.5f)
                isSelected -> appState.settings.selectionColor.toComposeColor()
                else -> androidx.compose.ui.graphics.Color.Transparent
            }
        ).clickable { onClick() }
            .onGloballyPositioned { globalPosition = it.localToWindow(Offset.Zero) }
            .pointerInput(id) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset -> setDraggedId(id); appState.dragPosition = globalPosition + offset },
                    onDragEnd = {
                        val targetId = appState.currentDropTargetNotebookId
                        if (targetId != null && targetId != notebookId) onMove(id, targetId)
                        setDraggedId(null); appState.currentDropTargetNotebookId = null
                    },
                    onDragCancel = { setDraggedId(null); appState.currentDropTargetNotebookId = null },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        appState.dragPosition += dragAmount
                        appState.currentDropTargetNotebookId = appState.notebookBounds.entries.find { it.value.contains(appState.dragPosition) }?.key
                    }
                )
            }.padding(4.dp)
    )
}

@Composable
fun NotebookItem(notebook: Notebook, appState: AppState) {
    val isDropTarget = appState.currentDropTargetNotebookId == notebook.id
    val isNotebookSelected = appState.selectedNotebookId == notebook.id && appState.selectedNoteId == null && appState.selectedDeckId == null

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(
            when {
                isDropTarget -> appState.settings.primaryBarColor.toComposeColor().copy(alpha = 0.7f)
                isNotebookSelected -> appState.settings.selectionColor.toComposeColor().copy(alpha = 0.5f)
                else -> androidx.compose.ui.graphics.Color.Transparent
            }
        ).onGloballyPositioned { appState.notebookBounds[notebook.id] = it.boundsInWindow() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { appState.selectNotebook(notebook.id) },
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { appState.toggleNotebookCollapse(notebook.id) }, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = if (notebook.isExpanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Toggle Expand",
                    tint = appState.settings.textColor.toComposeColor()
                )
            }
            TextField(
                value = notebook.name,
                onValueChange = { appState.updateNotebookName(notebook.id, it) },
                textStyle = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (appState.settings.baseFontSize + 2).sp,
                    color = appState.settings.textColor.toComposeColor()
                ),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = androidx.compose.ui.graphics.Color.Transparent, focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent, unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent),
                modifier = Modifier.weight(1f).onFocusChanged { if (it.isFocused) appState.selectNotebook(notebook.id) }
            )
            IconButton(
                onClick = {
                    if (appState.isFlashcardMode) appState.addDeckToNotebook(notebook.id)
                    else appState.addNoteToNotebook(notebook.id)
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item", tint = appState.settings.textColor.toComposeColor())
            }
        }

        if (notebook.isExpanded) {
            if (appState.isFlashcardMode) {
                notebook.decks.forEach { deck ->
                    DraggableSidebarItem(
                        id = deck.id,
                        name = deck.name,
                        isSelected = (deck.id == appState.selectedDeckId),
                        isBeingDragged = (deck.id == appState.draggedDeckId),
                        appState = appState,
                        notebookId = notebook.id,
                        onClick = { appState.selectDeck(notebook.id, deck.id) },
                        onMove = { id, target -> appState.moveDeckToNotebook(id, target) },
                        setDraggedId = { appState.draggedDeckId = it }
                    )
                }
            } else {
                notebook.notes.forEach { note ->
                    DraggableSidebarItem(
                        id = note.id,
                        name = note.title,
                        isSelected = (note.id == appState.selectedNoteId),
                        isBeingDragged = (note.id == appState.draggedNoteId),
                        appState = appState,
                        notebookId = notebook.id,
                        onClick = { appState.selectNote(notebook.id, note.id) },
                        onMove = { id, target -> appState.moveNoteToNotebook(id, target) },
                        setDraggedId = { appState.draggedNoteId = it }
                    )
                }
            }
        }
    }
}