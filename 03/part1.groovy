def map = []

// START: Load the data.
("data" as File).eachLine { line ->
	if( line ) { 
		boolean[] row = line.collect { it == '1' } // TRUE for TREES!
		map << row
	}
}

// tests
assert map.size() == 1000
def testmap = [[true,false,true],[false,false,true],[false,false,true]]
assert getCommonBitAtPosition( testmap, 0 ) == 0
assert getCommonBitAtPosition( testmap, 1 ) == 0
assert getCommonBitAtPosition( testmap, 2 ) == 1
assert countAtPosition( testmap, 0 ) == 1
assert countAtPosition( testmap, 1 ) == 0
assert countAtPosition( testmap, 2 ) == 3


def getCommonBitAtPosition( List<List> map, int position ) {
	def ones = 0
	def zeros = 0
	map.each { bits -> 
		if( bits[position] ) ++ones 
		else ++zeros
	}

	println "$position -> 0:$zeros, 1:$ones"
	if( ones == zeros ) { println "lika i position $position"}
	return ones > zeros ? 1 : 0
}

def countAtPosition( List<List> map, int position ) {
	def ones = 0
	map.each { bits -> 
		if( bits[position] ) ++ones 
	}
	return ones
}

// start
def n = map[0].size()
def result = []
def gamma = 0
def epsilon = 0
n.times { pos -> 	
	def b = getCommonBitAtPosition( map, pos )
	result[pos] = b
}
def c = result.reverse()
println result
println c
c.eachWithIndex { x, i -> 
	epsilon += x * Math.pow(2,i) 
	gamma += (x ? 0 : 1) * Math.pow(2,i) 
}

println "Part 1: ${result}, g = $gamma, e = $epsilon, g*e = ${gamma*epsilon}."
//println "Part 2: ${ct(map,1,1) * ct(map,1,3) * ct(map,1,5) * ct(map,1,7) * ct(map,2,1)}"
