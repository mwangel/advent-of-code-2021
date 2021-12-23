// Player 1 starting position: 8
// Player 2 starting position: 6

def die = (1..100)
long rollCount = 0

def getRoll( long round, IntRange die ) {
	long max = die.size()
	long index = ((round * 3) % max)
	long a = die[index]
	long b = die[(index+1) % max]
	long c = die[(index+2) % max]
	long result = a + b + c
	//println "Roll round $round, a:$a b:$b c:$c -> $result"
	result
}

int boardSize = 10

def pos1 = 8
def pos2 = 6
def score1 = 0
def score2 = 0

@groovy.transform.ToString class Player { long pos, score }

// POSITIONS 1-10 ARE KEPT AS 0-9 SO THERE WILL BE SOME +1 IN THE CODE!
def players = [new Player(pos:7), new Player(pos:5)]
println players

int whosup = 0
long round = 0
while( true ) {
	def roll = getRoll( round, die )
	rollCount += 3
	players[whosup].pos = (players[whosup].pos + roll) % boardSize
	players[whosup].score += (players[whosup].pos + 1)

	println( "player $whosup rolls $roll : ${players[whosup]}" )
	if( players[whosup].score >= 1000 ) break

	round++
	whosup = 1 - whosup
}

println "In round ${round+1}: Rolls=$rollCount, $players"
println( rollCount * players[0].score )
println( rollCount * players[1].score )
// 739785 too high