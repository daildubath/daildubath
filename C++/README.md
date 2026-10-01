# Project Title (Update)

Connect 4 built with classes, including a PC player option with basic logic.

## Instructions for Build and Use

Steps to build and/or run the software:

1. Make sure you have a compiler such as g++ installed on your machine.
2. Clone the repository to your local machine.
3. Enter a terminal (not in the IDE or the screen may not clear properly) and navigate to the directory where you cloned the repository.
4. Compile the code using the command `g++ main.cpp Board.cpp Player.cpp HumanPlayer.cpp ComputerPlayer.cpp -o connect4' (or with whatever compiler you have).
5. Run the compiled program using the command `./connect4` (or with whatever command is appropriate for your system).

Instructions for using the software:

1. You will be prompted to choose a game mode: Player vs Player or Player vs Computer. Take your pick!
2. Enter the numbers corresponding to the column you want to drop your piece in when prompted. The columns are numbered 1-7 from left to right.
3. If PVP, alternate between players until one wins or the board is full. If PVC, play against the computer until you win, lose, or the board is full.
4. At the end you will be prompted to play again or exit. Hit 'y' to play again or 'n' to exit.

## Development Environment

To recreate the development environment, you need the following software and/or libraries with the specified versions:

* g++ (or another C++ compiler) - version 9.3.0 or higher
* I used CLion as my IDE, but any text editor or IDE that supports C++ will work. Clion will automatically download the compiler if needed.
* To run the program, you will need a terminal that supports clearing the screen (such as Command Prompt on Windows, Terminal on macOS, or any terminal emulator on Linux).

## Useful Websites to Learn More

I found these websites useful in developing this software:

* W3Schools(https://www.w3schools.com/cpp/) This is where I learned the basics
* Gemini (https://gemini.google.com/) Sped through the building process using the schools free pro account
* Exact documentation from Windows (https://learn.microsoft.com/en-us/cpp/?view=msvc-170)

## Future Work

The following items I plan to fix, improve, and/or add to this project in the future:

* [ ] I would love to get the clear screen to actually work in the IDE
* [ ] Better test the computer logic. It was added last and hasn't been fully tested. I left the random response in just in case. 
* [ ] Spend more time on the code to make it more what I want rather than just what works and AI gave me.
