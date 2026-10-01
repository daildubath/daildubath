package daildubath.benjch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.skia.Image as SkiaImage
import java.io.File

class MarkdownProcessor {
    fun parseInline(text: String): AnnotatedString {
        val builder = AnnotatedString.Builder()
        var i = 0
        while (i < text.length) {
            when {
                text.startsWith("**", i) -> {
                    val end = text.indexOf("**", i + 2)
                    if (end != -1) { builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text.substring(i + 2, end)) }; i = end + 2 }
                    else { builder.append(text[i]); i++ }
                }
                text.startsWith("*", i) -> {
                    val end = text.indexOf("*", i + 1)
                    if (end != -1) { builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append(text.substring(i + 1, end)) }; i = end + 1 }
                    else { builder.append(text[i]); i++ }
                }
                text.startsWith("`", i) -> {
                    val end = text.indexOf("`", i + 1)
                    if (end != -1) { builder.withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = Color(0xFFE0E0E0), color = Color(0xFFD32F2F))) { append(text.substring(i + 1, end)) }; i = end + 1 }
                    else { builder.append(text[i]); i++ }
                }
                text.startsWith("[", i) -> {
                    val textEnd = text.indexOf("](", i)
                    if (textEnd != -1) {
                        val urlEnd = text.indexOf(")", textEnd)
                        if (urlEnd != -1) {
                            val linkText = text.substring(i + 1, textEnd)
                            val url = text.substring(textEnd + 2, urlEnd)

                            builder.pushLink(LinkAnnotation.Url(url))
                            builder.withStyle(SpanStyle(color = Color.Blue, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)) { append(linkText) }
                            builder.pop()

                            i = urlEnd + 1
                            continue
                        }
                    }
                    builder.append(text[i]); i++
                }
                else -> { builder.append(text[i]); i++ }
            }
        }
        return builder.toAnnotatedString()
    }
}

@Composable
fun MarkdownViewer(text: String, processor: MarkdownProcessor = remember { MarkdownProcessor() }) {
    val lines = text.split("\n")
    var i = 0

    SelectionContainer {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            while (i < lines.size) {
                val line = lines[i]
                when {
                    line.startsWith("---") || line.startsWith("***") -> { Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray, thickness = 2.dp); i++ }
                    line.startsWith("# ") -> { Text(processor.parseInline(line.removePrefix("# ")), fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp)); i++ }
                    line.startsWith("## ") -> { Text(processor.parseInline(line.removePrefix("## ")), fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp)); i++ }
                    line.startsWith("> ") -> {
                        Row(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                            Box(modifier = Modifier.width(4.dp).height(24.dp).background(Color.Gray))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(processor.parseInline(line.removePrefix("> ")), fontStyle = FontStyle.Italic, color = Color.DarkGray)
                        }
                        i++
                    }
                    line.startsWith("![") -> {
                        val textEnd = line.indexOf("](")
                        val urlEnd = line.indexOf(")", textEnd)
                        if (textEnd != -1 && urlEnd != -1) {
                            val altText = line.substring(2, textEnd)
                            val url = line.substring(textEnd + 2, urlEnd)

                            val imageFile = File(StorageManager.baseDir, url)
                            if (imageFile.exists()) {
                                val bitmap: ImageBitmap? = remember(imageFile.absolutePath) {
                                    try {
                                        val bytes = imageFile.readBytes()
                                        SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
                                    } catch (_: Exception) { null }
                                }
                                if (bitmap != null) {
                                    Image(bitmap = bitmap, contentDescription = altText, modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth())
                                } else {
                                    Text("Failed to load image: $url", color = Color.Red)
                                }
                            } else {
                                Text("Image not found locally: $url", color = Color.Red)
                            }
                        }
                        i++
                    }
                    line.startsWith("- ") || line.startsWith("* ") -> {
                        Row(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
                            Text("•", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text(processor.parseInline(line.substring(2)))
                        }
                        i++
                    }
                    line.firstOrNull()?.isDigit() == true && line.contains(". ") -> {
                        val splitIndex = line.indexOf(". ")
                        val num = line.substring(0, splitIndex)
                        Row(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
                            Text("$num.", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text(processor.parseInline(line.substring(splitIndex + 2)))
                        }
                        i++
                    }
                    line.startsWith("|") -> {
                        val tableRows = mutableListOf<String>()
                        while (i < lines.size && lines[i].startsWith("|")) { tableRows.add(lines[i]); i++ }
                        RenderTable(tableRows, processor)
                    }
                    line.isNotBlank() -> { Text(processor.parseInline(line), modifier = Modifier.padding(bottom = 8.dp)); i++ }
                    else -> i++
                }
            }
        }
    }
}

@Composable
fun RenderTable(rows: List<String>, processor: MarkdownProcessor) {
    if (rows.size < 3) return
    val headers = rows[0].split("|").map { it.trim() }.filter { it.isNotEmpty() }
    val dataRows = rows.drop(2)
    Column(modifier = Modifier.padding(vertical = 8.dp).background(Color(0xFFF5F7FA)).padding(1.dp)) {
        Row(modifier = Modifier.background(Color(0xFFD3D9E8))) {
            headers.forEach { header -> Text(text = processor.parseInline(header), fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(8.dp)) }
        }
        Divider(color = Color.Gray)
        dataRows.forEach { row ->
            val cells = row.split("|").map { it.trim() }.filter { it.isNotEmpty() }
            if (cells.isNotEmpty()) {
                Row { cells.forEach { cell -> Text(text = processor.parseInline(cell), modifier = Modifier.weight(1f).padding(8.dp)) } }
                Divider(color = Color.LightGray)
            }
        }
    }
}