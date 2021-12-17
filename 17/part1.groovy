// real data
def puzzleInput = 'target area: x=153..199, y=-114..-75'
def minx=153
def maxx=199
def maxy=-75
def miny=-114

// example
//minx=20
//maxx=30
//maxy=-5
//miny=-10


def step( long x, long y, long vx, long vy ) {
	x += vx
	y += vy

	if( vx > 0) vx--  // drag
	else if( vx < 0) vx++
	
	vy-- // gravity

	return [x, y, vx, vy]
}

def globalBestY = -99999999
def totalHitCount = 0
def allHits = [] as Set<String>

// vx at least 16 is required to reach minx. Max vx is maxx+1 (200)
def minvx = 5
def maxvx = maxx+1

def minvy = -200
def maxvy = 2500

def logmiss = false
println "Starting. vx:[${minvx}..${maxvx}], vy:[${minvy}..${maxvy}]"

(minvx..maxvx).each { startvx ->
	println "--- try vx = $startvx ---"
	(minvy..maxvy).each { startvy ->
		def x=0
		def y=0 
		def vx=startvx
		def vy=startvy
		def besty = -99999999
		def hit = false
		//println "--- try vx:$vx, vy:$vy ---"

		while( true ) {
			//println " $x, $y  speed: $vx, $vy"
			(x,y,vx,vy) = step( x, y, vx, vy )

			if( y > besty ) besty = y
			
			if( x >= minx && x <= maxx && y >= miny && y <= maxy ) {
				println " + Hit. Best Y $besty, (x $x, y $y), vx $startvx vy $startvy"
				allHits.add( "($startvx,$startvy)" )
				hit = true
				totalHitCount++
				break
			}

			if( vx > 0 && x > maxx ) {
				if(logmiss) println " - passed it x+ (x > $maxx and heading right)"
				break
			}
			else if( vx < 0 && x < minx ) {
				if(logmiss) println " - passed it x- (we are left of target and heading left)"
				break
			}
			else if( vx == 0 ) {
				if( x < minx || x > maxx ) {
					if(logmiss) println " - missed it, x < $minx and x speed is 0"
					break
				}

				if( vy < 0 && y < miny) {
					if(logmiss) println " - passed y- (we are at $y and falling below $miny)"
					break
				}
			}
		}

		if( hit && (besty > globalBestY) ) globalBestY = besty
	}
}

//println allHits

println "global best y: $globalBestY" // 6441
println "total hits: $totalHitCount"
println "number of different start speeds: ${allHits.size()}" // 1353 is too low
