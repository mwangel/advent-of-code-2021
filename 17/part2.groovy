@groovy.transform.Field long minx = 0
@groovy.transform.Field long maxx = 0
@groovy.transform.Field long miny = 0
@groovy.transform.Field long maxy = 0
@groovy.transform.Field long minz = 0
@groovy.transform.Field long maxz = 0
@groovy.transform.Field long minw = 0
@groovy.transform.Field long maxw = 0

String key( long x, long y, long z, long w ) { "$x;$y;$z;$w" }

void set( long x, long y, long z, long w, boolean value, Map world ) { 
	if( x < minx ) minx = x
	if( x > maxx ) maxx = x
	if( y < miny ) miny = y
	if( y > maxy ) maxy = y
	if( z < minz ) minz = z
	if( z > maxz ) maxz = z
	if( w < minw ) minw = w
	if( w > maxw ) maxw = w
	//if( value ) println "Set x:$x; y:$y; z:$z; w:$w"
	world[ key(x,y,z,w) ] = value 
}

boolean get( long x, long y, long z, long w, Map world ) { world[key(x,y,z,w)] }

def countNeighbours( long x, long y, long z, long w, Map world ) {
	int active = 0
	int inactive = 0
	for( long a=x-1; a <= x+1; a++ ) {
		for( long b=y-1; b <= y+1; b++ ) {
			for( long c=z-1; c <= z+1; c++ ) {
				for( long d=w-1; d <= w+1; d++ ) {
					if( a==x && b==y && c==z && d==w ) continue
					if( get(a,b,c,d, world) ) active++
					else inactive++
				}
			}
		}
	}
	return [ inactive, active ]
}

def countWorld( Map world ) {
	int active = 0
	int inactive = 0
	for( long a=minx-1; a <= maxx+1; a++ ) {
		for( long b=miny-1; b <= maxy+1; b++ ) {
			for( long c=minz-1; c <= maxz+1; c++ ) {
				for( long d=minw-1; d <= maxw+1; d++ ) {
					if( get(a,b,c,d, world) ) active++
					else inactive++
				}
			}
		}
	}
	return [ inactive, active ]
}


Map run( Map oldworld ) {
	Map<String,Boolean> newworld = [:].withDefault{false}
	int activeNeighbours
	int inactiveNeighbours
	for( long w=minw-1; w <= maxw+1; w++ ) {
		for( long z=minz-1; z <= maxz+1; z++ ) {
			for( long x=minx-1; x <= maxx+1; x++ ) {
				for( long y=miny-1; y <= maxy+1; y++ ) {
					def state = get( x,y,z,w, oldworld )
					(inactiveNeighbours,activeNeighbours) = countNeighbours( x,y,z,w, oldworld )
					//println "On x:$x; y:$y; z:$z; w:$w - state:$state - neighbours:[$inactiveNeighbours,$activeNeighbours]"

					if( state && (activeNeighbours==2 || activeNeighbours==3) ) { set(x,y,z,w, true, newworld) }
					else if( !state && activeNeighbours==3 ) { set(x,y,z,w, true, newworld) }
				}
			}
		}
	}
	return newworld
}

def input = ['#.##....','.#.#.##.','###.....','....##.#','#....###','.#.#.#..','.##...##','#..#.###']
//def input = ['.#.','..#','###'] // test data
Map<String,Boolean> world = [:].withDefault{false}
input.eachWithIndex { linedata, y -> 
	linedata.eachWithIndex { c, x -> 
		set( x,y,0,0, c=='#', world )
	}
}

println "\n-----------------"
6.times { n ->
	world = run( world )
	println "After ${n+1} cycles the world [in]active count is ${countWorld(world)}"
}
