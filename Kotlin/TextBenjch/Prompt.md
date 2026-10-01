I need you to play the role of a very experienced Kotlin Developer working on an important project. You will need to write quality code broken into parts, meetings specific requirements and working feature by feature. It needs to be efficient and written well. It should be easy to understand. The IDE used will be IntelliJ IDEA.
The project is in Kotlin. It's main purpose is for desktop use, such as evernotes or joplin. It will be tested on a debian linux distro, but it needs to be able to work on Windows as mac as well. This will take planning. It may be adapted for mobile later, but it is not within the initial scope. Build it in such a way that the code could be adapted later.
You will be given feedback throughout of errors that are recieved, bugs, and changes to the requirements. There are five main requirements in the way the code is written that will be checked in the end. You will also need to pick at least one additional requirement that you you feel would benefit the project. These are the requirements:

***

# Unique Requirements 
# & Stretch Challenges
## Write one or more programs in Kotlin that demonstrates the following 5 things:

1. Variables (mutable and immutable)

2. Conditionals

3. Loops

4. Functions

5. Classes

## Stretch Challenges (select one):

- Modify your program to demonstrate the creation and modification of collections.

- Modify your program to demonstrate data classes.

- Modify your program to demonstrate the varied uses of the when keyword.

**After all of the code is finished, you will be asked to comment where these requirements are met**

***

The following is the MVP and additional features that can be added:


# MVP

## MVP PART 1:

- The user can create notebooks which can contain notes also created by the user
- The notes and notebooks are included on the left in a bar
- The user can name the notebook and notes
- The user can type into the notes to the right of the bar
- A top "head" bar is included with the title "Text Benjch"
- The top left bar is titled Note Books
- The top Note Books title has a plus next to it which creates a new notebook

## MVP Part 2:

- The Notebooks can collapse, hiding the notes
- Users can drag notes to different notebooks, automatically updating the structure
- The notes are automatically saved either in Documents\Benjch\ for Windows users, or in a .Benjch folder in the home directory for Linux/Mac.
- Notes are saved as TXT with a Json saving the structure

## MVP Part 3:

- notes and notebooks can be deleted with a delete button on the bottom right of the left bar
- Before deleting notes the user is asked to confirm
- The user can import txt files by clicking a folders button to the left of the delete button on the bottom of the left bar
- A settings bar is included in the bottom left of the bar. It does not yet need to be active.

*The following parts are past the MVP, and will be added in chunks divided into parts*

# Markdown Language
*Markdown will add a ton to the notebook, allowing for bolding, italicizing, code snippets, and so on.*

## MD PART 1:

- An md button is included at the top of the page in the top bar, as well as a txt
- After them a "T|M" button is included.
- When the txt button is pushed, the raw text is shown, just as before
- When the MD file is pushed, the markdown language is shown
- When the T|M button is pushed, the markdown is displayed on the right, and the text on the left.

## MD PART 2:

- When settings is clicked, the user can pick whether the files are saved as MD's or TXT's.
- Only documents created or edited after the change are effected. (past txt's won't become MD's)

# MD PART 3:

- When the import button is clicked, the user can import an image
- The program automatically detects what type of file is included
- If a TXT or MD file is included, it imports it as a note
- If an image file is included, a copy is made in the folder, and the local copy is referenced using MD so that it can be included in the notes
- If another type of file is typed, the error is caught and the user informed

# FLASH CARDS
*The Flash card has a "preview" within the wireframe to save space. It should take up *
## Flash Cards PART 1:

- A flash cards button is included on the top bar on the far right
- When clicked, a delete, edit, and new button appear in the main window (where the text was)
- The "Flash Cards" button in the top right says "Notes" while in flashcard view
- a forward and backward arrow are included on the left and right
- a shuffle button is below
- A smaller text window is included in the center, that has centered text (like a flashcard) and has much larger text


## IMPORTANT INFO ON THE CARDS!!!
The cards should save as a JSON. The user can have multiple sets of flashcards, each with their own JSON. Each json can hold many cards. The JSON file must include the Notebook it belongs to (Scriptures for example), the flashcard set name (Old Testament Review), and then the individual cards, each containing a back and front (Card 1: Who slew Goliath?, David son of Jesse) The main structure json file should also include these cards with the notes. The UI will only display one or the other, so the type should be specified. 

## FLash Cards Part 2:

- The note books bar on the left switches to say "Flash Cards"
- The Note Books stay the same, but the notes disapear
- When the user clicks add, it creates a new flash card set
- The flash card set is automatically saved as a json
- The flash cards are not visible under "Notebooks" when switched back

## Flash Cards PART 3:

- The user can type in the text box when "Edit" is clicked.
- The text is limited by the size of the card (so there is a character limit)
- The card has two sides, (Front and back) which can be switched between when the card is clicked
- Same restriction applies to both sides
- When delete is clicked, the card is deleted
- When new is clicked, a new card is created

# Flash Cards Part 4

- When Shuffle is clicked, the card in memory are placed in a random order
- The user can click forward and back through the list looking at each card
- The card cannot be edited unless "Edit" is clicked. When the card is clicked, it simply switches sides.
- The cards front and back are different colors.

# EXTRA ACHIEVEMENTS
*These do not need to necissarily be done at the end*

## Settings:
In the settings, allow the user to change the color of the following:
    - Text Color
    - Note background color
    - Bar Colors
    - Additional Colors (outlines, plus buttons, settings button, etc)
    - Flash Card Front
    - Flash Card Back
In settings, Allow the user to set the default:
    - File save type (md or txt)
    - Default open table (txt, md, T|M)
    - Whether the flash cards are shuffled by default or not
    - Whether system spell check is on or off
    - Turn the visual on or off
Settings are saved in a json file so that each time the app is loaded they are the same
## Visual
    - Creat animation for the left bar unfolding when notebooks clicked
    - Have tree lines as shown in the wireframe included under notebooks or next to the contents when open

*These should be done at the end if the project is met before it's deadline*

## Drawing:

Previously I created a java drawing app in Java. If time allows (this would be very last) take that code and integrate it so that the user can draw, and add images to the notes themselves, and go back and edit those images. The app is already completely functional and could be translated if needed. It can save as png's or as a custom data type. This will create a "Draw" tab next to flashcards. Requirements for this will be included if it is reached. Until then, leave the drawing button off of the program. 

## Compatability
Make the Notebook android compatable. This also should be discussed only once everything else is complete. 

***

Please give me any questions you may have. I will answer the questions first. After all the questions are answered we will verify we are on the same page about what teck stack is being used, and what stretch challenge you chose. You may also stop at any time to ask questions, ask for content (such as icons), or anything else you may need to continue.
