use std::io::{self, Write};

// Requirement: Enum
enum CalculationStatus {
    Success(u64),
    Failure(String),
}

// Requirement: Struct
// We use this to package our user's preferences.
struct Config {
    verbose: bool,
}

fn main() {
    // Requirement: Mutable Variable
    let mut history: Vec<CalculationStatus> = Vec::new();

    // Ask for verbosity once at the start
    let config = Config {
        verbose: ask_verbose(),
    };

    // Requirement: Loop
    // This loop allows the user to calculate multiple numbers.
    loop {
        println!("\n--- Factorial Menu ---");
        print!("Enter a number (0-20) or 'q' to quit: ");
        io::stdout().flush().unwrap();

        let mut input = String::new();
        io::stdin().read_line(&mut input).expect("Failed to read");
        let input = input.trim();

        if input == "q" { break; }

        // Requirement: Match
        let n: u64 = match input.parse() {
            Ok(num) => num,
            Err(_) => {
                println!("Invalid input. Please enter a number.");
                continue; // Restart the loop
            }
        };

        // Requirement: Result as a return type
        let result = safe_factorial(n, config.verbose);

        match result {
            Ok(val) => {
                println!("Final Result: {}", val);
                history.push(CalculationStatus::Success(val));
            }
            Err(e) => {
                println!("Error: {}", e);
                history.push(CalculationStatus::Failure(e));
            }
        }
    }

    // Requirement: Function (Borrowing reference)
    display_history(&history);
}

fn ask_verbose() -> bool {
    print!("Enable verbose mode? (y/n): ");
    io::stdout().flush().unwrap();
    let mut choice = String::new();
    io::stdin().read_line(&mut choice).unwrap();
    choice.trim().to_lowercase() == "y"
}

fn safe_factorial(n: u64, verbose: bool) -> Result<u64, String> {
    if n > 20 {
        return Err(format!("{} is too high for u64 (Max 20)", n));
    }
    Ok(recursive_calc(n, verbose))
}

fn recursive_calc(n: u64, verbose: bool) -> u64 {
    if n == 0 {
        if verbose { println!("  Reached base case: 0! = 1"); }
        1
    } else {
        if verbose { println!("  Processing: {} * {}!", n, n-1); }
        let res = n * recursive_calc(n - 1, verbose);
        if verbose { println!("  Step Result: {}! = {}", n, res); }
        res
    }
}

fn display_history(history: &Vec<CalculationStatus>) {
    println!("\n--- Session History ---");
    for (i, item) in history.iter().enumerate() {
        match item {
            CalculationStatus::Success(v) => println!("Run {}: Success ({})", i + 1, v),
            CalculationStatus::Failure(e) => println!("Run {}: Failed ({})", i + 1, e),
        }
    }
}