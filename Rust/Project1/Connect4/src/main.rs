use std::collections::HashMap;
use std::io::{self, Write};
use std::thread;
use std::time::Duration;
use std::process::Command;

// Type alias to make our dictionary references cleaner
type Grid = HashMap<(i32, i32), char>;

/// Creates the dictionary grid using functional iterators
fn create_grid() -> Grid {
    (0..7)
        .flat_map(|x| (0..7).map(move |y| ((x, y), ' ')))
        .collect()
}

/// Clears the console and prints the current state of the dictionary grid
fn print_grid(grid: &Grid) {
    // Hard system clear for smooth animation
    let _ = Command::new(if cfg!(windows) { "cmd" } else { "clear" })
        .args(if cfg!(windows) { vec!["/c", "cls"] } else { vec![] })
        .status();

    println!("-----------------------------");

    for y in 0..7 {
        // Build the entire row string in one functional line!
        let row_str = (0..7)
            .map(|x| grid.get(&(x, y)).unwrap_or(&' ').to_string())
            .collect::<Vec<_>>()
            .join(" | ");

        println!("| {} |", row_str);
        println!("-----------------------------");
    }
    println!("||1   2   3   4   5   6   7||");
    println!("^^                         ^^");
}

/// Safely gets and validates integer input from the user
fn get_int() -> i32 {
    loop {
        print!("Enter a number (1-7): ");
        io::stdout().flush().unwrap();

        let mut input = String::new();
        io::stdin().read_line(&mut input).unwrap();

        match input.trim().parse::<i32>() {
            Ok(val) if (1..=7).contains(&val) => return val - 1,
            _ => println!("Invalid input. Please enter a number between 1 and 7."),
        }
    }
}

/// Checks for a win by casting rays outward ONLY from the newly dropped piece
fn check_win(grid: &Grid, last_x: i32, last_y: i32, token: char) -> bool {
    // The 4 axes: Horizontal, Vertical, Diagonal-Down-Right, Diagonal-Up-Right
    let axes = [(1, 0), (0, 1), (1, 1), (1, -1)];

    for &(dx, dy) in &axes {
        let mut count = 1; // Start at 1 (the piece we just dropped)

        // Cast ray forward
        for step in 1..4 {
            if grid.get(&(last_x + dx * step, last_y + dy * step)) == Some(&token) {
                count += 1;
            } else {
                break;
            }
        }

        // Cast ray backward
        for step in 1..4 {
            if grid.get(&(last_x - dx * step, last_y - dy * step)) == Some(&token) {
                count += 1;
            } else {
                break;
            }
        }

        // If we found 4 or more along this axis, it's a win!
        if count >= 4 {
            return true;
        }
    }
    false
}

/// Prompts the user to play again
fn play_again() -> bool {
    print!("Do you want to play again? (y/N) ");
    io::stdout().flush().unwrap();

    let mut input = String::new();
    io::stdin().read_line(&mut input).unwrap();

    input.trim().eq_ignore_ascii_case("y")
}

fn main() {
    loop {
        let mut grid = create_grid();
        let mut player_one_turn = true;

        loop {
            let token = if player_one_turn { 'X' } else { 'O' };
            print_grid(&grid);

            let player_name = if player_one_turn { "Player1" } else { "Player2" };
            println!("{}: ", player_name);

            let mut dropped_x;
            let mut dropped_y = 0;

            // Turn loop for input and dropping animation
            loop {
                dropped_x = get_int();

                // Check if the top slot (y = 0) of the selected column is empty
                if *grid.get(&(dropped_x, 0)).unwrap() == ' ' {
                    // Animation and dropping logic
                    for y in 0..7 {
                        if *grid.get(&(dropped_x, y)).unwrap() == ' ' {
                            grid.insert((dropped_x, y), token);
                            print_grid(&grid);
                            grid.insert((dropped_x, y), ' '); // remove after rendering
                            thread::sleep(Duration::from_millis(100));
                        }

                        // If we hit the bottom OR the next dictionary space down is full
                        if y == 6 || *grid.get(&(dropped_x, y + 1)).unwrap() != ' ' {
                            grid.insert((dropped_x, y), token); // Permanently place token
                            dropped_y = y; // Record the final Y coordinate
                            break;
                        }
                    }
                    break;
                } else {
                    println!("Column full.");
                }
            }

            // Immediately check if that specific drop caused a win
            if check_win(&grid, dropped_x, dropped_y, token) {
                print_grid(&grid); // Print final winning board
                let player_num = if player_one_turn { 1 } else { 2 };
                println!("Player {} wins!", player_num);
                break;
            }

            // Switch turns
            player_one_turn = !player_one_turn;
        }

        // Restart or quit
        if !play_again() {
            break;
        }
    }
}