def rules = [
	'departure_location': [[49,920], [932,950]], // 
	'departure_station': [[28,106], [130,969]],
	'departure_platform': [[47,633], [646,950]],
	'departure_track': [[41,839], [851,967]],
	'departure_date': [[30,71], [88,966]],
	'departure_time': [[38,532], [549,953]],
	'arrival_location': [[38,326], [341,968]],
	'arrival_station': [[27,809], [834,960]],
	'arrival_platform': [[29,314], [322,949]],
	'arrival_track': [[26,358], [368,966]],
	'class': [[34,647], [667,951]],
	'duration': [[39,771], [785,958]],
	'price': [[43,275], [286,960]],
	'route': [[28,235], [260,949]],
	'row': [[48,373], [392,962]],
	'seat': [[35,147], [172,953]],
	'train': [[37,861], [885,961]],
	'type': [[38,473], [483,961]],
	'wagon': [[49,221], [228,973]],
	'zone': [[46,293], [307,967]],
]

myTicket = [101,179,193,103,53,89,181,139,137,97,61,71,197,59,67,173,199,211,191,131]

def matchesRule( List<List<Long>> ranges, Long value ) {
	def result = ranges.any { List<Long> minmax -> value >= minmax.first() && value <= minmax.last() }
}

assert !matchesRule( rules['zone'], 45 )
assert matchesRule( rules['zone'], 46 )
assert matchesRule( rules['zone'], 293 )
assert !matchesRule( rules['zone'], 294 )
assert matchesRule( rules['zone'], 307 )
assert matchesRule( rules['zone'], 967 )
assert !matchesRule( rules['zone'], 1000 )

List<List<Integer>> tickets = []
int linesRead = 0
// Read "nearby tickets:"
("data2.txt" as File).eachLine { line ->	
	if( line.trim() && !line.startsWith('-') ) {
		linesRead++
		def values = line.split(",").collect { Integer.parseInt(it.trim()) }
		assert values.size() == 20
		tickets << values
	}
}
assert linesRead == 190

def ruleNames = rules.keySet()
Map<Integer,List<String>> index2names = [:].withDefault{ [] }

for( Integer index = 0; index < 20; index++ )  {
	def valuesFromThisIndex = tickets.collect { List<Integer> values -> values[index] }
	boolean matched = false
	rules.each { name, ranges ->
		if( valuesFromThisIndex.every { v -> matchesRule(ranges,v) } ) {
			//println "Values on index $index match rule $name"
			matched = true
			index2names[index].add( name )
		}
	}
	if( !matched ) {
		println "\nValues on index $index matches no rule! Values: ${valuesFromThisIndex.sort()}"
	}
}

println index2names.collect { it }.join("\n")
println "----"

// Some rules work for many indexes. 
// Find rules that work for only one index, 
// move them to "realIndexes" 
// and then remove them from the lists of rules for all other indexes.
// Continue until all rules are mapped to exactly one index.
Map<String,Integer> realIndexes = [:]
while( realIndexes.size() < 20 ) {
	def singles = index2names.findAll { index, names -> names.size() == 1 }
	singles.each { index, names ->
		String n = names.first()
		realIndexes[ n ] = index
		index2names.each { i, list -> index2names[i] = list - n }
	}
}
println realIndexes.collect {k, v -> "$k is on index ${v+1}" }.sort().join("\n")
def answer = realIndexes.findAll{ k, v -> k.startsWith('departure') }.values().collect{ (Long) myTicket[it] }
println "Multiply these values for your answer: $answer"
println "This is the answer you lazy person: ${answer.inject(1L) { prod, item -> prod*item }}"