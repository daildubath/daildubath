#ifndef PLAYER_H
#define PLAYER_H

#include <string>

// FORWARD DECLARATION: Fixes the circular include trap!
class Board;

class Player {
protected:
    std::string name;
    char token;

public:
    // Using const std::string& fixes the Clang-Tidy warning
    Player(const std::string& playerName, char playerToken);
    virtual ~Player() = default;

    std::string getName() const;
    char getToken() const;
    virtual int getMove(const Board& board) = 0;
};

#endif