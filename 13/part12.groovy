class Dot {
	long x, y
	int hashCode() { Objects.hash(x,y) }
	boolean equals( Object o ) { o && o.x.equals(x) && o.y.equals(y) }	
	String toString() { "$x,$y" }
}

class Fold {
	String xy
	long n
	String toString() { "Fold where $xy = $n" }	
}

// START: Load the data.
def dots = [] as Set<Dot>
def folds = [] as List<Fold>
//def fileData = ('testdata.txt' as File).readLines()
def fileData = ('data.txt' as File).readLines()
fileData.each { s -> 
	if( s.startsWith('fold') ) {
		s = s - 'fold along '
		def t = s.split('=')
		folds.add( new Fold( xy:t[0], n:Long.parseLong(t[1]) ) )
	}
	else if( s.trim() ) {
		def (a,b) = s.split(',')
		def d = new Dot( x:Long.parseLong(a), y:Long.parseLong(b) )
		dots.add( d )
	}
}

def maxxy( Set<Dot> dots ) { [dots.max { it.x }.x+1, dots.max { it.y }.y+1] }

def fold( Set<Dot> dots, Fold f ) {
	println f
	if( f.xy == 'y' ) {
		dots.each { d ->
			if( d.y > f.n ) {
				long u = d.y
				long v = d.y - f.n
				long w = f.n - v
				d.y = w
				//println "(${d.x};$u), $v, $w "
			}
		}
	}
	else if( f.xy == 'x' ) {
		dots.each { d ->
			if( d.x > f.n ) {
				long u = d.x
				long v = d.x - f.n
				long w = f.n - v
				d.x = w
				//println "($u;${d.y}), $v, $w "
			}
		}
	}
}

def show( Set<Dot> dots ) {
	def (w,h) = maxxy( dots )
	h.times { y ->
		w.times { x ->
			if( dots.any { d -> d.x == x && d.y == y } ) print "▒"
			else print " "
		}
		println ""
	}
	println ""
}

def count( Set<Dot> dots ) {
	Set<String> set = dots.collect { d -> d.toString() }
	set.size()
}

// PART 1:
println ';;;;;;;;;;;;;;;;;;;;;;;;'
folds.eachWithIndex { f, i -> 
	fold( dots, f ) 
	println( "After ${i+1} fold${i?'':'s'} there are ${count(dots)} dots." )
}

println "\nPart 2:"
show( dots )

/*
####...##..##..#..#...##..##...##..#..#
#.......#.#..#.#..#....#.#..#.#..#.#..#
###.....#.#..#.####....#.#....#..#.####
#.......#.####.#..#....#.#.##.####.#..#
#....#..#.#..#.#..#.#..#.#..#.#..#.#..#
#.....##..#..#.#..#..##...###.#..#.#..#
*/