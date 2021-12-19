class Pair {
	def parent, left, right

	static int middleComma( String s ) {
		int brackets = 0
		for( int i=0; i < s.size(); i++ ) {
			def c = s[i]
			if( c == '[' ) brackets++
			else if( c == ']' ) brackets--
			else if( (c == ',') && (brackets == 1) ) return i
		}
		throw new Exception("Found a bug in middleComma method for input $s")
	}

	static def parse( s ) {
		if( s.isInteger() ) return Integer.parseInt(s)
		int i = middleComma(s)
		def L = s[1..(i-1)]
		def R = s[(i+1)..-2]
		return Pair.of( parse(L), parse(R) )
	}

	static Pair of( l, r ) { new Pair(left:l,right:r) }

	void setLeft( Object newLeft ) {
		if( newLeft instanceof Pair ) {
			newLeft.parent = this
			left = newLeft
		}
		else left = newLeft
	}

	void setRight( Object newRight ) {
		if( newRight instanceof Pair ) {
			newRight.parent = this
			right = newRight
		}
		else right = newRight
	}

	/** Side effect: This operation changes the pairs by setting their parent. */
	Pair plus( Pair other ) {
        return new Pair( left:this, right:other )
    }

    String toString() {
    	"[$left,$right]"
    }

    int getDepth() {
    	if( parent == null ) return 0
    	else return 1 + parent.getDepth()
    }

    Pair findAncestorWhereWeAreTheRightLeg() {
    	if( parent == null ) {
    		//println "findAncestorWhereWeAreTheRightLeg from root returns NULL"
    		return null // root
    	}
    	if( parent.right.is(this) ) {
	    	//println "findAncestorWhereWeAreTheRightLeg in $this returns $parent"
	    	return parent
    	}
		//println "findAncestorWhereWeAreTheRightLeg keep looking in parent of $this "
		return parent.findAncestorWhereWeAreTheRightLeg()
    }

    def findFirstRightSideNumber() {
    	//println "findFirstRightSideNumber in $this"
    	if( right instanceof Pair ) {
    		return right.findFirstRightSideNumber()
    	}
    	return this
    }



    Pair findAncestorWhereWeAreTheLeftLeg() {
    	if( parent == null ) {
    		//println "findAncestorWhereWeAreTheLeftLeg from root returns NULL"
    		return null // root
    	}
    	if( parent.left.is(this) ) {
	    	//println "findAncestorWhereWeAreTheLeftLeg in $this returns $parent"
	    	return parent
    	}
		//println "findAncestorWhereWeAreTheLeftLeg keep looking in parent of $this "
		return parent.findAncestorWhereWeAreTheLeftLeg()
    }

    def findFirstLeftSideNumber() {
    	//println "findFirstLeftSideNumber in $this"
    	if( left instanceof Pair ) {
    		return left.findFirstLeftSideNumber()
    	}
    	return this
    }

    def splitDivision( n ) {
    	int a, b
    	if( n % 2 == 0 ) { a=n/2; b=a }
    	else { a=(n-1)/2; b=(n+1)/2 }
    	return [a,b]
    }

    def splitLeft() {
    	if( left instanceof Pair ) throw new Exception("Trying to split a Pair is not supported, only numbers can be split. Left. $this")
    	def (a,b) = splitDivision( left )
    	setLeft( Pair.of( a, b ) )
    }

    def splitRight() {
    	if( right instanceof Pair ) throw new Exception("Trying to split a Pair is not supported, only numbers can be split. Right. $this")
    	def (a,b) = splitDivision( right )
    	setRight( Pair.of( a, b ) )
    }

    def explode() {
    	if( left instanceof Pair || right instanceof Pair )
    		throw new Exception("Only number,number pairs can be exploded.")

    	Pair firstLeft = findAncestorWhereWeAreTheRightLeg()
    	//println "explode 1: firstLeft = $firstLeft"
    	if( firstLeft ) {
    		if( firstLeft.left instanceof Pair ) {
    			def firstNodeLeftOfUsWithNumberRightNode = firstLeft.left.findFirstRightSideNumber()    			
    			if( firstNodeLeftOfUsWithNumberRightNode )
    				firstNodeLeftOfUsWithNumberRightNode.right += this.left
    		}
    		else
    			firstLeft.left += this.left
    	}

    	Pair firstRight = findAncestorWhereWeAreTheLeftLeg()
    	//println "explode 2: firstRight = $firstRight"
    	if( firstRight ) {
    		if( firstRight.right instanceof Pair ) {
    			def firstNodeRightOfUsWithNumberInLeftNode = firstRight.right.findFirstLeftSideNumber()    			
    			if( firstNodeRightOfUsWithNumberInLeftNode ) {
    				//println "explode 2: firstNodeRightOfUsWithNumberInLeftNode = $firstNodeRightOfUsWithNumberInLeftNode"
    				firstNodeRightOfUsWithNumberInLeftNode.left += this.right
    			}
    		}
    		else {
    			firstRight.right += this.right
    		}
    	}

    	if( parent.left.is(this) ) parent.left = 0
    	else if( parent.right.is(this) ) parent.right = 0
    	else throw new Exception("Martin too stupid.")
    }


    def findFirstNumNumPairAtDepth4OrMore() {
    	// depth-first
    	if( depth >= 4 ) {
    		if( !(left instanceof Pair) && !(right instanceof Pair) )
    			return this
    	}
    	
    	def leftResult = (left instanceof Pair) ? left.findFirstNumNumPairAtDepth4OrMore() : null
    	if( leftResult ) return leftResult
    	
    	def rightResult = (right instanceof Pair) ? right.findFirstNumNumPairAtDepth4OrMore() : null
    	if( rightResult ) return rightResult

    	return null
    }

    def findFirstPairWithANumberGreaterThanNine() {
    	// depth-first
    	Pair p
    	if( left instanceof Pair ) {
    		p = left.findFirstPairWithANumberGreaterThanNine()
    		if( p ) return p
    	}
    	else if( left > 9 ) return this

    	if( right instanceof Pair ) {
    		p = right.findFirstPairWithANumberGreaterThanNine()
    		if( p ) return p
    	}
    	else if( right > 9 ) return this

    	return null
    }


    long magnitude() {
    	long lm = 0
    	if( left instanceof Pair ) lm = 3 * left.magnitude()
    	else lm = 3 * left

    	long rm = 0
    	if( right instanceof Pair ) rm = 2 * right.magnitude()
    	else rm = 2 * right

    	return lm+rm
    }
}

def reduce( Pair p ) {
	//println "Reduce $p..."
	while( true ) {
		def toExplode = p.findFirstNumNumPairAtDepth4OrMore()
		if( toExplode ) {
			//println "to explode: $toExplode"
			toExplode.explode()
			continue
		}

		def toSplit = p.findFirstPairWithANumberGreaterThanNine()
		if( toSplit ) {
			//println "to split: $toSplit"
			if( !(toSplit.left instanceof Pair) && (toSplit.left > 9) ) {
				toSplit.splitLeft()
				continue
			}
			else if( !(toSplit.right instanceof Pair) && (toSplit.right > 9) ) {
				toSplit.splitRight()
				continue
			}
			else throw new Exception("There is a bug in the split finder.")
		}

		break
	}
	//println "..into $p"
	return p
}


// TESTS -----------------------------
assert Pair.middleComma('[1,2]') == 2
assert Pair.middleComma('[1,[2,3]]') == 2
assert Pair.middleComma('[[1,2],[3,4]]') == 6
assert Pair.middleComma('[[[1,2],3],4]') == 10

//println new Pair( left:1, right:2 )
//println new Pair( left:1, right:(new Pair( left:2, right:3 )) )
//def p1 = Pair.of(1,2)  // [1,2]
//def px = Pair.of(4,5)  // [4,5]
//def p2 = Pair.of(px,6) // [[4,5],6]
//def p3 = p1 + p2       // [[1,2],[[4,5],6]]
//println p2
//println p3
//println p3.depth       // 0
//println p2.depth       // 1
//println px.depth       // 2
//
//println "p3 before exploding [4,5]: $p3"
//px.explode()
//println "p3 after exploding [4,5] : $p3"
//
//// (explode examples)
//def p98 = Pair.of(9,8)
//def ex1 = Pair.of( Pair.of( Pair.of( Pair.of( p98, 1 ), 2 ), 3 ), 4 )
//println "ex1 before exploding [9,8]: $ex1"
//p98.explode()
//println "ex1 after exploding [9,8] : $ex1"
//
//def ex2p32 = Pair.of(3,2)
//def ex2 = Pair.of(7, Pair.of(6, Pair.of(5, Pair.of(4, ex2p32))) )
//println "ex2 before exploding [9,8]: $ex2"
//ex2p32.explode()
//println "ex2 after exploding [9,8] : $ex2"

//def x1l = Pair.of( Pair.of( Pair.of( 4, 3 ), 4 ), 4 )
//def x1r = Pair.of( 7, Pair.of( Pair.of( 8, 4 ), 9 ) )
//def x1 = Pair.of( x1l, x1r )
//def x2 = Pair.of( 1, 1 )
//def x3 = x1 + x2
//println x3
//
//reduce( x3 )

//println( reduce( Pair.of(1,1) + Pair.of(2,2) + Pair.of(3,3) + Pair.of(4,4) ) )

//println( reduce( Pair.of(1,1) + Pair.of(2,2) + Pair.of(3,3) + Pair.of(4,4) + Pair.of(5,5) ) )

//println( reduce( Pair.of(1,1) + Pair.of(2,2) + Pair.of(3,3) + Pair.of(4,4) + Pair.of(5,5) + Pair.of(6,6) ) )

assert Pair.parse('[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]').toString() == '[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]'
assert Pair.parse('[[1,2],[[3,4],5]]').magnitude() == 143
assert Pair.parse('[[[[0,7],4],[[7,8],[6,0]]],[8,1]]').magnitude() == 1384
assert Pair.parse('[[[[1,1],[2,2]],[3,3]],[4,4]]').magnitude() == 445
assert Pair.parse('[[[[3,0],[5,3]],[4,4]],[5,5]]').magnitude() == 791
assert Pair.parse('[[[[5,0],[7,4]],[5,5]],[6,6]]').magnitude() == 1137
assert Pair.parse('[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]').magnitude() == 3488

List<Pair> pairs = ("data.txt" as File).readLines().collect { s -> Pair.parse(s) }
println( "Read ${pairs.size()} numbers." )

def p = pairs[0]
for( int i = 1; i < pairs.size(); i++ ) {
	p = p + pairs[i]
	reduce( p )
}

println "part 1: magnitude=${p.magnitude()}, p=$p"

//======== part 2 ========

def lines = ("data.txt" as File).readLines()
println( "Read ${lines.size()} lines for part 2." )
long bestMagnitude = 0
for( int i = 0; i < lines.size()-1; i++ ) {
	for( int j = i+1; j < lines.size(); j++ ) {
		def a = Pair.parse( lines[i] )
		def b = Pair.parse( lines[j] )
		def c = reduce( a + b )
		def m1 = c.magnitude()
		if( m1 > bestMagnitude ) bestMagnitude = m1

		def d = Pair.parse( lines[i] )
		def e = Pair.parse( lines[j] )
		def f = reduce( e + d ) // the other way around
		def m2 = f.magnitude()
		if( m2 > bestMagnitude ) bestMagnitude = m2
	}
}
println "Best magnitude: $bestMagnitude"
