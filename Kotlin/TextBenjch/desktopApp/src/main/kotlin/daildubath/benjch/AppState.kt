package daildubath.benjch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import java.util.UUID
import javax.swing.JFileChooser
import javax.swing.UIManager

class AppState {
    var notebooks by mutableStateOf<List<Notebook>>(emptyList())
        private set

    // --- NEW SETTINGS STATE ---
    var settings by mutableStateOf(StorageManager.loadSettings())
        private set

    var selectedNotebookId by mutableStateOf<String?>(null)
    var selectedNoteId by mutableStateOf<String?>(null)
    var selectedDeckId by mutableStateOf<String?>(null)
    var currentNoteContent by mutableStateOf("")

    var draggedNoteId by mutableStateOf<String?>(null)
    var draggedDeckId by mutableStateOf<String?>(null)
    var currentDropTargetNotebookId by mutableStateOf<String?>(null)
    var dragPosition by mutableStateOf(Offset.Zero)
    val notebookBounds = mutableMapOf<String, Rect>()

    var showDeleteConfirmDialog by mutableStateOf(false)
    var showSettingsDialog by mutableStateOf(false)
    var showResetSettingsDialog by mutableStateOf(false) // New reset confirmation dialog
    var showErrorDialog by mutableStateOf(false)
    var showCorruptFileDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    // ViewMode now defaults to whatever is in the settings
    var viewMode by mutableStateOf(settings.defaultViewMode)

    var isFlashcardMode by mutableStateOf(false)
    var currentCardIndex by mutableStateOf(0)
    var isCardFlipped by mutableStateOf(false)
    var isEditingCard by mutableStateOf(false)

    val markdownProcessor = MarkdownProcessor()

    init {
        try {
            notebooks = StorageManager.loadStructure()
        } catch (_: Exception) {
            notebooks = emptyList()
            showCorruptFileDialog = true
        }
    }

    // --- NEW SETTINGS FUNCTIONS ---
    fun updateSettings(newSettings: AppSettings) {
        settings = newSettings
        StorageManager.saveSettings(newSettings)
    }

    fun resetSettingsToDefault() {
        val defaultSettings = AppSettings()
        updateSettings(defaultSettings)
        showResetSettingsDialog = false
        showSettingsDialog = false
    }

    fun overwriteSaveFile() {
        notebooks = emptyList()
        save()
        showCorruptFileDialog = false
    }

    private fun save() = StorageManager.saveStructure(notebooks)

    private fun updateNotebook(notebookId: String, updater: (Notebook) -> Notebook) {
        notebooks = notebooks.map { if (it.id == notebookId) updater(it) else it }
        save()
    }

    private fun updateActiveNote(updater: (Note) -> Note) {
        val noteId = selectedNoteId ?: return
        updateNotebook(selectedNotebookId ?: return) { nb ->
            nb.copy(notes = nb.notes.map { if (it.id == noteId) updater(it) else it })
        }
    }

    private fun updateActiveDeck(updater: (Deck) -> Deck) {
        val dId = selectedDeckId ?: return
        updateNotebook(selectedNotebookId ?: return) { nb ->
            nb.copy(decks = nb.decks.map { if (it.id == dId) updater(it) else it })
        }
    }

    private fun <T> moveSidebarItem(
        itemId: String, targetId: String, getItems: (Notebook) -> List<T>,
        getId: (T) -> String, setItems: (Notebook, List<T>) -> Notebook, onMoved: () -> Unit
    ) {
        val itemToMove = notebooks.flatMap(getItems).find { getId(it) == itemId } ?: return
        notebooks = notebooks.map { nb ->
            when (nb.id) {
                targetId -> setItems(nb, getItems(nb) + itemToMove).copy(isExpanded = true)
                else -> setItems(nb, getItems(nb).filter { getId(it) != itemId })
            }
        }
        save()
        onMoved()
    }

    private inline fun withValidCard(crossinline action: (MutableList<Flashcard>, Deck) -> Unit) {
        val cards = getActiveDeckCards()
        if (cards.isEmpty() || currentCardIndex !in cards.indices) return
        updateActiveDeck { deck ->
            val newCards = deck.cards.toMutableList()
            action(newCards, deck)
            deck.copy(cards = newCards)
        }
    }

    fun addNotebook() { notebooks = notebooks + Notebook(); save() }

    fun toggleNotebookCollapse(notebookId: String) = updateNotebook(notebookId) { it.copy(isExpanded = !it.isExpanded) }

    fun updateNotebookName(notebookId: String, newName: String) = updateNotebook(notebookId) { it.copy(name = newName) }

    fun addNoteToNotebook(notebookId: String, title: String = "New Note", content: String = "", forceExtension: String? = null) {
        val ext = forceExtension ?: settings.defaultSaveExtension // Routed through settings
        val newNote = Note(title = title, fileExtension = ext)
        updateNotebook(notebookId) { it.copy(notes = it.notes + newNote, isExpanded = true) }
        StorageManager.saveNoteContent(newNote.id, content, ext)
        selectNote(notebookId, newNote.id)
    }

    fun selectNote(notebookId: String, noteId: String) {
        val note = notebooks.flatMap { it.notes }.find { it.id == noteId }
        selectedNotebookId = notebookId
        selectedNoteId = noteId
        selectedDeckId = null
        currentNoteContent = if (note != null) StorageManager.loadNoteContent(note.id, note.fileExtension) else ""
        viewMode = settings.defaultViewMode // Reset view mode to default on new note selection
    }

    fun selectNotebook(notebookId: String) {
        selectedNotebookId = notebookId
        selectedNoteId = null
        selectedDeckId = null
        currentNoteContent = ""
        currentCardIndex = 0
        isCardFlipped = false
        isEditingCard = false
    }

    fun updateSelectedNoteTitle(newTitle: String) = updateActiveNote { it.copy(title = newTitle) }

    fun updateSelectedNoteContent(newContent: String) {
        currentNoteContent = newContent
        val activeNote = notebooks.flatMap { it.notes }.find { it.id == selectedNoteId } ?: return

        if (activeNote.fileExtension != settings.defaultSaveExtension) {
            StorageManager.deleteNoteFile(activeNote.id, activeNote.fileExtension)
            updateActiveNote { it.copy(fileExtension = settings.defaultSaveExtension) }
        }
        StorageManager.saveNoteContent(activeNote.id, newContent, settings.defaultSaveExtension)
    }

    fun getSelectedNoteTitle(): String { return notebooks.find { it.id == selectedNotebookId }?.notes?.find { it.id == selectedNoteId }?.title ?: "" }

    fun moveNoteToNotebook(noteId: String, targetNotebookId: String) = moveSidebarItem(
        itemId = noteId, targetId = targetNotebookId, getItems = { it.notes }, getId = { it.id },
        setItems = { nb, items -> nb.copy(notes = items) },
        onMoved = { if (selectedNoteId == noteId) selectedNotebookId = targetNotebookId }
    )

    fun moveDeckToNotebook(deckId: String, targetNotebookId: String) = moveSidebarItem(
        itemId = deckId, targetId = targetNotebookId, getItems = { it.decks }, getId = { it.id },
        setItems = { nb, items -> nb.copy(decks = items) },
        onMoved = { if (selectedDeckId == deckId) selectedNotebookId = targetNotebookId }
    )

    fun requestDelete() { if (selectedNoteId != null || selectedNotebookId != null || selectedDeckId != null) showDeleteConfirmDialog = true }

    fun confirmDelete() {
        val nId = selectedNotebookId
        val noteId = selectedNoteId
        val deckId = selectedDeckId

        if (isFlashcardMode && deckId != null) {
            updateNotebook(nId!!) { it.copy(decks = it.decks.filter { d -> d.id != deckId }) }
            selectedDeckId = null
        } else if (!isFlashcardMode && noteId != null) {
            val note = notebooks.flatMap { it.notes }.find { it.id == noteId }
            updateNotebook(nId!!) { it.copy(notes = it.notes.filter { n -> n.id != noteId }) }
            if (note != null) StorageManager.deleteNoteFile(noteId, note.fileExtension)
            selectedNoteId = null
            currentNoteContent = ""
        } else if (nId != null) {
            notebooks.find { it.id == nId }?.notes?.forEach { StorageManager.deleteNoteFile(it.id, it.fileExtension) }
            notebooks = notebooks.filter { it.id != nId }
            selectedNotebookId = null
        }
        save()
        showDeleteConfirmDialog = false
    }

    fun importFile() {
        val targetNotebookId = selectedNotebookId
        if (targetNotebookId == null) {
            errorMessage = "Please select a Notebook first to import into."
            showErrorDialog = true
            return
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            val chooser = JFileChooser().apply { dialogTitle = "Select a File to Import" }
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                val file = chooser.selectedFile
                if (file != null && file.exists()) {
                    when (val ext = file.extension.lowercase()) {
                        "txt", "md" -> { addNoteToNotebook(targetNotebookId, file.nameWithoutExtension, file.readText(), ext) }
                        "png", "jpg", "jpeg", "gif" -> {
                            if (selectedNoteId == null) { errorMessage = "Please select a Note before importing an image."; showErrorDialog = true; return }
                            val newFileName = "${UUID.randomUUID()}.$ext"
                            file.copyTo(File(StorageManager.baseDir, newFileName))
                            updateSelectedNoteContent(currentNoteContent + "\n![${file.nameWithoutExtension}]($newFileName)\n")
                        }
                        else -> { errorMessage = "Unsupported file type: .$ext. Please import TXT, MD, or an Image (PNG/JPG)."; showErrorDialog = true }
                    }
                }
            }
        } catch (_: Exception) {}
    }

    fun addDeckToNotebook(notebookId: String, name: String = "New Deck") {
        val newDeck = Deck(name = name)
        updateNotebook(notebookId) { it.copy(decks = it.decks + newDeck, isExpanded = true) }
        selectDeck(notebookId, newDeck.id)
    }

    fun selectDeck(notebookId: String, deckId: String) {
        selectedNotebookId = notebookId
        selectedNoteId = null
        selectedDeckId = deckId
        currentCardIndex = 0
        isCardFlipped = false
        isEditingCard = false

        // Handle auto-shuffle default
        if (settings.shuffleFlashcardsDefault) shuffleDeck()
    }

    fun getSelectedDeckName(): String {
        return notebooks.find { it.id == selectedNotebookId }?.decks?.find { it.id == selectedDeckId }?.name ?: ""
    }

    fun updateSelectedDeckName(newName: String) = updateActiveDeck { it.copy(name = newName) }

    fun getActiveDeckCards(): List<Flashcard> {
        val nb = notebooks.find { it.id == selectedNotebookId } ?: return emptyList()
        val deck = nb.decks.find { it.id == selectedDeckId } ?: return emptyList()
        return deck.cards
    }

    fun addFlashcard() {
        updateActiveDeck { it.copy(cards = it.cards + Flashcard(front = "", back = "")) }
        currentCardIndex = getActiveDeckCards().size - 1
        isCardFlipped = false
        isEditingCard = true
    }

    fun updateFlashcard(newFront: String, newBack: String) {
        withValidCard { newCards, _ ->
            newCards[currentCardIndex] = newCards[currentCardIndex].copy(front = newFront, back = newBack)
        }
    }

    fun deleteFlashcard() {
        withValidCard { newCards, _ -> newCards.removeAt(currentCardIndex) }
        if (currentCardIndex >= getActiveDeckCards().size) {
            currentCardIndex = maxOf(0, getActiveDeckCards().size - 1)
        }
        isCardFlipped = false
        if (getActiveDeckCards().isEmpty()) isEditingCard = false
    }

    fun shuffleDeck() {
        updateActiveDeck { it.copy(cards = it.cards.shuffled()) }
        currentCardIndex = 0
        isCardFlipped = false
    }
}