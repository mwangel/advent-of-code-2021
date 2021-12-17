void pickup( String s, int i, List<Character> stack ) {
    def legalPairs = ['(':')', '[':']', '{':'}', '<':'>']

    if( i >= s.size() ) {
        def missing = stack.reverse().collect { legalPairs[it] }.join()
        throw new Missing( missing )
    }

    def c = s[i] 
    def e = stack.size() ? legalPairs[ stack.last() ] : 'Any starter'
    
    if( c in ['(','[','{','<'] ) 
        stack << c
    else if( c == e )
        stack.removeLast()
    else
        throw new Corruption( "Problem on index $i in line $s. Expected $e, found $c.", c )

    pickup( s, i+1, stack )
}


def solve( List<String> data ) {  
    List<Long> scores = []
    def part1total = 0
    data.eachWithIndex { s, i -> 
        try {
            pickup( s, 0, [] )
        }
        catch( Corruption c ) {
            println " - ${i.toString().padLeft(3,' ')} - Corruption              in $s"
            part1total += c.score
        }
        catch( Missing m ) {
            println " + ${i.toString().padLeft(3,' ')} + Missing ${m.message.padRight(15)} in $s"
            scores << m.score
        }
    }

    def sorted = scores.sort()
    def middle = Math.floor( sorted.size() / 2 )

    return [ part1total, sorted[ middle ] ]
}


class Corruption extends Exception {
    def map = [')':3, ']':57, '}':1197, '>':25137]
    long score
    Corruption( String s, String c ) { 
        super(s) 
        score = map[ c ]
    }
}

class Missing extends Exception {
    def map = [')':1, ']':2, '}':3, '>':4]
    long score = 0    
    Missing( String s ) { 
        super(s) 
        s.each { score = score * 5 + map[ it ] }
    }
}


def realInput = ("data.txt" as File).readLines()
def (a,b) = solve( realInput )
println "part 1 = $a, part 2 = $b"  // 411471, 3122628974 for Martin
