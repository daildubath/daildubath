#include <iostream>
#include <random>
#include <thread>
#include <chrono>
#include "Board.h"
#include "HumanPlayer.h"
#include "ComputerPlayer.h"

// --- NEW ANIMATION FUNCTION ---
void animateCoinFlip() {
    // Adding static constexpr makes the warning disappear instantly!
    static constexpr char spin[] = {'|', '/', '-', '\\'};

    for (int i = 0; i < 15; ++i) {
        std::cout << "\x1B[2J\x1B[H"; // Clear screen
        std::cout << "\n\n   Flipping coin to see who starts...\n";
        std::cout << "                   " << spin[i % 4] << "\n";
        std::cout << std::flush;
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
    }
}
// ------------------------------

int main() {
    bool playAgain = true;

    while (playAgain) {
        Board gameBoard;

        Player* player1 = new HumanPlayer("Player 1", 'X');
        Player* player2 = nullptr;

        char choice;
        std::cout << "\x1B[2J\x1B[H"; // Clear the screen for the main menu
        std::cout << "Play against: \n1. Human\n2. Computer\nChoice: ";
        std::cin >> choice;

        if (choice == '1') {
            player2 = new HumanPlayer("Player 2", 'O');
        } else {
            player2 = new ComputerPlayer("Computer", 'O');
        }

        // Call the animation before we pick the winner!
        animateCoinFlip();

        // --- NEW COIN FLIP LOGIC ---
        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> coinFlip(0, 1);

        Player* currentPlayer = nullptr;
        if (coinFlip(gen) == 0) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }

        // --- LOUD START ANNOUNCEMENT ---
        std::cout << "\x1B[2J\x1B[H";
        std::cout << "========================================\n";
        std::cout << "      " << currentPlayer->getName() << " WON THE TOSS! \n";
        std::cout << "         " << currentPlayer->getName() << " GOES FIRST! \n";
        std::cout << "========================================\n";
        std::cout << std::flush;

        // Pause for 2 seconds so the player can actually read it
        std::this_thread::sleep_for(std::chrono::seconds(2));
        // -------------------------------

        bool gameOver = false;
        gameBoard.display();

        while (!gameOver) {
            int col = currentPlayer->getMove(gameBoard);
            int final_y = gameBoard.dropToken(col, currentPlayer->getToken());

            if (gameBoard.checkWin(col, final_y, currentPlayer->getToken())) {

                // Print the banner directly under the completed board
                std::cout << "\n========================================\n";
                std::cout << "   🏆 " << currentPlayer->getName() << " WINS THE GAME! 🏆\n";
                std::cout << "========================================\n\n";
                std::cout << std::flush;

                // Let them soak in the victory for 2 seconds
                std::this_thread::sleep_for(std::chrono::seconds(2));

                // PULL THE LEVER! (Trigger the falling pieces)
                gameBoard.clearBoardAnimation(currentPlayer->getName());

                gameOver = true;
            } else {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }
        }

        delete player1;
        delete player2;

        std::cout << "Play again? (y/n): ";
        char replay;
        std::cin >> replay;
        if (replay != 'y' && replay != 'Y') {
            playAgain = false;
        }
    }

    std::cout << "Thanks for playing!\n";
    return 0;
}