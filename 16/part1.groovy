def realData='020D78804D397973DB5B934D9280CC9F43080286957D9F60923592619D3230047C0109763976295356007365B37539ADE687F333EA8469200B666F5DC84E80232FC2C91B8490041332EB4006C4759775933530052C0119FAA7CB6ED57B9BBFBDC153004B0024299B490E537AFE3DA069EC507800370980F96F924A4F1E0495F691259198031C95AEF587B85B254F49C27AA2640082490F4B0F9802B2CFDA0094D5FB5D626E32B16D300565398DC6AFF600A080371BA12C1900042A37C398490F67BDDB131802928F5A009080351DA1FC441006A3C46C82020084FC1BE07CEA298029A008CCF08E5ED4689FD73BAA4510C009981C20056E2E4FAACA36000A10600D45A8750CC8010989716A299002171E634439200B47001009C749C7591BD7D0431002A4A73029866200F1277D7D8570043123A976AD72FFBD9CC80501A00AE677F5A43D8DB54D5FDECB7C8DEB0C77F8683005FC0109FCE7C89252E72693370545007A29C5B832E017CFF3E6B262126E7298FA1CC4A072E0054F5FBECC06671FE7D2C802359B56A0040245924585400F40313580B9B10031C00A500354009100300081D50028C00C1002C005BA300204008200FB50033F70028001FE60053A7E93957E1D09940209B7195A56BCC75AE7F18D46E273882402CCD006A600084C1D8ED0E8401D8A90BE12CCF2F4C4ADA602013BC401B8C11360880021B1361E4511007609C7B8CA8002DC32200F3AC01698EE2FF8A2C95B42F2DBAEB48A401BC5802737F8460C537F8460CF3D953100625C5A7D766E9CB7A39D8820082F29A9C9C244D6529C589F8C693EA5CD0218043382126492AD732924022CE006AE200DC248471D00010986D17A3547F200CA340149EDC4F67B71399BAEF2A64024B78028200FC778311CC40188AF0DA194CF743CC014E4D5A5AFBB4A4F30C9AC435004E662BB3EF0'

def toBits( String s ) {
	Map c2bin = ['0':'0000', '1':'0001', '2':'0010', '3':'0011', '4':'0100', '5':'0101', '6':'0110', '7':'0111', '8':'1000', '9':'1001', 'A':'1010', 'B':'1011', 'C':'1100', 'D':'1101', 'E':'1110', 'F':'1111']
	def result = s.collect { c2bin[it] }.join()
	def resultSplit = s.collect { c2bin[it] }.join(', ')
	//println "$s toBits = $resultSplit"
	return result
}

//////// TESTS /////////
[
 '8A004A801A8002F478':16,
 '620080001611562C8802118E34':12, 
 'C0015000016115A2E0802F182340':23, 
 'A0016C880162017C3686B18A3D4780':31
].each { kk, vv ->
 	def pp = Packet.startParse( toBits(kk) )
 	assert pp.versionSum() == vv
 }

[
 'C200B40A82':3,
 '04005AC33890':54, 
 '880086C3E88112':7, 
 'CE00C43D881120':9,
 'D8005AC2A8F0':1,
 'F600BC2D8F':0, 
 '9C005AC2F8F0':0,
 '9C0141080250320F1802104A08':1
].each { kk, vv ->
 	def pp = Packet.startParse( toBits(kk) )
 	assert pp.calculate() == vv
 }

def p1 = Packet.startParse( toBits(realData) )
p1.print()
println( "version sum: " + p1.versionSum() ) // 901
println( "calculated : " + p1.calculate() )  // 110434737925


/*
3 bits Version
3 bits Type

Type=4: single (binary) number in blocks of 5 bits:
	- include next 4 bits if prefix bit is a 1
	- last group of 4 bits has prefix 0
	- 

Type=(inte 4): operator:
	- if first bit == 0
		then next 15 bits is total length of all subpackets
		else next 11 bits is number of subpackets
	- after those numbers start subpackets
*/

abstract class Packet {
	int version, type
	int bitsUsed

	static int peekType( String s, int i ) {
		def result = Packet.toDecimal( s[(i+3)..(i+5)] )
		def ver = Packet.toDecimal( s[(i)..(i+2)] )
		//println "peeked package type $result (version $ver) from $i:${s[i..(i+5)]} in $s"
		return result
	}

	static def getVersionAndType( String s, int i ) {
		def ver = Packet.toDecimal( s[(i)..(i+2)] )
		def result = Packet.toDecimal( s[(i+3)..(i+5)] )
		return [ver,result]
	}

	static Packet startParse( String s, int i = 0 ) {
		if( peekType(s,i) == 4 ) 
			return new Value().parse(s,i)

		return new Operator().parse(s,i)
	}

	int versionSum() { version }

	abstract void print( int indentation = 0 );
	abstract long calculate();

	// Assumes all numbers are positive so first bit does not indicate sign.
	static long toDecimal( String s ) {
		//println "toDecimal( $s )"
		long result = 0
		int N = s.size()
		def r = s.reverse()
		for( int x=0; x<N; x++ ) {
			if( r[x] == '1' ) {
				def v = Math.pow( 2, x )
				result += v
				//println " index $x, value $v, result $result"
			}
		}
		//if(s[0] == '1') result = -result
		result
	}

	static def parseNumber( String s, int startIndex ) {
		int lastUsedIndex = 0
		int i = startIndex
		def b = new StringBuilder(20)
		while( s[i] == '1' ) {
			b.append(s[i+1]).append(s[i+2]).append(s[i+3]).append(s[i+4])
			i += 5
		}
		b.append(s[i+1]).append(s[i+2]).append(s[i+3]).append(s[i+4])
		i += 5
		long number = toDecimal( b.toString() )
		//println "parseNumber( $s, $startIndex ) -> $number"
		return [number, i]
	}

	static info(String s, int i, String info) {
		//println "$s (${s.size()} bits)"
		//println "${' ' * i}^ $info"
	}
}

class Value extends Packet {
	long v

	Value parse( String s, int i = 0 ) {
		info( s, i, "Value header (6 bits)")
		//println "Parsing VALUE from $start, ver $version, type $type. New index $i"
		int start = i
		(version,type) = getVersionAndType(s,i)
		version = Packet.toDecimal( s[i..(i+2)] )
		i += 6
		info( s, i, "number starts")
		(v,i) = Packet.parseNumber( s, i )
		bitsUsed = i - start
		info( s, i, "number = $v ($bitsUsed bits)")
		//println "Did parse Value $v ($bitsUsed bits used)"
		this
	}

	void print( int indentation = 0 ) {
		println "${' ' * indentation}Value $v (version $version)"
	}

	long calculate() { v }
}

class Operator extends Packet {
	int lengthtype
	List<Packet> subpackets = []

	int versionSum() { version + subpackets.sum { it.versionSum() } }

	// @param s Binary-style string.
	Operator parse( String s, int i = 0 ) {
		int start = i
		info( s, i, "Operator start (6 bits)")
		(version,type) = getVersionAndType(s,i)
		i+=6
		info( s, i, "Length type (1 bit)")
		lengthtype = s[i++] as int

		//println "Parsing OP from $start, ver $version, type $type, I $lengthtype. New index $i"

		if( lengthtype == 1 ) {
			def numSubPackets = toDecimal( s[i..(i+10)] )
			info( s, i, "subpacket count = $numSubPackets (11 bits)")
			i += 11
			//println "Used 11 bits of subpacket count. Parsing $numSubPackets sub packets from index $i ..."
			numSubPackets.times { n -> 
				int sptype = peekType( s, i )
				if( sptype == 4 ) {
					def vp = new Value().parse( s, i )
					subpackets.add( vp )
					i += vp.bitsUsed
					//bitsUsed += vp.bitsUsed
				}
				else {
					// parsing of op packet in packet count mode
					def op = new Operator().parse( s, i )
					subpackets.add( op )
					i += op.bitsUsed
					//bitsUsed += op.bitsUsed
				}
			}
		}
		else {
			info( s, i, "number of bits in subpackets (15 bits)")
			def numSubPacketsBits = toDecimal( s[i..(i+14)] )
			i += 15
			//println " Parsing $numSubPacketsBits bits of sub packets from index $i: ${s[i..(i+numSubPacketsBits)]}"
			int usedSubPacketBits = 0
			while( true ) {
				int sptype = peekType( s, i )			
				if( sptype == 4 ) {
					def vp = new Value().parse( s, i )
					subpackets.add( vp )
					i += vp.bitsUsed
					usedSubPacketBits += vp.bitsUsed
					if( usedSubPacketBits == numSubPacketsBits ) break
					if( usedSubPacketBits > numSubPacketsBits ) throw new Exception('packet bit count error, used $usedSubPacketBits > const $numSubPacketsBits')
				}
				else {
					// parsing an op packet in bitcount mode
					def op = new Operator().parse( s, i )
					subpackets.add( op )
					i += op.bitsUsed
					usedSubPacketBits += op.bitsUsed
					if( usedSubPacketBits == numSubPacketsBits ) break
					if( usedSubPacketBits > numSubPacketsBits ) throw new Exception('packet bit count error, used $usedSubPacketBits > const $numSubPacketsBits')
				}
			}
		}
		info( s, i, 'OP ends here' )
		bitsUsed = i-start
		this
	}

	void print( int indentation = 0 ) {
		println "${' ' * indentation}Operator (version $version) type $type"
		subpackets.each { it.print( indentation + 1 )}
	}

	long calculate() {
		if( type == 0 ) return subpackets.sum { it.calculate() }
		if( type == 1 ) return subpackets.inject(1) { acc, item -> acc * item.calculate() }
		if( type == 2 ) return subpackets.min { it.calculate() }.calculate()
		if( type == 3 ) return subpackets.max { it.calculate() }.calculate()
		if( type == 5 ) return subpackets[0].calculate() > subpackets[1].calculate() ? 1 : 0
		if( type == 6 ) return subpackets[0].calculate() < subpackets[1].calculate() ? 1 : 0
		if( type == 7 ) return subpackets[0].calculate() == subpackets[1].calculate() ? 1 : 0
	}
}

