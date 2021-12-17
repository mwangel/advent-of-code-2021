class Rule { 
	String pattern
	Character c
	Character first, second
	private String A, B

	String toString() { "$pattern/${getA()}:${getB()}" }
	
	void setPattern( String p ) {
		pattern = p
		first = p[0] as Character
		second = p[1] as Character
	}

	String getA() { if( !A ) A = "$first$c"; A }
	String getB() { if( !B ) B = "$c$second"; B }
}

// Reading the data...
LinkedList<Character> list = [] 
Set<Rule> rules = [] as HashSet<Rule>
def lines = ("data.txt" as File).readLines()

def s = lines[0].trim()
def pairCounts = new HashMap<String,Long>().withDefault { 0 }
for( int i=0; i<s.size()-1; i++ ) {
	pairCounts[ s.substring(i, i+2) ]++
}
def firstChar = s[0]
def lastChar = s[-1]
println firstChar
println lastChar

for( int i=2; i < lines.size(); i++ ) { 
	def line = lines[i]
	def (p,c) = line.tokenize(' -> ')
	rules << new Rule( pattern:p, c:c )
}
println pairCounts
println rules
println "--------------------------------"

Map applyRules( Map<String,Long> pairCounts, Set<Rule> rules ) {
	boolean dbg = !true
	def result = new HashMap<String,Long>().withDefault { 0L }
	pairCounts.each { k, v ->
		if( dbg ) println " $k/$v in $rules"
		def r = rules.find { it.pattern == k }
		if( r ) {
			if( dbg ) println " $r"
			result[ r.A ] += v
			result[ r.B ] += v
			if( dbg ) println result
		}
		else {
			if( dbg ) println " $k not found"
			result[k] = v
		}
	}
	return result
}

def Map<Character,Long> countChars( Map<String,Long> map, firstChar, lastChar ) {
	def counts = new HashMap<Character,Long>().withDefault { 0L }
	map.each { k, v -> 
		counts[ k[0] ] += v
		counts[ k[1] ] += v
	}
	counts[ firstChar ] -= 1
	counts[ lastChar ] -= 1
	counts.each { k, v -> counts[k] = v/2 }
	counts[ firstChar ] += 1
	counts[ lastChar ] += 1
	return counts
}

40.times { 
	println "apply ${it+1} of 40\n"
	pairCounts = applyRules( pairCounts, rules ) 
}
println "Pair counts: $pairCounts\n"
def freq = countChars( pairCounts, firstChar, lastChar )
long maxFreq = freq.max { it.value }.value
long minFreq = freq.min { it.value }.value
println "Character counts: $freq\n"
println( "Answer: $maxFreq - $minFreq = ${maxFreq - minFreq}\n" )
