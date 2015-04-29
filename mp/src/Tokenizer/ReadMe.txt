Authors:
Stephen Bush
Jonathan Koenes
Skyler Downes


This is the java code for a uPascal Token Scanner. To use the scanner, make an instance of MicroPascalScanner, giving a file as input to the constructor.

Once you have an instance of MicroPascalScanner, you can use several methods on it: 

getAllTokens(): Returns a LinkedList that contains Tokens from the Token Class
remove(int index): Remove a token at a certain index in the list

Example:

File inputFile = new File("some/place/toScanStuffFrom");
LinkedList<Token> list = new LinkedList<Token>;

MicroPascalScanner sc = new MicroPascalScanner(inputFile);
list = sc.getAllTokens();


