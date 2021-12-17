
// TEST:

// START: Load the data.

def lines = ("data" as File).readLines()
def plays = lines[0].split(',').collect{ Integer.parseInt(it) }

int i=2
List<Board> boards = []
while( i < lines.size() ) {
	def s = lines[i].trim()
	if( s ) {
		Board b = new Board( [s, lines[i+1], lines[i+2], lines[i+3], lines[i+4]] )
		boards.add( b )
		i += 5
	}
	i++
}

class Board {
	static int nextId = 1
	int id = Board.nextId++
	int[][] numbers = new int[5][5]
	int[][] hits = new int[5][5]	

	Board( List<String> input ) {
		input.eachWithIndex() { s, i ->
			def rowNumbers = s.trim().split().collect { str -> Integer.parseInt(str) }
			5.times { x -> numbers[i][x] = rowNumbers[x] }
			//println "$i: $rowNumbers, ${numbers[i]}"
		}
	}

	def mark( int n ) {
		5.times { y -> 
			5.times { x -> 
				if( numbers[y][x] == n ) {
					hits[y][x] += 1
					//println " Board $id has $n on row $x, column $y)"
				}
			}
		}
	}

	def rowCheck() {
		def result = 0
		5.times { y -> 
			//println " rowCheck for Board $id on row $y - ${hits[y]}"
			if( hits[y][0] && 
				hits[y][1] && 
				hits[y][2] && 
				hits[y][3] && 
				hits[y][4] ) 
			{
				++result
			}
		}
		result
	}

	def colCheck() {
		def result = 0
		5.times { x -> 
			if( hits[0][x] && 
				hits[1][x] && 
				hits[2][x] && 
				hits[3][x] && 
				hits[4][x] ) 
				++result
		}
		result
	}

	def anyRowOrColumn() { rowCheck() > 0 || colCheck() > 0 }

	def scorePartOne() {
		def sum = 0
		5.times { y -> 
			5.times { x -> 
				if( !hits[y][x] ) sum += numbers[y][x]
			}
		}
		return sum
	}
}

plays.each { p ->
	println "Playing number: $p"
	def winningBoardIds = []
	boards.each { b -> 
		b.mark( p )
		if( b.anyRowOrColumn() ) {
			def boardScore = b.scorePartOne()
			def finalScore = p * boardScore
			println "winner: ${b.id} with score $boardScore for a final score of $finalScore"
			winningBoardIds << b.id
		}
	}
	boards.removeIf { winningBoardIds.contains( it.id ) }
}
