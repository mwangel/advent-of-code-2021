class Point { 
    int x, y, v 
    int hashCode() { Objects.hash(x,y) }
    boolean equals( Object o ) { o && o.class == Point && o.x==x && o.y==y }
    String toString() { "{$x;$y @$v}" }
}

def d2s( x, y ) { "[$x;$y]" }
def d2p( x, y, v ) { new Point(x:x, y:y, v:v) }

def flowToNines( x, y, data, w, h, Set<Point> visitedPoints ) {
    def dbg = !true
    if( dbg ) println "visit $x/$y"
    if( x < 0 || x >= w ) return []
    if( y < 0 || y >= h ) return []    
    def v = data[y][x]
    if( v == 9 ) return []

    Point p = d2p(x,y,v)
    if( visitedPoints.contains(p) ) return []
    visitedPoints.add( p )

    visitedPoints.addAll( flowToNines(x-1,y,data,w,h,visitedPoints) )
    visitedPoints.addAll( flowToNines(x+1,y,data,w,h,visitedPoints) )
    visitedPoints.addAll( flowToNines(x,y-1,data,w,h,visitedPoints) )
    visitedPoints.addAll( flowToNines(x,y+1,data,w,h,visitedPoints) )

    return visitedPoints
}

// TEST:
def testData = """2199943210
3987894921
9856789892
8767896789
9899965678"""

assert new Point(x:1, y:2, v:3) == new Point(x:1, y:2, v:3)
assert new Point(x:1, y:2, v:3) != new Point(x:7, y:2, v:3)
assert new Point(x:1, y:2, v:3) != new Point(x:1, y:7, v:3)
assert new Point(x:1, y:2, v:3) == new Point(x:1, y:2, v:7) // value does not count in equals()..
assert new Point(x:1, y:1, v:1).hashCode() == 993
assert new Point(x:1, y:1, v:2).hashCode() == 993 //...or in hash
assert new Point(x:1, y:2, v:3).hashCode() == 994
assert new Point(x:2, y:1, v:3).hashCode() == 1024
assert new Point(x:2, y:2, v:3).hashCode() == 1025
assert new Point(x:1, y:3, v:3).hashCode() == 995
println "========== tests complete ============"

// START: Load the data.
int a=100
int b=100
//a=5;b=10
int[][] data = new int[a][b]
int lineCount = 0
("data.txt" as File).eachLine { line ->
//testData.eachLine { line ->
    //println "$lineCount: $line"
    if( line.trim() ) {
        line.trim().eachWithIndex { c, i -> 
            int v = Integer.parseInt(c) 
            //print "$v "
            data[lineCount][i] = v
        }
        ++lineCount
    }
    println ""
}

def w = data[0].size()
def h = data.size()

def basins = [] as Set<Set<Point>>
h.times { y ->
    w.times { x ->
        def points = flowToNines( x, y, data, w, h, [] as Set<Point> )
        println "($x,$y) : $points"
        basins.add( points )
    }
}

println "--------------------------------"
println "data width * height: $w * $h"
println "part 2: "
println basins.collect{it}.sort{it.size()}.collect{ "${it.size()}: $it" }.join('\n')
// The 3 largest have sizes 99, 100 and 103. Multiplied it comes to 1019700.
println "--------------------------------"

