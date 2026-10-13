package daildubath.benjch

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object StorageManager {
    private val osName = System.getProperty("os.name").lowercase()
    private val userHome = System.getProperty("user.home")

    val baseDir: File = if (osName.contains("win")) File(userHome, "Documents\\Benjch") else File(userHome, ".Benjch")

    private val structureFile = File(baseDir, "structure.json")
    private val settingsFile = File(baseDir, "settings.json") // New file for settings

    private val json = Json {
        ignoreUnknownKeys = true // Prevents crashes if we add more settings later
    }

    init { if (!baseDir.exists()) baseDir.mkdirs() }

    // --- Structure IO ---

    fun saveStructure(notebooks: List<Notebook>) {
        structureFile.writeText(json.encodeToString(notebooks))
    }

    fun loadStructure(): List<Notebook> {
        if (!structureFile.exists()) return emptyList()
        return json.decodeFromString(structureFile.readText())
    }

    // --- Settings IO ---

    fun saveSettings(settings: AppSettings) {
        settingsFile.writeText(json.encodeToString(settings))
    }

    fun loadSettings(): AppSettings {
        return if (settingsFile.exists()) {
            try {
                json.decodeFromString(settingsFile.readText())
            } catch (_: Exception) {
                AppSettings() // Fallback to defaults if file is corrupted
            }
        } else {
            AppSettings() // First-time load
        }
    }

    // --- Note File IO ---

    fun saveNoteContent(noteId: String, content: String, extension: String) {
        File(baseDir, "$noteId.$extension").writeText(content)
    }

    fun loadNoteContent(noteId: String, extension: String): String {
        val f = File(baseDir, "$noteId.$extension")
        return if (f.exists()) f.readText() else ""
    }

    fun deleteNoteFile(noteId: String, extension: String) {
        val f = File(baseDir, "$noteId.$extension")
        if (f.exists()) f.delete()
    }
}