import os
import time

# Written by Erik Woodruff and Andrew Benge, comments by AI
# written Dec 2024
# written for Erik's final project
# Feel free to play around, but I wouldn't recommend messing with the diagonal check logic
# unless you really know what your doing....
# improvements could be modulizing the game and adding new games with the setup (three way, special tokens, connect4 pop, etc.)
# The game could also use a lot of optimizing. There are a lot of for loops in here! A LOT of for loops. 

grid = []  # Game grid

def set_grid():
    grid.clear()  # Clear grid
    for column in range(7):  # Create columns
        grid.append([])
        for row in range(7):  # Create rows
            grid[column].append(" ")

def print_grid():
    os.system(clear := 'cls' if os.name == 'nt' else 'clear')  # Clear screen
    print("-----------------------------")
    for column in range(7):  # Print grid rows
        print("| ", end="")
        for row in range(7):  # Print grid columns
            print(grid[row][column], end=" | ")
        print("\n-----------------------------")
    print("||1   2   3   4   5   6   7||")  # Column numbers
    print("^^                         ^^")

def get_int():
    while True:  # Input loop
        try:
            value = int(input("Enter a number (1-7): ")) - 1  # Get column
            if 0 <= value <= 6:  # Validate input
                return value
            else:
                print("Please enter a number between 1 and 7.")
        except ValueError:
            print("Invalid input. Please enter a valid integer.")

def check():
    p1 = 0  # Player 1 counter
    p2 = 0  # Player 2 counter

    # Check horizontal
    for column in range(7):
        p1 = 0
        p2 = 0
        for row in range(7):
            if grid[column][row] == 'X':
                p1 += 1
                p2 = 0
            elif grid[column][row] == 'O':
                p2 += 1
                p1 = 0
            elif grid[column][row] == ' ':
                p1 = 0
                p2 = 0
            if p1 == 4:
                print("Player 1 wins")
                again()
            if p2 == 4:
                print("Player 2 wins")
                again()

    p1 = 0
    p2 = 0

    # Check vertical
    for row in range(7):
        p1 = 0
        p2 = 0
        for column in range(7):
            if grid[column][row] == 'X': 
                p1 += 1
                p2 = 0
            elif grid[column][row] == 'O':
                p2 += 1
                p1 = 0
            elif grid[column][row] == ' ':
                p1 = 0
                p2 = 0
            if p1 == 4: 
                print("Player 1 wins")
                again()
            if p2 == 4: 
                print("Player 2 wins")
                again()

    # Check diagonal (top-left to bottom-right)
    diag1 = []
    for column in grid:
        diag1.append([])
        for row in column:
            diag1[-1].append(row)

    beginning = 0
    end = 6
    for column in range(7):
        if beginning != 0:
            for i in range(beginning):  # Add padding
                diag1[column].append(' ')
        if end != 0:
            for i in range(end):  # Add padding
                diag1[column].insert(0, ' ')
        beginning += 1
        end -= 1

    p1 = 0
    p2 = 0

    for diag in range(13):  # Check diagonals
        p1 = 0
        p2 = 0
        for cross in range(7):
            if diag1[cross][diag] == 'X':  
                p1 += 1
                p2 = 0
            elif diag1[cross][diag] == 'O':  
                p2 += 1
                p1 = 0
            elif diag1[cross][diag] == ' ': 
                p1 = 0
                p2 = 0
            if p1 == 4: 
                print("Player 1 wins")
                again()
            if p2 == 4:  
                print("Player 2 wins")
                again()

    # Check diagonal (top-right to bottom-left)
    diag2 = []
    for column in grid:
        diag2.append([])
        for row in column:
            diag2[-1].append(row)

    beginning = 6
    end = 0
    for column in range(7):
        if beginning != 0:
            for i in range(beginning):  # Add padding
                diag2[column].append(' ')
        if end != 0:
            for i in range(end):  # Add padding
                diag2[column].insert(0, ' ')
        beginning -= 1
        end += 1

    p1 = 0
    p2 = 0

    for diag in range(13):  # Check diagonals
        p1 = 0
        p2 = 0
        for cross in range(7):
            if diag2[cross][diag] == 'X':  
                p1 += 1
                p2 = 0
            elif diag2[cross][diag] == 'O':  
                p2 += 1
                p1 = 0
            elif diag2[cross][diag] == ' ': 
                p1 = 0
                p2 = 0
            if p1 == 4:  
                print("Player 1 wins")
                again()
            if p2 == 4:  
                print("Player 2 wins")
                again()

def again():
    resume = input("Do you want to play again? (y/N) ")  # Replay prompt
    if resume.lower() == "y":
        main()  # Restart game
    else:
        quit()  # Exit game

def main():
    set_grid()  # Initialize grid

    player = True  # Player 1 starts

    while True:
        token = "X" if player else "O"  # Set token for current player

        print_grid()  # Display grid
        check()  # Check for win

        print("Player1:" if player else "Player2:", end=" ")

        while True:
            column = get_int()  # Get column input
            if ' ' in grid[column]:  # Check if column is not full
                for i in range(7): 
                    if grid[column][i] == " ": # animation
                        grid[column][i] = token
                        print_grid()
                        grid[column][i] = " "
                        time.sleep(0.1)  # Delay for animation
                    if grid[column][i] != " ":  # Place token
                        grid[column][i - 1] = token
                        break
                    elif i == 6:  # Place token at bottom
                        grid[column][i] = token
                break
            else:
                print("Column full.")  # Column full message

        player = not player  # Switch player

if __name__ == "__main__":
    main()  # Start game
