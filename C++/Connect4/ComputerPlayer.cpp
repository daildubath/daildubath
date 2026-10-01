#include "ComputerPlayer.h"
#include "Board.h"
#include <iostream>
#include <thread>
#include <chrono>
#include <random>
#include <utility> // Needed for std::move

// Pass by value here, and std::move it up to the Player constructor
ComputerPlayer::ComputerPlayer(std::string playerName, char playerToken)
    : Player(std::move(playerName), playerToken) {}

// ... keep your existing getMove() logic exactly as it is below this!

int ComputerPlayer::getMove(const Board& board) {
    std::cout << name << " (" << token << ") is thinking...\n";
    std::this_thread::sleep_for(std::chrono::milliseconds(600));

    // Figure out what the human's token is
    char opponentToken = (token == 'X') ? 'O' : 'X';

    // 1. OFFENSE: Can I win right now?
    for (int col = 0; col < 7; ++col) {
        if (board.isWinningMove(col, token)) {
            return col; // Take the win!
        }
    }

    // 2. DEFENSE: Can the human win on their next turn?
    for (int col = 0; col < 7; ++col) {
        if (board.isWinningMove(col, opponentToken)) {
            return col; // Block them!
        }
    }

    // 3. RANDOM: If no immediate threats or wins, pick a random valid column
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> distrib(0, 6);

    while (true) {
        int col = distrib(gen);
        if (!board.isColumnFull(col)) {
            return col;
        }
    }
}

// WORKING RANDOM LOGIC BELOW!!!

// #include "ComputerPlayer.h"
// #include "Board.h" // Now we actually include Board.h here so we can use it!
// #include <iostream>
// #include <thread>
// #include <chrono>
// #include <random> // Modern C++ randomness
//
// ComputerPlayer::ComputerPlayer(const std::string& playerName, char playerToken) : Player(playerName, playerToken) {}
//
// int ComputerPlayer::getMove(const Board& board) {
//     std::cout << name << " (" << token << ") is thinking...\n";
//     std::this_thread::sleep_for(std::chrono::milliseconds(600));
//
//     // Modern C++11 Random Number Generator
//     std::random_device rd;
//     std::mt19937 gen(rd());
//     std::uniform_int_distribution<> distrib(0, 6);
//
//     while (true) {
//         // Declaration and assignment joined!
//         int col = distrib(gen);
//
//         if (!board.isColumnFull(col)) {
//             return col;
//         }
//     }
// }