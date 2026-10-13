# Good morning! Here's your coding interview problem for today.

# This problem was asked by Jane Street.

# cons(a, b) constructs a pair, and car(pair) and cdr(pair) returns the first and last element of that pair. For example, car(cons(3, 4)) returns 3, and cdr(cons(3, 4)) returns 4.

# Given this implementation of cons:

# def cons(a, b):
#     def pair(f):
#         return f(a, b)
#     return pair
# Implement car and cdr.


## SOLUTION ## 

def construct(first, last):
    def pair(function):
        return function(first, last)
    return pair

def first(pair): # this is 'car'
    def get(first, second):
        f"goodbye {second}"  # I just don't want the argument above to be shaded, I guess
        return first
    return pair(get)

def second(pair): # this is 'cdr'
    def get(first, second):
        f"goodbye {first}"
        return second
    return pair(get)


# testing time!

print("first:", first(construct(3, 4)))
print("Second:", second(construct(3, 4)))

# Double testing time! 

import random

errors = 0

for i in range(100):
    p1 = random.randint(-1000, 1000)
    p2 = random.randint(-1000, 1000)
    try:
        assert p1 == first(construct(p1, p2))
        assert p2 == second(construct(p1, p2))
    except AssertionError:
        errors += 1

if not errors:
    print("There were no errors")
else:
    print(f"There were {errors} errors")

# The end!

#  Work through:

# # I've been switching between C# and Python like crazy, and wow, do I mix up naming conventions. However, I think its fair to say 'cons' is not a great name for construct, and car and cdr aren't either.
# # Actually, I'm not even sure what the a, r, or d stand for.

# def construct(first, last):
#     def pair(f):
#         return f(first, last)
#     return pair

# #first, I'm going to see what this returns. I'm not positive on what it's doing.

# print("return:", construct(1, 2))

# # <function construct.<locals>.pair at 0x75b47281dd00> might need to google this one...
# # Ok, it wasn't that compicated. You pass in a funtion, and it calls that function with the values given to construct. That's kinda cool

# pair = construct(1, 2)

# def add(number1, number2):
#     return number1 + number2

# print("Add 1 and 2:", pair(add))

# # I haven't run it yet, but I'm hoping for three, otherwise I've got a lot of guess work to do

# # Yep, 3. Ok, good. That makes my life a little easier. Also, I just solved the problem.