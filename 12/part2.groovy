def rotateWaypoint90Right( long shipx, long shipy, long wpX, long wpY ) {
	if( shipx==wpX && shipy==wpY ) {
		println "** rotation around self at ($shipx, $shipy)"
		return [wpX, wpY]
	}

	if( shipy==wpY ) {
		if( wpX > shipx ) return [shipx, shipy + (wpX-shipx)]
		else return [shipx, shipy - (shipx-wpX)]
	}
	if( shipx==wpX ) {
		if( wpY > shipy ) return [shipx - (wpY-shipy), shipy]
		else return [shipx + (shipy-wpY), shipy]
	}


	// from first quadrant
	if( wpX > shipx && wpY < shipy ) {
		def x = shipx + (shipy-wpY)
		def y = shipy + (wpX-shipx)
		return [x,y]
	}
	if( wpX > shipx && wpY > shipy ) {
		def x = shipx - (wpY-shipy)
		def y = shipy + (wpX-shipx)
		return [x,y]
	}
	if( wpX < shipx && wpY > shipy ) {
		def x = shipx - (wpY-shipy)
		def y = shipy - (shipx-wpX)
		return [x,y]
	}
	if( wpX < shipx && wpY < shipy ) {
		def x = shipx + (shipy-wpY)
		def y = shipy - (shipx-wpX)
		return [x,y]
	}
	throw new Exception("coder too stupid to rotate: $shipx, $shipy, $wpX, $wpY, (90)")
}


def rotateWaypoint( long shipx, long shipy, long wpX, long wpY, String instruction ) {
	def times = 0
	switch( instruction ) {
		case 'R90' :times=1; break
		case 'R180':times=2; break
		case 'R270':times=3; break
		case 'L90' :times=3; break
		case 'L180':times=2; break
		case 'L270':times=1; break
		default: throw new Exception("you silly boy")
	}
	times.times { (wpX,wpY) = rotateWaypoint90Right( shipx,shipy, wpX,wpY ) }
	return [wpX, wpY]
}

// TEST:
assert rotateWaypoint90Right( 0,0, 1,-1 ) == [1,1]

assert rotateWaypoint90Right( 3,4, 5,4 ) == [3,6] // right, same y
assert rotateWaypoint90Right( 3,4, 1,4 ) == [3,2] // left, same y

assert rotateWaypoint90Right( 3,4, 3,2 ) == [5,4] // above, same x
assert rotateWaypoint90Right( 3,4, 3,6 ) == [1,4] // below, same x

assert rotateWaypoint90Right( 3,4, 5,3 ) == [4,6]
assert rotateWaypoint90Right( 3,4, 4,6 ) == [1,5]
assert rotateWaypoint90Right( 3,4, 1,5 ) == [2,2]
assert rotateWaypoint90Right( 3,4, 2,2 ) == [5,3]
assert rotateWaypoint( 3,4, 5,3, 'R90' ) == [4,6]
assert rotateWaypoint( 3,4, 5,3, 'L270' ) == [4,6]
assert rotateWaypoint( 3,4, 5,3, 'R180' ) == [1,5]
assert rotateWaypoint( 3,4, 5,3, 'L180' ) == [1,5]
assert rotateWaypoint( 3,4, 5,3, 'R270' ) == [2,2]
assert rotateWaypoint( 3,4, 5,3, 'L90' ) == [2,2]
println "========== tests complete ============"

def go( String instruction, long shipx, long shipy, long wpX, long wpY ) {
	def command = instruction[0]
	print "${instruction.padRight(4)} @ ($shipx, $shipy) x ($wpX, $wpY) -> "
	def number  = Long.parseLong( instruction.substring(1) )

	def dx = wpX - shipx
	def dy = wpY - shipy

	switch( command ) {
		case 'N': wpY -= number; break
		case 'S': wpY += number; break
		case 'W': wpX -= number; break
		case 'E': wpX += number; break

		case 'R': 
		case 'L': 
			(wpX,wpY) = rotateWaypoint(shipx, shipy, wpX, wpY, instruction)
			break

		case 'F': 
			shipx += dx * number
			shipy += dy * number
			wpX = shipx + dx
			wpY = shipy + dy
			break

		default: throw new Exception("not again!")
	}

	println "($shipx, $shipy) x ($wpX, $wpY)"
	return [shipx, shipy, wpX, wpY]
}


// START: Load the data.
def data = [] as List<Long>
("data.txt" as File).eachLine { line ->
	if( line.trim() ) {
		data << line
	}
}
assert data.size() == 790

println "--------------------------------"
println "data size: ${data.size()}"
println "--------------------------------"
def testData = ['F10','N3','F7','R90','F11']

// part 1: answer = 41682220
long x = 0
long y = 0
long waypointx = 10
long waypointy = -1
//testData.each { s -> (x,y,waypointx,waypointy) = go( s, x, y, waypointx, waypointy ) }
data.each { s -> (x,y,waypointx,waypointy) = go( s, x, y, waypointx, waypointy ) }

println "-----------------"
println "at ($x, $y) with waypoint ($waypointx, $waypointy). Distance = ${Math.abs(x) + Math.abs(y)}"
