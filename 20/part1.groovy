// def lines = ('testdata.txt' as File).readLines()
def lines = ('data.txt' as File).readLines()
def mask = lines[0]

boolean[][] img = makeImg( lines.size()-2, lines[2].size() )
assert count( img ) == 0

for( int y = 2; y < lines.size(); y++ ) { 
	def s = lines[y]
	s.eachWithIndex { c, x ->
		img[y-2][x] = (c == '#')
	}
}
assert mask.size() == 512
// assert img.size() == 100
// assert img[0].size() == 100
// println getIndex(img,99,99,false)
// println getIndex(img,99,99,true)
// println getIndex(img,100,100,true)

// println getIndex(img,-2,-2)
// println getIndex(img,0,0)
// println getIndex(img,-2,2)
// println getIndex(img,-1,2)
// println getIndex(img,0,2)
// println getIndex(img,2,2)
// println getIndex(img,3,3)
// println getIndex(img,4,4)
// println getIndex(img,5,5)

boolean shouldFlipOutsideValue = ((mask[0]=='#') && (mask[511]=='.'))
boolean outsideValue = false
println "outsideValue = $outsideValue, ${shouldFlipOutsideValue ? 'Flipping' : 'Constant'}"

println img[0]
println "Before any transform: ${count( img )}"

final int N = 50
N.times { int n ->
	img = transform( img, mask, outsideValue )
	//printImg(img)
	println "After ${n+1} transform : ${count( img )}"
	if( shouldFlipOutsideValue ) outsideValue = !outsideValue
}

// Answer to part 1: 5301
// Answer to part 2: 19492


boolean[][] transform( boolean[][] img, String mask, boolean outsideValue ) {
	int width = img[0].size()
	int height = img.size()
	def newImg = makeImg( height + 2, width + 2 )
	for( int y=0; y < height+2; y++ ) {
		for( int x=0; x < width+2; x++ ) {
			def index = getIndex( img, x-1, y-1, outsideValue )
			def newValue = getNewValue( mask, index )
			newImg[y][x] = newValue
			//println "At x:${x-1}, y:${y-1} - index=$index & newValue=${newValue?1:0}"
		}		
	}
	newImg
}

boolean getNewValue( String mask, int index ) { return mask[index]=='#' }

def printImg( boolean[][] i ) {
	if( !i ) throw new Exception("Print empty map? Right... $i")
	int width = i[0].size()
	int height = i.size()
	for( int y=0; y < height; y++ ) {
		println i[y].collect { it ? '#' : '.' }.join()
	}	
	println ""
}

def count( boolean[][] img ) {
	int width = img[0].size()
	int height = img.size()
	long n = 0
	for( int y=0; y < height; y++ ) {
		for( int x=0; x < width; x++ ) {
			if( img[y][x] ) n++
		}
	}
	return n
}

def makeImg( int height, int width ) {
	println "New image. Height:$height * Width:$width"
	boolean[][] newImg = new boolean[height][width]
	return newImg
}

int getIndex( boolean[][] img, int x, int y, boolean outsideValue = false ) {	
	def sb = new StringBuilder()
	int width = img[0].size()
	int height = img.size()
	
	for( yy = y-1; yy <= y + 1; yy++ ) {
		for( xx = x-1; xx <= x + 1; xx++ ) {
			def b = (xx<0 || yy<0 || xx>=width || yy >= height ) ? outsideValue : img[yy][xx]
			//println "getindex(yy $yy, xx $xx, outside $outsideValue) --> ${b?1:0}"
			sb.append( b ? '1' : '0' )
		}
	}
	//println "getIndex x:$x, y:$y -> $sb"
	return Integer.parseInt( sb.toString(), 2 )
}

