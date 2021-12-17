var map:List<BooleanArray> = mutableListOf()

// START: Load the data.
java.io.File("data.txt").forEachLine { line:String ->
	if( line.length > 0 ) { 
		//var row = BooleanArray( line.length ) { i:Int -> (line.getAt(i) == '#' ? 1 : 0)  }   // TRUE for TREES!
		val row = line.map { it == '#' }
		map.add( row )
	}
}

/** Check map[row][column] (column is mod:ed by rowLength). */
fun treeAt( map:List<BooleanArray>, row:Int, column:Int ) : Boolean {
	val rowLength = map[row].length	
	return map[row][column % rowLength ]
}

/** Count trees found when stepping through the map this way. */
fun ct( map:List<BooleanArray>, ystep:Int, xstep:Int ) : Long { 
	var column:Int = 0
	var trees:Long = 0
	for( y in 0..map.length step ystep ) { 
		trees += (treeAt( map, y, column ) ? 1 : 0)
		column += xstep
	}
	return trees
}

// tests
assert( map.length == 323 )
val testmap = mutableListOf( booleanArrayOf(true,false,true) ) //,[false,false,true]
assert( treeAt( testmap, 0, 2 ) )
assert( treeAt( testmap, 0, 3 ) ) // 3 should rotate back around to 0
assert( treeAt( testmap, 0, 0 ) )
assert( !treeAt( testmap, 0, 1 ) )
assert( !treeAt( testmap, 1, 0 ) )
assert( !treeAt( testmap, 1, 1 ) )

// start
println( "Part 1: ${ct(map,1,3)} trees found." )
println( "Part 2: ${ct(map,1,1) * ct(map,1,3) * ct(map,1,5) * ct(map,1,7) * ct(map,2,1)}" )
