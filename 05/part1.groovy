// Advent of Code 2020, day 5, Martin Wangel
p = /^(\d+),(\d+) \-\> (\d+),(\d+)$/ 
class Point { int x; int y }
class Line { 
	Line( x1, y1, x2, y2 ) { a=new Point(x:x1, y:y1); b=new Point(x:x2, y:y2) }
	Point a; Point b; 
	def getHorizontal() { a.y == b.y }
	def getVertical() { a.x == b.x }
	def getLeft() { Math.min(a.x, b.x) }
	def getRight() { Math.max(a.x, b.x) }
	def getTop() { Math.min(a.y, b.y) }
	def getBottom() { Math.max(a.y, b.y) }
	String toString() { "${a.x},${a.y} -> ${b.x},${b.y} (${horizontal ? 'horiz' : ''}${vertical ? 'vert' : ''})" }
}

List<Line> lines = []

def test = false
def asserts = false

// START: Load the data.
def filename = 'testdata.txt'
if( !test ) filename = 'data.txt'
int linesLoaded = 0
(filename as File).eachLine { str ->
	if( str ) { 
		def m = str =~ p
		if( m.matches() ) {
			def line = new Line( Integer.parseInt(m[0][1]), Integer.parseInt(m[0][2]), Integer.parseInt(m[0][3]), Integer.parseInt(m[0][4]) )
			if( line.horizontal || line.vertical ) lines << line
			linesLoaded++
		}
	}
}
println "---- loaded ${lines.size()} lines ----"
//println lines.collect { "${it.toString()}" }.join('\n')

def between( int v, int start, int end ) { 
	//println " between $v, $start, $end"
	if( start < end )
		return v >= start && v <= end 
	else
		return v >= end && v <= start
}

def intersect( Line first, Line second ) {
	println "intersect $first, $second"
	if( first.horizontal && second.horizontal ) return false
	if( first.vertical && second.vertical ) return false
	if( first.horizontal && second.vertical ) {
		return ( between( first.a.y, second.a.y, second.b.y ) && between( second.a.x, first.a.x, first.b.x ) )
			//return true			
	}
	return intersect( second, first )
}

def intersections( Line first, Line second ) {
	//println "intersections $first, $second"
	if( first.horizontal && second.horizontal ) {
		if( first.top != second.top ) {
			return 0;
		}
		def a = Math.max( first.left, second.left )
		def b = Math.min( first.right, second.right )
		if( a > b ) return 0

		(b-a+1).times { x ->
			hit( a+x, first.top )
		}
		return b-a+1
	}
	
	if( first.vertical && second.vertical ) {
		if( first.left != second.left ) return 0 // parallel vertical lines
		def a = Math.max( first.top, second.top )
		def b = Math.min( first.bottom, second.bottom )
		if( a > b ) return 0

		(b-a+1).times { y ->
			hit( first.left, a+y )
		}

		return b-a+1
	}

	if( first.horizontal && second.vertical ) {
		if( between( first.top, second.top, second.bottom ) && between( second.left, first.left, first.right ) ) {
			hit( second.left, first.top )
			return 1
		}
		else return 0
	}
	
	return intersections( second, first )
}


import groovy.transform.Field
@Field def hitpoints = [] as Set<String>
def hit( int x, int y ) { 
	println "Hit $x,$y"
	hitpoints.add("$x;$y") 
}

// TEST:
if( asserts ) {
	assert new Line(0,0, 0,10).vertical
	assert new Line(0,0, 0,10).horizontal == false
	assert new Line(0,0, 10,0).horizontal
	assert new Line(0,0, 10,0).vertical == false
	assert !intersect( new Line(0,9, 5,9), new Line(2,2, 2,1) )
	assert intersections( new Line(0,0, 0,2), new Line(0,3, 0,5) ) == 0
	assert intersections( new Line(0,0, 0,2), new Line(0,2, 0,5) ) == 1
	assert intersections( new Line(0,0, 0,2), new Line(0,0, 0,5) ) == 3
	assert intersections( new Line(0,1, 0,2), new Line(0,0, 0,5) ) == 2
	assert intersections( new Line(1,1, 7,1), new Line(2,2, 6,2) ) == 0
	assert intersections( new Line(1,1, 7,1), new Line(0,1, 1,1) ) == 1
	assert intersections( new Line(1,1, 7,1), new Line(0,0, 0,4) ) == 0
	assert intersections( new Line(0,1, 7,1), new Line(2,0, 2,10) ) == 1
	assert intersections( new Line(0,1, 8,1), new Line(2,0, 2,11) ) == 1
}
// START:
int n = 0

for( int i=0; i < lines.size()-1; i++ ) { 
	for( int j=i+1; j < lines.size(); j++ ) { 
		def c = intersections( lines[i], lines[j] ) 
		println "intersections ${lines[i]}, ${lines[j]} : $c"
		n += c
	}
}


println hitpoints
println n
println hitpoints.size()
