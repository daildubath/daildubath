# Good morning! Here's your coding interview problem for today.

# This problem was asked by Uber.

# Given an array of integers, return a new array such that each element at index i of the new array is the product of all the numbers in the original array except the one at i.

# For example, if our input was [1, 2, 3, 4, 5], the expected output would be [120, 60, 40, 30, 24]. If our input was [3, 2, 1], the expected output would be [2, 3, 6].

# Follow-up: what if you can't use division?



# First attempt:
numbers = [1, 2, 3, 4, 5]
products = []

for i in range(len(numbers)):
    start = 1
    for j in range(len(numbers)):
        if i != j:
            start = start * numbers[j]
    products.append(start)

print(numbers)
print(products)

# Really simple solution actually... uses the same method I used in connect four to create the grid and check for a win
# It wasn't until after I solved it that I realized I not only didn't use division, but I also don't know what I'd use division for... checking? Who knows!

def GetProducts(numberList):
    products = []
    for i in range(len(numberList)):
        product = 1
        for j in range(len(numberList)):
            if i != j:
                product = product * numberList[j]
        products.append(product)
    print(products) # Not good practice in a function. Unless your testing!
    return products

#now lets put this baby to test...

import random

def GetArray():
    numbers = []
    for i in range(5):
        randomInt = random.randint(1, 15)
        numbers.append(randomInt)
    print(numbers)
    return numbers

GetProducts(GetArray())
GetProducts(GetArray())
GetProducts(GetArray())
GetProducts(GetArray())
GetProducts(GetArray())

