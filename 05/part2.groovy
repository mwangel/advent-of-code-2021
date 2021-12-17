// Advent of Code 2021, day 5, Martin Wangel
import groovy.transform.Field
@Field def allpoints = [:] as Map<Point,Long>

p = /^(\d+),(\d+) \-\> (\d+),(\d+)$/ 
class Point { 
	int x; int y 
	int hash = 0

	Point( int x, int y ) { 
		this.x = x; 
		this.y = y; 
		this.hash = Objects.hash(x,y) 
	}

	boolean equals( Object o ) {
	    if( o.is(this) ) return true
	    if( !(o instanceof Point) ) return false

	    Point other = (Point) o
	    return this.x == other.x && this.y == other.y
	}

	int hashCode() {
	    hash
	}

	String toString() { "$x;$y" }
}

def addPoint( int x, int y, Map<Point,Long> allpoints ) {
	Point p = new Point( x, y )

	if( !allpoints.containsKey(p) )
		allpoints[p] = 0
	allpoints[p] += 1
}

/** Step through the coordinates on the line defined by the x1,y1 and x2,y2 endpoints. */
def addLine( x1, y1, x2, y2, Map<Point,Long> allpoints ) {
	def dx = 0
	if( x1 < x2 ) dx = 1
	else if( x1 > x2 ) dx = -1

	def dy = 0
	if( y1 < y2 ) dy = 1
	else if( y1 > y2 ) dy = -1

	def x = x1
	def y = y1
	while( (x != x2) || (y != y2) ) {
		addPoint( x, y, allpoints )
		x += dx
		y += dy
	}
	addPoint( x, y, allpoints )
}

// START: Load the data. Each coordinate on each line adds 1 to the values in "allpoints".
def filename = 'data.txt'
(filename as File).eachLine { str ->
	if( str ) { 
		def m = str =~ p
		if( m.matches() ) {
			addLine( 
				Integer.parseInt(m[0][1]), Integer.parseInt(m[0][2]), 
				Integer.parseInt(m[0][3]), Integer.parseInt(m[0][4]), 
				allpoints )
		}
	}
}
println "---- loaded ----"

// RUN: Just count the number of point in allpoints that have a value of more than one.
def pointsWithMoreThanOneLine = allpoints.findAll { k, v -> v > 1 }
//println "pointsWithMoreThanOneLine: $pointsWithMoreThanOneLine"
println "Answer to part 2: ${pointsWithMoreThanOneLine.size()}"
