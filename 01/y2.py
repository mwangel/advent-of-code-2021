#!/usr/bin/python3

with open('data') as f: lines = f.readlines()

data = list( map( lambda s: int(s), lines ) )
result = 0
for n in range( len(data)-3 ):
	a = data[n] + data[n+1] + data[n+2]
	b = data[n+1] + data[n+2] + data[n+3]
	if a < b: result += 1

print(result) # 1471
