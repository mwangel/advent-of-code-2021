#!/usr/bin/python3
with open('data') as f:
    lines = f.readlines()

n = 0
for i in range(len(lines)-1):
	if int(lines[i]) < int(lines[i+1]):
		n += 1

print('python3, day 1, part 1 :', n)
