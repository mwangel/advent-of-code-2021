#!/usr/bin/python3
lines = []
with open('data.txt') as f:
    lines = f.readlines()

x = 0
d = 0
for s in lines:
	[dir,n] = s.split()
	if dir == 'forward': x = x + int(n)
	elif dir == 'up'   : d = d - int(n)
	elif dir == 'down' : d = d + int(n)
	else               : raise Error('apa')

print(x*d)
