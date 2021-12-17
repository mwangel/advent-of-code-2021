// Advent of Code 2021, day 6, Martin Wangel
def realdata = [1,2,5,1,1,4,1,5,5,5,3,4,1,2,2,5,3,5,1,3,4,1,5,2,5,1,4,1,2,2,1,5,1,1,1,2,4,3,4,2,2,4,5,4,1,2,3,5,3,4,1,1,2,2,1,3,3,2,3,2,1,2,2,3,1,1,2,5,1,2,1,1,3,1,1,5,5,4,1,1,5,1,4,3,5,1,3,3,1,1,5,2,1,2,4,4,5,5,4,4,5,4,3,5,5,1,3,5,2,4,1,1,2,2,2,4,1,2,1,5,1,3,1,1,1,2,1,2,2,1,3,3,5,3,4,2,1,5,2,1,4,1,1,5,1,1,5,4,4,1,4,2,3,5,2,5,5,2,2,4,4,1,1,1,4,4,1,3,5,4,2,5,5,4,4,2,2,3,2,1,3,4,1,5,1,4,5,2,4,5,1,3,4,1,4,3,3,1,1,3,2,1,5,5,3,1,1,2,4,5,3,1,1,1,2,5,2,4,5,1,3,2,4,5,5,1,2,3,4,4,1,4,1,1,3,3,5,1,2,5,1,2,5,4,1,1,3,2,1,1,1,3,5,1,3,2,4,3,5,4,1,1,5,3,4,2,3,1,1,4,2,1,2,2,1,1,4,3,1,1,3,5,2,1,3,2,1,1,1,2,1,1,5,1,1,2,5,1,1,4]
def testdata = [3,4,3,1,2]

// From a list to a map.
def dataMassage( List data ) {
	Map<Long,Long> result = [:]
	9.times { n -> 
		result[n] = data.count { it == n }
	}
	return result
}

def report( long days, Map counts ) {
	println "After ${days.toString().padLeft(3,' ')} days ${counts.values().sum().toString().padLeft(13,'.')} fish: ${counts}"
}

def counts = dataMassage( realdata )
def DAYS = 256
report( 0, counts )

DAYS.times { d ->
	9.times { age -> counts[age-1] = (Long) counts[age] }
	counts[ 8 ] = counts[ -1 ] // The number of new fish = how many zeros we had.
	counts[ 6 ] += counts[ -1 ] // Fish that were at zero flip to 6.
	counts.remove( -1 ) // So we don't have to skip key -1 when counting the total.
	
	if( d < 18 ) report( d+1, counts )
}
report( DAYS, counts )
