String filename = "data.txt"

p = /^(\w+) (\d+)$/ 
class Rule { 
	String dir; int n;
}

List<Rule> rules = []

// START: Load the data.
int linesLoaded = 0
(filename as File).eachLine { line ->
	if( line ) { 
		def m = line =~ p
		if( m.matches() ) {
			def rule = new Rule( dir:m[0][1], n:Integer.parseInt(m[0][2]) )
			rules << rule
			linesLoaded++
		}
	}
}
println "---- loaded ----"

// tests...
assert linesLoaded == 1000
assert rules.size() == 1000

x = 0
aim = 0
d = 0
rules.each { r -> 
	switch( r.dir ) {
		case 'up': aim -= r.n; break
		case 'down': aim += r.n; break
		case 'forward': {
			x += r.n
			d += r.n * aim
		}
	}
}

println( "part 2: x=$x, aim=$aim, depth=$d, x*depth=${x*d}" )
