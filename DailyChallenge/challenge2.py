# Good morning! Here's your coding interview problem for today.

# This problem was asked by Stripe.

# Given an array of integers, find the first missing positive integer in linear time and constant space. In other words, find the lowest positive integer that does not exist in the array. The array can contain duplicates and negative numbers as well.

# For example, the input [3, 4, -1, 1] should give 2. The input [1, 2, 0] should give 3.

# You can modify the input array in-place.

import random


test1 = [3, 4, -1, 1]
test2 = [1, 2, 0]

start = 1

while True:
    if start in test2:
        start += 1
    else:
        break

print(f"The lowest integer is {start}")

# Am I just not understanding the question, or was that easier than the first?

def find_first(array):
    int = 1

    while True:
        if int in array:
            int += 1
        else:
            return int
        
# Let's copy the array creating code from before:

def get_array():
    numbers = []
    for i in range(15):
        randomInt = random.randint(-7, 7)
        numbers.append(randomInt)
    return numbers


# And test our function out:

for i in range(15):
    array = get_array()
    lowest_int = find_first(array)
    array.sort()
    print(f"In {array} the lowest missing positive int is {lowest_int}.")
    print()