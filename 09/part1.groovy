def isLowPoint( x, y, data, w, h ) {
	def dbg = true
	def v = data[y][x]
	def others = []
	if(y>0) others << data[y-1][x]
	if(y<99) others << data[y+1][x]
	if(x>0) others << data[y][x-1]
	if(x<99) others << data[y][x+1]

	if( others.every { v < it } ) {
		if(dbg) println "$x;$y v:$v others:$others is low"
		return true
	}
	if(dbg) println "$x;$y v:$v others:$others is high"
	return false
}

// TEST:
def testData = """2199943210
3987894921
9856789892
8767896789
9899965678"""

println "========== tests complete ============"

// START: Load the data.
int a=100
int b=100
//a=5;b=10
int[][] data = new int[a][b]
int lineCount = 0
("data.txt" as File).eachLine { line ->
//testData.eachLine { line ->
	println "$lineCount: $line"
	if( line.trim() ) {
		line.trim().eachWithIndex { c, i -> 
			int v = Integer.parseInt(c) 
			print "$v "
			data[lineCount][i] = v
		}
		++lineCount
	}
	println ""
}

def w = data[0].size()
def h = data.size()

def lowCount = 0
def riskLevel = 0
h.times { y ->
	w.times { x ->
		if( isLowPoint( x, y, data, w, h ) ) {
			lowCount++
			riskLevel += data[y][x] + 1
		}
	}
}

println "--------------------------------"
println "data width * height: $w * $h"
println "part 1: lowCount:$lowCount risk:$riskLevel"
println "--------------------------------"

