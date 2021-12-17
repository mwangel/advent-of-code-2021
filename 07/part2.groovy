// Load the data.
def data = ("data.txt" as File).text.split(',').collect { s -> Long.parseLong(s) }
def range = (data.min()..data.max())

// This pre-emptive calculation is not really necessary but it makes the program run a LOT faster.
// The sum of all numbers 1 to d == (1..d).sum() == d*(d+1)/2
def precomputedCosts = range.collectEntries { d -> [d, d*(d+1)/2] } 
precomputedCosts[0L] = 0L // could be required for your data

// Total cost of moving each position in data to position x.
def totalCost( data, x, precomputedCosts ) {
	data.sum { d -> precomputedCosts[ Math.abs( d - x ) ] }
}

// Solution, short, hard to read:
// For each position between outermost crabs, find cost of moving all crabs to that position, then pick least.
// (Output is in the form "position=cost". My answer is "473=92676646")
println( range.collectEntries { x -> [x, totalCost(data, x, precomputedCosts )] }.min { it.value } )

// // Solution, the most straight-forward way:
// def costs = [:]
// range.each { x ->
// 	def c = totalCost( data, x, precomputedCosts )
// 	costs[x] = c
// }
// println "Result: ${costs.min { it.value }}"
// println "--------------------------------"
