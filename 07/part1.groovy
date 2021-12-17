// TEST:

// START: Load the data.
int goods = 0
def data = ("data.txt" as File).text.split(',').collect { s -> Long.parseLong(s) }
println "average: ${data.average()}"
int smallest = data.min()
int largest = data.max()

def totalCost( data, x ) {
	data.sum { d -> Math.abs( d - x ) }
}

def costs = [:]
for( int x=smallest; x < largest; x++ ) {
	def c = totalCost( data, x )
	costs[x] = c
	//println "$x : $c"
}


println "--------------------------------"
println costs.min { it.value }
