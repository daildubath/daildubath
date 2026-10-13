# Sprint 5 - Text Benjch Note Taking App

Text Benjch is a clean, efficient desktop markdown editor and flashcard app built with Kotlin and Compose for Desktop. It features a Note Mode with a live markdown preview and a Flashcard Mode for interactive study sessions. The application supports drag-and-drop sidebar organization, external file/image importing, and a fully customizable settings menu to personalize interface colors and font sizes using persistent JSON storage.

## Instructions for Build and Use

Steps to build and/or run the software:

1. Ensure you have a Java Development Kit (JDK) 17 or higher installed on your machine.
2. Open the root project directory in your IDE of choice (IntelliJ IDEA is highly recommended).
3. Open the terminal or the IDE's Gradle window and execute the `./gradlew run` task to compile and launch the application.

Instructions for using the software:

1. **Manage Workspaces:** Create organizational folders by clicking the "+" button next to "Note Books" in the left sidebar. You can rename notebooks directly inline and click the arrow indicators to expand or collapse them.
2. **Toggle Modes:** Use the primary button in the top bar to switch between "Notes" mode and "Flash Cards" mode. Clicking the "+" icon on an expanded notebook adds a new item for whichever mode is currently active.
3. **Personalize the System:** Click the Settings gear icon at the bottom of the left panel to configure your workspace. You can change text sizes, pick your default document format (.txt or .md), or type in custom Hex codes to instantly change the application's colors.

## Development Environment

To recreate the development environment, you need the following software and/or libraries with the specified versions:

* **Java Development Kit (JDK):** Version 17 or higher
* **Kotlin SDK:** Stable 1.9+ or 2.0+ toolchain releases
* **Compose Multiplatform:** Desktop variant framework for UI declaration and rendering
* **Kotlinx Serialization:** JSON parsing components used to track app state and save user settings
* **Gradle Build Wrapper:** Automated dependency manager included natively in the build path

## Useful Websites to Learn More

I found these websites useful in developing this software:

* [Gemini AI](https://gemini.google.com/)
* [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
* [Kotlin Multiplatform Getting Started](https://kotlinlang.org/docs/multiplatform/get-started.html)
* [W3Schools Kotlin Tutorial](https://www.w3schools.com/kotlin/)

## Future Work

The following items I plan to fix, improve, and/or add to this project in the future:

* [ ] **System Spell Checker:** Implement a custom dictionary parser and text annotator wrapper for real-time error underlining.
* [ ] **Canvas Feature:** Introduce a sketchpad module to support drawing, handwriting, or rough diagram attachments inside notes.
* [ ] **General Bug Fixes:** Refine structural layout constraints and continuously polish minor edge-case UI focus behaviors.
* [ ] **Cross-Platform Compatibility:** Port core application modules into an overarching multiplatform layout targeting mobile platforms and web browsers.