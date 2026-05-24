#ifndef COMPUTERPLAYER_H
#define COMPUTERPLAYER_H

#include "Player.h"
#include <string>

class ComputerPlayer : public Player {
public:
    // Pass by value
    ComputerPlayer(std::string playerName, char playerToken);
    int getMove(const class Board& board) override;
};

#endif



// WORKING CONSTRUCTOR
// #ifndef COMPUTERPLAYER_H
// #define COMPUTERPLAYER_H
//
// #include "Player.h"
//
// class ComputerPlayer : public Player {
// public:
//     ComputerPlayer(std::string playerName, char playerToken);
//     int getMove(const Board& board) override;
// };
//
// #endif
