cmake_minimum_required(VERSION 4.2)
project(CSE310Project2)

set(CMAKE_CXX_STANDARD 14)

include_directories(Connect4)

add_executable(CSE310Project2
        Connect4/Board.cpp
        Connect4/Player.cpp
        Connect4/HumanPlayer.cpp
        Connect4/HumanPlayer.h
        Connect4/ComputerPlayer.cpp
        Connect4/ComputerPlayer.h
        Connect4/Board.h
        Connect4/Player.h
        Connect4/main.cpp
)
