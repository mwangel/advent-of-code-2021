class Point { 
    int x, y, v 
    
    int hashCode() { Objects.hash(x,y) }
    boolean equals( Object o ) { o && o.class == Point && o.x==x && o.y==y }
    String toString() { "{$x,$y:$v/$costFromStart/${visited?'V':''}}" }
    
    Point parent = null
    int costFromStart = 0
    boolean visited = false
}


// START: Load the data.
def fileText = ("data.txt" as File).readLines()
int height = fileText.size()
int width = fileText.first().size()
Point[][] data = new Point[height][width]

fileText.eachWithIndex { line, y ->
    if( line.trim() ) {
        line.trim().eachWithIndex { c, x -> 
            int v = Integer.parseInt(c) 
            data[y][x] = new Point( x:x, y:y, v:v )
        }
    }
}
//println data

List<Point> neighbours( Point p, data ) {
    int w = data[0].size()
    int h = data.size()
    int x = p.x
    int y = p.y
    List<Point> result = []
    if( x > 0 )   result << data[y][x-1]
    if( x < w-1 ) result << data[y][x+1]
    if( y > 0 )   result << data[y-1][x]
    if( y < h-1 ) result << data[y+1][x]
    result
}


def takeBestStep( Point[][] data, int tox, int toy, Set<Point> active ) {
    Point here = active.min { it.costFromStart }     
    List<Point> nearbyPoints = neighbours( here, data ).findAll { !it.visited && !active.contains(it) }
    
    //println "Best is $here. Neighbours: $nearbyPoints"
    here.visited = true
    active.remove( here )

    Point end = nearbyPoints.find { it.x==tox && it.y==toy }
    if( end ) {
        println "=================="
        int pathCost = end.v
        Point p = here
        while( p.parent ) {
            pathCost += p.v
            println p
            p = p.parent
        }
        throw new Exception( "$here + $end for $pathCost risk" )
    }

    int totalRiskFromHere = 99999999
    for( Point n : nearbyPoints ) {
        if( n.visited ) continue
        n.costFromStart = here.costFromStart + n.v
        n.parent = here
        active << n
    }

    return active
}


def show( Point[][] data, int width, int height, Set<Point> activePoints ) {    
    height.times { y -> 
        def sb = new StringBuilder( width * 2 )
        width.times { x ->
            Point p = data[y][x]
            if( activePoints.contains(p) ) sb.append('#')
            else if( p.visited ) sb.append('*')
            else sb.append('.')
        }
        println sb
    }
    println ""
}


Set<Point> activePoints = []
Point startPoint = data[0][0]
activePoints << startPoint

1000000.times{ i ->
    takeBestStep( data, width-1, height-1, activePoints )
    //println "Active($i): $activePoints\n"
    //show( data, width, height, activePoints )
}

