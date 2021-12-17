class Rule { 
	String pattern
	Character c
	Character first, second

	String toString() { "$pattern --> $c" }
	
	void setPattern( String p ) {
		pattern = p
		first = p[0] as Character
		second = p[1] as Character
	}
}

LinkedList<Character> list = [] 
Set<Rule> rules = [] as HashSet<Rule>
def lines = ("data.txt" as File).readLines()
list = lines[0].trim().getChars().collect { new Character(it) }
for( int i=2; i < lines.size(); i++ ) { 
	def line = lines[i]
	def (p,c) = line.tokenize(' -> ')
	rules << new Rule( pattern:p, c:c )
}
println list
println rules
println "--------------------------------"

void applyRules( LinkedList<Character> list, Set<Rule> rules ) {
	int startSize = list.size()
	for( int i = startSize - 2; i >= 0; i-- ) {
		Character a = list[i]
		Character b = list[i+1]
		Rule r = rules.find { it.first == a && it.second == b }
		//println "found pattern ${r.pattern} on index $i"
		if( r ) { 
			list.add( i+1, r.c ) 
			//println " -=> $list"
		}
	}
}

def Map<Character,Long> count( List<Character> list ) {
	def counts = new HashMap<Character,Long>().withDefault { 0 }
	list.each { c -> counts[c]++ }
	return counts
}

10.times { 
	println "apply ${it+1} of 10"
	applyRules( list, rules ) 
}
println " -=> ${list.join()}"
def freq = count( list )
def maxFreq = freq.max { it.value }
def minFreq = freq.min { it.value }
println( maxFreq.value - minFreq.value )
