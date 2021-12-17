def map = []

// START: Load the data.
("data" as File).eachLine { line ->
//("testdata.txt" as File).eachLine { line ->
	if( line ) { 
		List<Boolean> row = line.collect { it == '1' } // TRUE for TREES!
		map << row
	}
}

//map = map[1,2,3]

// tests
//assert map.size() == 1000
def testmap = [[true,false,true],[false,false,true],[false,false,true]]
assert getCommonBitAtPosition( testmap, 0,1 ) == 0
assert getCommonBitAtPosition( testmap, 1,1 ) == 0
assert getCommonBitAtPosition( testmap, 2,1 ) == 1
assert countAtPosition( testmap, 0 ) == 1
assert countAtPosition( testmap, 1 ) == 0
assert countAtPosition( testmap, 2 ) == 3
assert boolsToNum( [false, false, false, false] ) == 0
assert boolsToNum( [false, false, false, true] ) == 1
assert boolsToNum( [false, false, true, false] ) == 2
assert boolsToNum( [false, false, true, false] ) == 2
assert boolsToNum( [true, false, true, false] ) == 10

def getCommonBitAtPosition( List<List> map, int position, int equalValue ) {
	def ones = 0
	def zeros = 0
	map.each { bits -> 
		if( bits[position] ) ++ones 
		else ++zeros
	}

	//println "$position -> 0:$zeros, 1:$ones"
	if( ones == zeros ) { println "lika i position $position"; return equalValue }
	return ones > zeros ? 1 : 0
}

def countAtPosition( List<List> map, int position ) {
	def ones = 0
	map.each { bits -> 
		if( bits[position] ) ++ones 
	}
	return ones
}

def boolsToNum( List<Boolean> bools ) {
	long result = 0
	bools.reverse().eachWithIndex { b, i -> result += (b ? Math.pow(2,i) : 0) }
	return result
}

def makeMasks( List<List> map ) {
	def n = map[0].size()
	def mostCommon = [] // list of 0s and 1s
	def leastCommon = [] // dito
	n.times { pos -> 	
		def b = getCommonBitAtPosition( map, pos, 1 )
		mostCommon[pos] = b
		leastCommon[pos] = (b == 1 ? 0 : 1)
	}
	return [mostCommon, leastCommon]
}

def p(List<Boolean> bits) {
	println( bits.collect { it ? 1 : 0 } )
}
def q(List<Boolean> bits) {
	return bits.collect { it ? 1 : 0 } 
}

def findByMask( List<List> map, int maskIndex ) {
	def mask = makeMasks(map)[maskIndex]
	println "find by mask\n   $mask"
	map.eachWithIndex { l,i -> println "$i: ${q(l)}" }

	def o = []
	map.each { bits -> 
		o << new ArrayList(bits)
	}
	def n = map[0].size()
	for( int pos=0; pos < n; pos++ ) {
		Boolean toFind = mask[pos] == 1
		println "== find $toFind at $pos"
		o = o.findAll { bits -> bits[pos] == toFind }
		o.each { p it }
		println "${pos.toString().padLeft(2,' ')}, ${mask[pos]} o:$o"
		if( o.size() == 1 ) break
		mask = makeMasks(o)[maskIndex]
	}
	return boolsToNum(o[0])
}

def (mostCommon, leastCommon) = makeMasks(map)
println "--------------------------"
def A = findByMask( map, 0 )
def B = findByMask( map, 1 )
println( A )
println( B )
println "--------------------------"
println mostCommon
println leastCommon
println "--------------------------"
println( A * B ) // INTE 2974222
// o = 943
// 