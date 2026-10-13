package daildubath.benjch

import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color
import java.util.UUID

@Serializable
data class Note(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "New Note",
    var fileExtension: String = "txt"
)

@Serializable
data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    var front: String = "Front of Card",
    var back: String = "Back of Card"
)

@Serializable
data class Deck(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "New Deck",
    var cards: List<Flashcard> = emptyList()
)

@Serializable
data class Notebook(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "New Notebook",
    var notes: List<Note> = emptyList(),
    var decks: List<Deck> = emptyList(),
    var isExpanded: Boolean = true
)

enum class ViewMode { TXT, MD, SPLIT }

@Serializable
data class AppSettings(
    var textColor: String = "#000000",
    var noteBackgroundColor: String = "#FFFFFF",
    var primaryBarColor: String = "#8B9DC3",
    var secondaryBarColor: String = "#D3D9E8",
    var selectionColor: String = "#C0C8DA",
    var accentColor: String = "#4A65A4",
    var flashcardFrontColor: String = "#EDE7F6",
    var flashcardBackColor: String = "#D1C4E9",
    var defaultSaveExtension: String = "txt",
    var defaultViewMode: ViewMode = ViewMode.TXT,
    var shuffleFlashcardsDefault: Boolean = false,
    var baseFontSize: Int = 16
)

// Extension functions for easy Hex <-> Compose Color conversion
fun String.isValidHexColor(): Boolean {
    return this.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"))
}

fun String.toComposeColor(): Color {
    val hex = this.removePrefix("#")
    return try {
        when (hex.length) {
            6 -> Color(hex.toLong(16) or 0xFF000000) // Applies 100% opacity to standard hex
            8 -> Color(hex.toLong(16)) // Assumes #AARRGGBB format
            else -> Color.Gray // Safe fallback
        }
    } catch (_: Exception) {
        Color.Red // Visual indicator that something failed
    }
}