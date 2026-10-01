#include "Player.h"

// Updated constructor signature
Player::Player(const std::string& playerName, char playerToken) : name(playerName), token(playerToken) {}

std::string Player::getName() const {
    return name;
}

char Player::getToken() const {
    return token;
}
// Note: getMove() is pure virtual (= 0) in the .h file, so it doesn't get implemented here!
