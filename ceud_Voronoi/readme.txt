::OPERATION::

To use the voronoi generator, specify desired input parameters via the commandline.
	e.g. "Voronoi.jar -i:mypoints.txt -a:true -t:voronoi2 -w:1024 -h:768"
Be sure not to include spaces in file names!


::ARGUMENTS::

Command: -i
  Usage: -i:file.txt
   Info: 1 set of coordinates per line, each line in format "x,y"

Command: -r
  Usage: -r:250
Default: 0
   Info: Number of random vertices to be generated

Command: -a
  Usage: -a:true
Default: false
   Info: Use anti-aliasing to smooth jagged lines

Command: -t
  Usage: -t:voronoi2
Default: mixed2
   Info: Type of diagram to create
  Types: delaunay (edges only)
	 delaunay2 (with points)
	 voronoi (edges only)
	 voronoi2 (with points)
	 mixed (edges only)
	 mixed2 (with points)

Command: -w
  Usage: -w:100
Default: 500
   Info: Set width of output diagram

Command: -h
  Usage: -h:100
Default: 500
   Info: Set height of output diagram