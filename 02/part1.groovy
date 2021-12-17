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

def x = rules.findAll { r -> r.dir == "forward" }.sum { r -> r.n }
def downs = rules.findAll { r -> r.dir == "down" }.sum { r -> r.n } 
def ups = rules.findAll { r -> r.dir == "up" }.sum { r -> r.n } 

println( "part 1: x=$x, downs=$downs, ups=$ups, depth=${downs-ups}, x*depth=${x*(downs-ups)}" )

//println( "part 1: " + rules.count { it.isGood() } )
//println( "part 2: " + rules.count { it.isGood2() } )
