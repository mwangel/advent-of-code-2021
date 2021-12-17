def data = ("data" as File).text.split('\n').collect { s -> Integer.parseInt(s) }

List threes = []
for( int i=0; i < data.size()-2; i++ ) {
    threes[i] = data[i] + data[i+1] + data[i+2]
}

println threes.withIndex().count { v,i -> i > 0 && v > threes[i-1] }
