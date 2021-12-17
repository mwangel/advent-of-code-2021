def filedata = """acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
abcefg cf acdeg acdfg bcdf abdfg abdefg acf abcdefg abcdfg | cf cf cf cf
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"""

class Measurement {
	List<String> left = []
	List<String> right = []
	
	Measurement( String fileLine ) {
		def parts = fileLine.split(' \\| ')
		left = parts[0].split().collect { s -> 
			s.getChars().toList().sort().join()
		}
		right = parts[1].split().collect { s -> 
			s.getChars().toList().sort().join()
		}
	}

	String toString() { "$left|$right" }
}

// 1 4 7 8
def part1( List<Measurement> data ) { 
	data.sum { m -> m.right.count { r -> r.size() in [2,3,4,7] } }
}

def part2( List<Measurement> data ) {
	def x = data.sum { findNumbers(it) }
	println "\n\n+++++++++++++++++\n\n"
	println x
}

def pickup( List<String> list, int size ) {
	String s = list.find { it.size() == size }
	println "$list - $size - $s"
	list.remove( s )
	return s
}

def strdiff( String a, String b ) { 
	if( !b || !a ) throw new Exception("$b - $a is undefined")
	if( b.size() > a.size() ) return strdiff(b,a)
	def result = (a.getChars().toList() - b.getChars().toList()).join() 
	//println "$a - $b = $result"
	return result
}
def stradd( String a, String b ) { 
	def result = "$a$b".getChars().toList().sort().join() 
	//println "$a + $b = $result"
	return result
}

def findNumbers( Measurement m ) {
	def result = [:] as Map<Long,String>
	def list = m.left.clone()
	
	// Translage wire to segment.
	//
	// if wires bf are used to show 1 
	// and bdf are used for 7 
	// then wire d goes to the top segment 
	// (but b and f could both be either of the two segments on the right)
	//
	// so d must be in the strings for 0,2,3,5,6,7,8,9
	def mapping = [:] as Map<String,String>

	// These are known..
	result[1] = pickup( list, 2 )
	result[4] = pickup( list, 4 )
	result[7] = pickup( list, 3 )
	result[8] = pickup( list, 7 )
	def fives = list.findAll { it.size() == 5 }
	def sixes = list.findAll { it.size() == 6 }

	fives.remove( result[2] )

	//..so some wires can be mapped:
	def top = strdiff( result[7], result[1] )
	mapping[top] = 'a'	

	def three = fives.find { strdiff(it, result[7]).size() == 2 }
	result[3] = three
	fives.remove( three )
	def nine = sixes.find { strdiff(it, three).size() == 1 }
	sixes.remove( nine )
	result[9] = nine
	def downleft = strdiff( result[8], nine )
	mapping[downleft] = 'e'

	def six = sixes.find { strdiff(it,downleft) in fives }
	result[6] = six
	list.remove( six )
	sixes.remove( six )

	assert sixes.size() == 1
	def zero = sixes[0]	
	result[0] = zero
	list.remove( zero )
	sixes.remove( zero )
	
	def f = strdiff(six,downleft)
	//println "f $f, downleft $downleft, six $six, fives $fives, sixes $sixes"
	def five = fives.find { it == f }
	result[5] = five
	list.remove( five )
	fives.remove( five )
	//println "five $five, downleft $downleft, six $six, three $three, nine $nine, result $result"

	assert fives.size() == 1
	def two = fives[0]
	result[2] = two

	def upright = strdiff(nine,five)
	mapping[ upright ] = 'c'

	def bottom = strdiff(nine, stradd(result[4], top) )
	mapping[ bottom ] = 'g'
	
	def center = strdiff( result[8], zero )
	mapping[ center ] = 'd'

	def upleft = strdiff( nine, three )
	mapping[ upleft ] = 'b'

	def bottomright = strdiff( stradd(three,downleft), two )
	mapping[bottomright] = 'f'


	def num = number(m.right, mapping)

	println '******************'
	println mapping
	println( m.left )
	println( "${m.right}, ${num}" )
	println '******************'


	return num
}

def number( List<String> wires, Map<String,String> wire2segment ) {
	println "wire2segment: $wire2segment"
	def segments2number = ['abcefg':0, 'cf':1, 'acdeg':2, 'acdfg':3, 'bcdf':4, 'abdfg':5, 'abdefg':6, 'acf':7, 'abcdefg':8, 'abcdfg':9]
	def result = ''
	wires.each { ws -> 
		println " $ws // $segments2number"
		
		def sortedWs = '' //ws.getChars().toList().sort().collect { wire2segment[it] }.join()
		ws.getChars().toList().sort().each { c ->
			def d = wire2segment[c.toString()]
			println "  c $c, wire2segment[c] $d"
			sortedWs += d
		}
		sortedWs = sortedWs.getChars().toList().sort().join()

		def num = segments2number[sortedWs]
		def snum = num.toString()
		println " $ws -> $sortedWs = $snum ($num)"
		result += snum
	}
	return Long.parseLong(result)
}


// TEST:

println "======================"

// START: Load the data.
List<Measurement> measurements = []
("data.txt" as File).eachLine { line ->
//filedata.split('\n').each { line ->
	if( line.trim() ) {
		measurements << new Measurement( line )
	}
}
println part1( measurements )
part2( measurements )


println "--------------------------------"
println "--------------------------------"
