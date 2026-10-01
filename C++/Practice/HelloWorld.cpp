//
// Created by benge on 5/21/26.
//
#include <iostream>
#include <cmath>
using namespace std;

int main() {
    // this is a useless comment!
    cout << "My name is Benge" << endl;
    int myNum = 23;
    cout << "I am " << myNum << " years old." << endl;
    // const int minutesPerHour = 70;
    // minutesPerHour = 60; // error
    int x;
    cout << "Type a number: "; // Type a number and press enter
    cin >> x; // Get user input from the keyboard
    cout << "Your number is: " << x << endl; // Display the input value
    cout << "Your number squared is " << x * x << endl;
    cout << "Your numbers square root is " << sqrt(x) << endl;
    cout << ((x < 10) ? "Your number is less than ten.\n" : "Your number is greater than ten.\n");
    struct {
        string brand;
        string model;
        int year;
    } myCar1, myCar2, brokenCar; // We can add variables by separating them with a comma here

    // Put data into the first structure
    myCar1.brand = "BMW";
    myCar1.model = "X5";
    myCar1.year = 1999;

    // Put data into the second structure
    myCar2.brand = "Ford";
    myCar2.model = "Mustang";
    myCar2.year = 1969;

    //Testing a pulling from memory I shouldn't
    brokenCar.brand = "Tesla";
    brokenCar.model = "Model S";

    // Print the structure members
    cout << myCar1.brand << " " << myCar1.model << " " << myCar1.year << "\n";
    cout << myCar2.brand << " " << myCar2.model << " " << myCar2.year << "\n";
    cout << brokenCar.brand << " " << brokenCar.model << " " << brokenCar.year << "\n";
    return 0;
}

/* This
 is
 a
 big
 comment!
 */
