def key(int row, int col) { "$row;$col" }

def unKey( String key ) {
	key.split(';').collect { s -> Integer.parseInt(s) }
}


def isInTheRoom( Map input, int width, int height, int row, int col ) {
	return !( row < 0 || row >= height || col < 0 || col >= width )
}

def increaseSurroundingValues( Map input, int width, int height, int row, int col ) {
	if( isInTheRoom( input, width, height, row-1, col-1 )) input[ key(row-1,col-1) ]++ 
	if( isInTheRoom( input, width, height, row-1, col ))   input[ key(row-1,col) ]++ 
	if( isInTheRoom( input, width, height, row-1, col+1 )) input[ key(row-1,col+1) ]++ 
	if( isInTheRoom( input, width, height, row, col-1 ))   input[ key(row,col-1) ]++ 
	if( isInTheRoom( input, width, height, row, col+1 ))   input[ key(row,col+1) ]++ 
	if( isInTheRoom( input, width, height, row+1, col-1 )) input[ key(row+1,col-1) ]++ 
	if( isInTheRoom( input, width, height, row+1, col ))   input[ key(row+1,col) ]++ 
	if( isInTheRoom( input, width, height, row+1, col+1 )) input[ key(row+1,col+1) ]++  
}

def show( Map input, int width, int height ) {
	for( int row=0; row < height; row++ ) {
		def sb = new StringBuilder( width * 2 )
		for( int x=0; x < width; x++ ) {
			def k = key( row, x )
			def c = input[ k ]
			sb.append( c )
		}
		println( sb.toString() )
	}
	println ""
}

def play( Map input, int width, int height ) {
	long FLASH = 9
	long flashes = 0
	Set<String> flashedAlready = []

	// increase by 1
	input.each { k, v -> input[k] = v + 1 }

	// flashing..	
	while( true ) {
		def aboveNines = input.findAll { k, v -> v > 9 && !flashedAlready.contains(k) }
		
		if( aboveNines.size() == 0 )
			break

		aboveNines.each { k, v -> 
			def (row, x) = unKey( k )
			increaseSurroundingValues( input, width, height, row, x )
			flashedAlready << k
			flashes++
		}
	}

	flashedAlready.each { k -> 
		input[k] = 0
	}

	if( flashedAlready.size() == input.size() ) {
		println "\n=========================="
		println "== EVERYBODY FLASH NOW! =="
		println "=========================="
		throw new Exception("Smurf!")
	}

	//show( input, width, height )
	return flashes
}

// TEST:
List testData = ['11111','19991','19191','19991','11111']
testData = ['5483143223','2745854711','5264556173','6141336146','6357385478','4167524645','2176841721','6882881134','4846848554','5283751526']
List realData = ['2478668324','4283474125','1663463374','1738271323','4285744861','3551311515','8574335438','7843525826','1366237577','3554687226']

Map<String,Integer> theMap = [:]
realData.eachWithIndex { chars, row -> chars.eachWithIndex { c, col -> theMap[key(row,col)] = Integer.parseInt(c) } }
println(theMap)

def step = 0
while(true) { 
	print "step ${++step}:"
	play( theMap, testData[0].size(), testData.size() ) 
	println " .. not synchronized"
}
println "Number of flashes: $total"
