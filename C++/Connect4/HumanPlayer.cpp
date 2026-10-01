#include "HumanPlayer.h"
#include "Board.h" // Required to access board.isColumnFull
#include <iostream>

HumanPlayer::HumanPlayer(const std::string& playerName, char playerToken) : Player(playerName, playerToken) {}

int HumanPlayer::getMove(const Board& board) {
    int col;
    while (true) {
        std::cout << name << " (" << token << "), enter a column (1-7): ";
        std::cin >> col;

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(10000, '\n');
            std::cout << "Invalid input. Please enter a number.\n";
            continue;
        }

        col -= 1;

        if (col >= 0 && col < 7) {
            if (!board.isColumnFull(col)) {
                return col;
            } else {
                std::cout << "Column full. Pick another.\n";
            }
        } else {
            std::cout << "Out of bounds. Please enter a number between 1 and 7.\n";
        }
    }
}
