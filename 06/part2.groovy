// Advent of Code 2020, day 6, Martin Wangel

def realdata = [1,2,5,1,1,4,1,5,5,5,3,4,1,2,2,5,3,5,1,3,4,1,5,2,5,1,4,1,2,2,1,5,1,1,1,2,4,3,4,2,2,4,5,4,1,2,3,5,3,4,1,1,2,2,1,3,3,2,3,2,1,2,2,3,1,1,2,5,1,2,1,1,3,1,1,5,5,4,1,1,5,1,4,3,5,1,3,3,1,1,5,2,1,2,4,4,5,5,4,4,5,4,3,5,5,1,3,5,2,4,1,1,2,2,2,4,1,2,1,5,1,3,1,1,1,2,1,2,2,1,3,3,5,3,4,2,1,5,2,1,4,1,1,5,1,1,5,4,4,1,4,2,3,5,2,5,5,2,2,4,4,1,1,1,4,4,1,3,5,4,2,5,5,4,4,2,2,3,2,1,3,4,1,5,1,4,5,2,4,5,1,3,4,1,4,3,3,1,1,3,2,1,5,5,3,1,1,2,4,5,3,1,1,1,2,5,2,4,5,1,3,2,4,5,5,1,2,3,4,4,1,4,1,1,3,3,5,1,2,5,1,2,5,4,1,1,3,2,1,1,1,3,5,1,3,2,4,3,5,4,1,1,5,3,4,2,3,1,1,4,2,1,2,2,1,1,4,3,1,1,3,5,2,1,3,2,1,1,1,2,1,1,5,1,1,2,5,1,1,4]
def testdata = [3,4,3,1,2]

// From a list to a map.
def dataMassage( List data ) {
	Map<Long,Long> result = [:]
	data.each { n -> 
		result[n] = data.count { it == n }
	}
	return result
}


def data
data = testdata
data = realdata

def counts = dataMassage( data )
def DAYS = 256

DAYS.times { d ->
	Long newFish = 0
	Map<Long, Long> newData = [:]

	counts.each { age, number -> 
		//print "$age:$number -> "
		age = age - 1
		if( age < 0 ) {
			age = 6
			newFish += number
		}
		//println "$age:$number + $newFish"
		if( newData.containsKey(age) )
			newData[age] += number
		else 
			newData[age] = number
	}
	
	if( newFish > 0 ) newData[8] = newFish
	counts = newData.clone()
	
	if( d < 20 )
		println "After ${(d+1).toString().padLeft(3,' ')} days ${counts.values().sum().toString().padLeft(13,'.')} fish: ${counts}"
}
println "...\nAfter ${DAYS.toString().padLeft(3,' ')} days ${counts.values().sum().toString().padLeft(13,' ')} fish: ${counts}"
