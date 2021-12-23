@groovy.transform.ToString 
class Box { 
	boolean on = false
	int x1,x2,y1,y2,z1,z2
}

List<Box> data = []
def text = ('data.txt' as File).readLines()
text.each { s ->	
	def box = new Box()
	box.on = s.startsWith('on ')
	t = ((s - 'on ') - 'off ').split(',')
	def (a,b) = ((t[0]-'x=').split('\\.\\.')).collect{ Integer.parseInt(it) }
	def (c,d) = ((t[1]-'y=').split('\\.\\.')).collect{ Integer.parseInt(it) }
	def (e,f) = ((t[2]-'z=').split('\\.\\.')).collect{ Integer.parseInt(it) }
	box.x1 = a; box.x2 = b
	box.y1 = c; box.y2 = d
	box.z1 = e; box.z2 = f
	data << box
}

def key(x,y,z) {"$x;$y;$z"} //{ Objects.hash(x,y,z) }

def part1( List<Box> data ) {
	def ons = [] as Set<String>
	data.each { b ->
		if( b.x1 >= -50 && b.x2 <= 50 && b.y1 >= -50 && b.y2 <= 50 &&  b.z1 >= -50 && b.z2 <= 50 ) {
			((b.x1)..(b.x2)).each { x ->
				((b.y1)..(b.y2)).each { y ->
					((b.z1)..(b.z2)).each { z->
						def k = key(x,y,z)
						if( b.on ) ons << k
						else if( ons.contains(k) ) ons.remove(k)
					}
				}
			}
		}
	}
	println ons.size()
}

part1(data)