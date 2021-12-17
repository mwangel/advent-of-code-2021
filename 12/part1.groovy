def realData = [ 'bm-XY', 'ol-JS', 'bm-im', 'RD-ol', 'bm-QI', 'JS-ja', 'im-gq', 'end-im', 'ja-ol', 'JS-gq', 'bm-AF', 'RD-start', 'RD-ja', 'start-ol', 'cj-bm', 'start-JS', 'AF-ol', 'end-QI', 'QI-gq', 'ja-gq', 'end-AF', 'im-QI', 'bm-gq', 'ja-QI', 'gq-RD' ]
def testData = [ 'start-A', 'start-b', 'A-c', 'A-b', 'b-d', 'A-end', 'b-end' ]
def usedData = testData
usedData = realData

class Cave { 
	String name
	Set<Cave> neighbours = []
	boolean isBig = false
	boolean isStart = false
	boolean isEnd = false

	int hashCode() { name.hashCode() }
	boolean equals( Object o ) { o && o.name.equals(name) }	
	String toString() { "$name" }

	void setName( s ) { 
		name = s
		isBig = (s.toUpperCase() == s)
		isStart = (s == 'start')
		isEnd = (s == 'end')
	}

	boolean isSmall() { return !isBig }
}

// START: Load the data.
def map = [] as Set<Cave>
usedData.each { s -> 
	def (a,b) = s.split('-')
	Cave caveA = map.find { it.name == a } ?: new Cave( name:a )
	Cave caveB = map.find { it.name == b } ?: new Cave( name:b )
	caveA.neighbours << caveB
	caveB.neighbours << caveA
	map << caveA
	map << caveB
}

def findPaths( Set<Cave> caves, Cave c, Set<Cave> visited = [], List<Cave> path = [] ) {
	//println "Enter '${c.name}'..."
	long pathCounter = 0
	path << c
	if( c.small ) visited << c

	if( c.isEnd ) {
		pathCounter++
		println "Found a road: $path"
	}
	else {
		for( Cave n : c.neighbours ) {
			if( n.small & visited.contains(n) ) {
				//println "skip ${n.name}"
				continue
			}
			pathCounter += findPaths( caves, n, visited, path )
		}
	}

	// back out...
	//println "Leave ${c.name}"
	visited.remove( c )
	path.removeLast()
	return pathCounter
}

def part2( Set<Cave> caves, Cave c, Map<Cave,Long> visits = [:].withDefault{0}, List<Cave> path = [], Cave usedSmallCave = null ) {
	//println "Enter '${c.name}'..."
	long pathCounter = 0

	if( c.small ) {		
		def nv = visits[c]
		if( nv == 0 ) { 
			// this is fine, we have not been in this small cave before
		}
		else {
			def mv = visits.findAll { k, v -> v == 2 }
			if( mv.size() == 0 ) { 
				// this is fine, no small caves have been visited 2 times
			}
			else {
				if( mv.size() > 1 ) 
					throw new Exception("too many multiple-visited small caves")
				return 0
			}
		}
	}

	path << c
	if( c.small ) {
		visits[c] += 1
	} 

	if( c.isEnd ) {
		pathCounter++
		//println "$path"
	}
	else {
		for( Cave n : c.neighbours ) {
			if( n.isStart ) continue // stops multiple visits to start
			pathCounter += part2( caves, n, visits, path, usedSmallCave )
		}
	}

	// back out...
	//println "Leave ${c.name}"
	visits[c] -= 1
	path.removeLast()
	usedSmallCave = null
	return pathCounter
}


// TEST:
Cave start = map.find { it.isStart }
Cave end = map.find { it.isEnd }

println "--------------------------------"
println "caves: ${map}"
println "part 1: ${findPaths( map, start )}" // 3887
println "++++++++++++++++++++++++++++++++"
println "part 2: ${part2( map, start )}" // 104834
println "--------------------------------"

