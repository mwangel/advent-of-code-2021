fun key(row:Int, col:Int) { "$row;$col" }

fun unKey( key: String ) {
	key.split(';').collect { s -> Integer.parseInt(s) }
}


fun isInTheRoom( input:Map<String,Int>, width:Int, height:Int, row:Int, col:Int ) {
	return !( row < 0 || row >= height || col < 0 || col >= width )
}

fun increaseSurroundingValues( input:Map, width:Int, height:Int, row:Int, col:Int ) {
	if( isInTheRoom( input, width, height, row-1, col-1 )) input[ key(row-1,col-1) ]++ 
	if( isInTheRoom( input, width, height, row-1, col ))   input[ key(row-1,col) ]++ 
	if( isInTheRoom( input, width, height, row-1, col+1 )) input[ key(row-1,col+1) ]++ 
	if( isInTheRoom( input, width, height, row, col-1 ))   input[ key(row,col-1) ]++ 
	if( isInTheRoom( input, width, height, row, col+1 ))   input[ key(row,col+1) ]++ 
	if( isInTheRoom( input, width, height, row+1, col-1 )) input[ key(row+1,col-1) ]++ 
	if( isInTheRoom( input, width, height, row+1, col ))   input[ key(row+1,col) ]++ 
	if( isInTheRoom( input, width, height, row+1, col+1 )) input[ key(row+1,col+1) ]++  
}

fun show( input:Map, width:Int, height:Int ) {
	for( row:Int in 0..height-1 ) {
		var sb = new StringBuilder( width * 2 )
		for( x:Int in x..width-1 ) {
			var k = key( row, x )
			var c = input[ k ]
			sb.append( c )
		}
		println( sb.toString() )
	}
	println("")
}

fun play( input:Map, width:Int, height:Int ) {
	var flashes:long = 0
	var flashedAlready:Set<String> = []

	// increase by 1
	input.each { k, v -> input[k] = v + 1 }

	// flashing..	
	while( true ) {
		var aboveNines = input.findAll { k, v -> v > 9 && !flashedAlready.contains(k) }
		
		if( aboveNines.size() == 0 )
			break

		aboveNines.each { k, v -> 
			var (row, x) = unKey( k )
			increaseSurroundingValues( input, width, height, row, x )
			flashedAlready.add(k)
			flashes++
		}
	}

	flashedAlready.each { k -> 
		input[k] = 0
	}

	if( flashedAlready.size() == input.size() ) {
		println("\n==========================")			
		println("== EVERYBODY FLASH NOW! ==")
		println("==========================")
		throw new Exception("Smurf!")
	}

	//show( input, width, height )
	return flashes
}

// TEST:
var testData:List = ['11111','19991','19191','19991','11111']
testData = ['5483143223','2745854711','5264556173','6141336146','6357385478','4167524645','2176841721','6882881134','4846848554','5283751526']
val realData:List = ['2478668324','4283474125','1663463374','1738271323','4285744861','3551311515','8574335438','7843525826','1366237577','3554687226']

var theMap:Map<String,Int> = [:]
realData.eachWithIndex { chars, row -> 
	chars.eachWithIndex { c, col -> 
		theMap[key(row,col)] = Integer.parseInt(c) } }

println(theMap)

var step = 0
while(true) { 
	print("step ${++step}:")
	play( theMap, testData[0].size(), testData.size() ) 
	println(" .. not synchronized")
}
