#ifndef HUMANPLAYER_H
#define HUMANPLAYER_H

#include "Player.h"

class HumanPlayer : public Player {
public:
    HumanPlayer(const std::string& playerName, char playerToken);
    int getMove(const Board& board) override;
};

#endif