def data = ("data" as File).text.split('\n').collect { s -> Integer.parseInt(s) }
println data.withIndex().count { v,i -> i > 0 && v > data[i-1] }
