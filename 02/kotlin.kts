// RUN LIKE THIS: kotlinc -script kotlin.kts
import java.io.File

var ok1:Int=0
var ok2:Int=0
var regex = "^(\\d+)\\-(\\d+) (\\w): (\\w+)\$".toRegex()

fun Int.betweenInclusive( x:Int, y:Int ) : Boolean = ( this >= x && this <= y )

fun evaluate( line : String ) : Unit {
	//assert( regex.matches(line) )
	val match = regex.find( line )!!
	val (sa,sb,sc,spwd) = match.destructured
	val a = sa.toInt()
	val b = sb.toInt()
	val c = sc.get(0)

	val n = spwd.count { x -> x == c }
	if( n.betweenInclusive(a,b) ) ok1++

	if( (spwd.get(a-1)==c) xor (spwd.get(b-1)==c) ) ok2++
}

File("data.txt").forEachLine { s:String -> evaluate( s ) }
println("svar 1 = $ok1")
println("svar 2 = $ok2")
