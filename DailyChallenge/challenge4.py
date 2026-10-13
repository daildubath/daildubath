# There exists a staircase with N steps, and you can climb up either 1 or 2 at a time. Given N, write a function that returns the number of unique ways you can climb the staircase. The order of the steps matters. 

# For Example, if N is 4, then there are 5 unique ways:
# 1, 1, 1, 1
# 2, 1, 1
# 1, 2, 1
# 1, 1, 2
# 2, 2

# What if instead of being able to climb 1 or 2 steps at a time, you could climb any number from a set of positive integers X? For example, if X = {1,3,5}, you could climb 1, 3, or 5 steps at a time.


# I have a hard time wrapping my head around this one, which I guess is the point. Not only that, I'm dealing with a bit of jet lag. I'm going to try solving a few on my own to see what patterns I notice.

# -1 = 1 total 1
# 2 = 11 or 2 total 2
# 3 = 111 12 21 total 3
# 4 = 1111 211 122 121 22 total 5
# 5 == 11111 2111 1211 1121 1112 221 212 122... ok. I'm getting the drift. that is 8. If I'm noticing a pattern, the next should have 13 (8 + 5)
# 6 == 111111 21111 12111 11211 11121 11112 2211 2121 2112 1221 1212 1122 222 That's all, right? that's 13, as expected. So the pattern is it should be the previous two combined,
# or the fibonacci sequence. It will always be the combo of the last two. In testing, I might be able to use this as a check. Next is 21, 34, and so on. 

# Now the question I've got is, "How do I get a function to do this?" I can cycle through them myself like I did above, but I don't know the best way to have a function do that simply.

# I'm going to try some other math equations to play with this, and also try to include three

# 1 - 1 t 1
# 2 - 1 2 t 2
# 3 - 1 21 12 3 t 4
# 4 - 1111 211 112 22 121 31 13 t 7
# I won't do more, but I'm assuming it is the previous three combined. If I take out 2, what do I get?
# 1 - 1 t 1
# 2 - 11 t 1
# 3 - 111 3 t 2
# 4 - 1111 31 13 t 3
# 5 - 11111 311 131 113 t 4
# next should be 4 plus 2 if the pattern continues?
# 6 - 111111 3111 1311 1131 1113 33 t 6
# cracked the pattern. Again, though, this doesn't help create the function, just test it.

# I'm thinking I may be able to weasle an easy way to do this.


def get_options(steps):
    options = []
    if steps == 0:
        return options
    elif steps == 1:
        return ['1']
    elif steps == 2:
        return ['11', '2']
    else:
        options.append("".join('1' for i in range(steps)))
        def rotate(option):
            for i in range(len(option) - 1):
                option = option[-1] + option
                option = option[:-1]
                if option not in options:
                    options.insert(0, option)
            def mix(option):
                if (option[-2] == '2' and option[-1] == '2') and (option[1] == '1' and option[0] == '1'):
                    option = option[:-1] + '12'
                    option = option[1:]
                    print(option)
                    rotate(option)
            mix(option)
        def add2(option):
            if option[0] == '1' and option[1] == '1':
                options.insert(0, '2' + option[2:])
                rotate(option)
        while options[0][1] == '1':
            add2(options[0])
            rotate(options[0])
        return options

for i in range(9):
    print(get_options(i))