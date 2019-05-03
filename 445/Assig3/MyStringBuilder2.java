// CS 0445 Spring 2019
// Read this class and its comments very carefully to make sure you implement
// the class properly.  The data and public methods in this class are identical
// to those MyStringBuilder, with the exception of the two additional methods
// shown at the end.  You cannot change the data or add any instance
// variables.  However, you may (and will need to) add some private methods.
// No iteration is allowed in this implementation. 

// For more details on the general functionality of most of these methods, 
// see the specifications of the similar method in the StringBuilder class.  
public class MyStringBuilder2
{
	// These are the only three instance variables you are allowed to have.
	// See details of CNode class below.  In other words, you MAY NOT add
	// any additional instance variables to this class.  However, you may
	// use any method variables that you need within individual methods.
	// But remember that you may NOT use any variables of any other
	// linked list class or of the predefined StringBuilder or 
	// or StringBuffer class in any place in your code.  You may only use the
	// String class where it is an argument or return type in a method.
	private CNode firstC;	// reference to front of list.  This reference is necessary
							// to keep track of the list
	private CNode lastC; 	// reference to last node of list.  This reference is
							// necessary to improve the efficiency of the append()
							// method
	private int length;  	// number of characters in the list

	// You may also add any additional private methods that you need to
	// help with your implementation of the public methods.


	public MyStringBuilder2(String str){
		firstC = null;
		lastC = null;
		if (!(str.equals("")))
			rec_init_string(str,0);
		else 
			length = 0;
	}
	private void rec_init_string(String data, int loc){
		
		if (loc == data.length()-1){								//Base Case
			CNode n = new CNode(data.charAt(loc));					//Initialize the end of the list
			length++;
			firstC = n;
			lastC = n;
		}
		else{
			rec_init_string(data, ++loc);
			CNode n = new CNode(data.charAt(loc-1));				//Builds back to front
			length++;
			n.next = firstC;
			firstC = n;
		}

	}
	
	// Create a new MyStringBuilder2 initialized with the chars in array s
	public MyStringBuilder2(char[] str){
		firstC = null;
		lastC = null;
		rec_init_char(str, 0);
	}
	
	
	public void rec_init_char(char[] data, int loc){				//Loc is where in data[] we are 
		if (loc == data.length-1){									//Base Case
			CNode n = new CNode(data[loc]);							//Initialize the end of the list
			firstC = n;
			lastC = n;
			length++;
		}
		else{
			rec_init_char(data, ++loc);
			CNode n = new CNode(data[loc-1]);						//Build back to front	
			length++;		
			n.next = firstC;
			firstC = n;
		}
	}

	// Create a new empty MyStringBuilder2
	public MyStringBuilder2()
	{
		firstC = null;												//Initialize to null
		lastC = null;
		length = 0;
	}

	// Append MyStringBuilder2 b to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(MyStringBuilder2 b)
	{
		recAppendBuilder(b.firstC, 0);								//Call start of recursive method
		return this;
	}
	
	private void recAppendBuilder(CNode curr, int loc){
		if (curr == null){											//When finished, update length
			length += loc;
		}
		else {
			lastC.next = new CNode(curr.data);						//Create a new node so seperate MSB can be created
			lastC = lastC.next;
			recAppendBuilder(curr.next, ++loc);
			
		}
		
	}


	// Append String s to the end of the current MyStringBuilder2, and return
	// the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(String s)
	{
		recAppendString(s, 0);										
		return this;
	}
	
	private void recAppendString(String s, int loc){
		if (length == 0){											//If list is empty create a new node and update firstC and lastC to be that node					
			firstC = new CNode(s.charAt(loc));
			lastC = firstC;
			length++;
			recAppendString(s,++loc);
		} else if (loc == s.length()){								//At the end of recursion update length
			length += loc;
		} else {
			lastC.next = new CNode(s.charAt(loc));					//Add new char to the end and continue adding letters
			lastC = lastC.next;
			recAppendString(s, ++loc);
			
		}
		
	}

	// Append char array c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char [] c)
	{
		recAppendChar(c, 0);										
		return this;
	}

	private void recAppendChar(char[] c, int loc){
		if (length == 0){											//If list is empty create a new node and update firstC and lastC to be that node					
			firstC = new CNode(c[loc]);
			lastC = firstC;
			length++;
			recAppendChar(c,++loc);
		}else if (loc == c.length){									//Update length at the end ad stop recursing
			length += loc;
		}
		else {														//Add the next char to end of list until no more chars
			lastC.next = new CNode(c[loc]);	
			lastC = lastC.next;
			recAppendChar(c, ++loc);
			
		}
		
	}
	
	// Append char c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char c)
	{
		if (lastC == null){											//Special case if appending to an empty list
			firstC = new CNode(c);									//Make the firstC a new node
			lastC = firstC;											//Lastc is firstC since only 1 node
			length++;
			return this;
		}	
		lastC.next = new CNode(c);									//No recursion necessary, just add to end
		lastC = lastC.next;
		length++;
		return this;
	}

	// Return the character at location "index" in the current MyStringBuilder2.
	// If index is invalid, throw an IndexOutOfBoundsException.
	public char charAt(int index)
	{
		return recCharAt(firstC, index, 0);
	}
	
	private char recCharAt(CNode curr, int index, int loc){
		if (loc >= length){											//Checks to see if index is valid
			throw new IndexOutOfBoundsException("Index given is not a valid location!");
		}else {
			if (loc == index)										//Once index is reached, return that data
				return curr.data;
			else
				return recCharAt(curr.next, index, ++loc);			//Find node[index]
		}
		
	}

	// Delete the characters from index "start" to index "end" - 1 in the
	// current MyStringBuilder2, and return the current MyStringBuilder2.
	// If "start" is invalid or "end" <= "start" do nothing (just return the
	// MyStringBuilder2 as is).  If "end" is past the end of the MyStringBuilder2, 
	// only remove up until the end of the MyStringBuilder2. Be careful for 
	// special cases!
	public MyStringBuilder2 delete(int start, int end)
	{
		if (start >= end){											//Check given data to see if valid
			throw new IndexOutOfBoundsException("Index given is not a valid location!");
		}else if (end >= length)
			end = length;
		
		recDelete(firstC, start, end, 0);							//Start recursing
		length -= (end - start);									//Update length
		return this;
	}
	
	private CNode recDelete(CNode curr, int start, int end, int loc){
		CNode temp = null;
		
		if (loc == end)												//On the way up recursion, find ending node (node where deletion ends), stop recurssing, and return it back down recursion
			return curr;
		else if (loc != length)
			temp = recDelete(curr.next, start, end, ++loc);
		
		if (loc == start && end == length){							//Special case if deleting last node 
			curr.next = temp;
			lastC = curr;											//Update lastC
		}else if (loc == start)										//Normal case, link out the part of string being deleted, links middle of string to later part in string
			curr.next = temp;
		else if (start == 0)										//Special case if deleting in front, set the firstC to be the ending node, 
			firstC = temp;
		
		
		return temp;
		
	}

	// Delete the character at location "index" from the current
	// MyStringBuilder2 and return the current MyStringBuilder2.  If "index" is
	// invalid, do nothing (just return the MyStringBuilder2 as is).
	// Be careful for special cases!
	public MyStringBuilder2 deleteCharAt(int index)
	{
		if (index >= length){										//Make sure index is valid
		//	System.out.println("Index given is not a valid location!");
			return this;
		} else if (index < 0)
			return this;
		
		recDeleteCharAt(firstC, index, 0);
		length--;
		return this;
	}

	private void recDeleteCharAt(CNode curr, int index, int loc){
		if (index == 0 && length == 1){
			firstC = null;
			lastC = null;
		}
		else if (index == 0)											//Special case remove frst node, set the first node to be the second node
			firstC = firstC.next;
		else if (loc == index-1 && loc < length-1)						//Normal case, set the node before to link to node after deleted char
			curr.next = curr.next.next;
		else if (loc == length-1){										//Special case remove last node, set the 2nd to last.next to null and update lastC
			curr.next = null;
			lastC = curr;
		}else
			recDeleteCharAt(curr.next, index, ++loc);
		
	}
	// Find and return the index within the current MyStringBuilder2 where
	// String str first matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.
	public int indexOf(String str)
	{
		int temp = recIndexOf(str, firstC, 0,0);					//Returns the index where found string ends
		if (temp == -1)												//If not found, do not adjust index
			return temp;
		else 														//If found, move index returned to the front of string not the end
			return temp - (str.length()-1);
	}
	
	private int recIndexOf(String str, CNode curr, int loc, int node){			
		if (str.length()-1 == loc && curr.data == str.charAt(loc))
			return node;											//Base case at the end of string and found return the position
		 else if (node == length-1)
			return -1;												//Base case at the end of string and not found return -1
		 else if (str.charAt(loc) == curr.data)						//If equal continue to recurse checking next pairing
			return recIndexOf(str, curr.next, ++loc, ++node);		//Return what was passed down from base case
		 else														//If not equal, restart and look again 
			return recIndexOf(str, curr.next, 0, ++node);			//Return what was passed down from base case
		
		
	}
	
	// Insert String str into the current MyStringBuilder2 starting at index
	// "offset" and return the current MyStringBuilder2.  if "offset" == 
	// length, this is the same as append.  If "offset" is invalid
	// do nothing.
	public MyStringBuilder2 insert(int offset, String str)
	{
		MyStringBuilder2 other = new MyStringBuilder2(str);
		if (offset > length)
			return this;
		else if (offset == 0){										//2 special cases if offset is 0 add to front
			other.lastC.next = firstC;
			firstC = other.firstC;
			length += other.length;
		}else if (offset == length)									//if offset is length just append it
			append(other);
		else
			recInsert(offset, other, firstC, 0);					//Normal case traverse to offset then link
		
		
		
		return this;
	}

	private void recInsert(int offset, MyStringBuilder2 other, CNode curr, int node){		
		if (node == offset-1){										//When node before offset is reached, link in new string
			other.lastC.next = curr.next;
			curr.next = other.firstC;
			length += other.length;
			return ;
		}
		recInsert(offset, other, curr.next, ++node);				//Travel to needed node
		
		
	}
	// Insert character c into the current MyStringBuilder2 at index
	// "offset" and return the current MyStringBuilder2.  If "offset" ==
	// length, this is the same as append.  If "offset" is invalid, 
	// do nothing.
	public MyStringBuilder2 insert(int offset, char c)
	{
		CNode other = new CNode(c);
		
		if (offset > length)										//if invalid offset do nothing
			return this;	
		else if (offset == 0){										//if adding to begining special case update firstC
			other.next = firstC;
			firstC = other;
			length++;												//Update length
		} else if (offset == length)								//if adding to end call append
			append(c);												//Append updates length 
		else 
			recInsertChar(offset, other, firstC, 0);				//Normal case loop up to position to insert
		return this;
		
	}
	
	private void recInsertChar(int offset, CNode other, CNode curr, int node){
		if (node == offset-1){										//When node before offset is reached, link in new char
			other.next = curr.next;
			curr.next = other;
			length++;												//Update length
			return ;
		}
		recInsertChar(offset, other, curr.next, ++node);			//Travel to needed node
		
		
	}

	// Insert char array c into the current MyStringBuilder2 starting at index
	// index "offset" and return the current MyStringBuilder2.  If "offset" is
	// invalid, do nothing.
	public MyStringBuilder2 insert(int offset, char [] c)
	{
		MyStringBuilder2 other = new MyStringBuilder2(c);			//Method idea is same as previous 2 cases
		
		if (offset > length)
			return this;
		else if (offset == 0){										//If begining, link end of char[] to firstC and firstC to begining of char[]
			other.lastC.next = firstC;								
			firstC = other.firstC;
			length += other.length();
		} else if (offset == length)								//Special case at the end is same as append
			append(c);
		else 
			recInsertCharArr(offset, other, firstC, 0);
		return this;
		
	}
	
	private void recInsertCharArr(int offset, MyStringBuilder2 other, CNode curr, int node){
		
		if (node == offset-1){
			other.lastC.next = curr.next;							//When found, link in array into current list
			curr.next = other.firstC;
			length += other.length;
			return ;
		}
		recInsertCharArr(offset, other, curr.next, ++node);			//Travel to needed node

	}

	// Return the length of the current MyStringBuilder2
	public int length()
	{
		return length;
	}

	// Delete the substring from "start" to "end" - 1 in the current
	// MyStringBuilder2, then insert String "str" into the current
	// MyStringBuilder2 starting at index "start", then return the current
	// MyStringBuilder2.  If "start" is invalid or "end" <= "start", do nothing.
	// If "end" is past the end of the MyStringBuilder2, only delete until the
	// end of the MyStringBuilder2, then insert.  This method should be done
	// as efficiently as possible.  In particular, you may NOT simply call
	// the delete() method followed by the insert() method, since that will
	// require an extra traversal of the linked list.
	public MyStringBuilder2 replace(int start, int end, String str)
	{
		if (start >= end)											//Check given data to see if valid
			throw new IndexOutOfBoundsException("Range given is not valid!");
		else if (end >= length)
			end = length;
		
		MyStringBuilder2 other = new MyStringBuilder2(str);			
		recReplace(start, end, other, firstC, 0);
		length = length - (end - start) + other.length;				//Update length
		return this;
	}
	
	private CNode recReplace(int start, int end, MyStringBuilder2 other, CNode curr, int node){
		CNode stop = null;
		
		if (node == end){											//On the way up recursion, find ending node, stop recursing, and return it back down recursion
			other.lastC.next = curr;
			return curr;
		}else if (node != length)
			stop = recReplace(start, end, other, curr.next, ++node);
		if (node == start && end == length){						//Special case if replacing to last node 
			other.lastC.next = stop;								
			curr.next = other.firstC;								//Link in new string
			lastC = other.lastC;									//Set the new last to be the new string last
		}else if (node == start){									//Normal case link in new string to middle
			curr.next = other.firstC;
			other.lastC.next = stop;
		}else if (start == 0){										//If replacing from front
			firstC = other.firstC;									//Set new front to be the new string front
			other.lastC.next = stop;								//Link last to end of replace char
		}
		return stop;
	}

	// Reverse the characters in the current MyStringBuilder2 and then
	// return the current MyStringBuilder2.
	public MyStringBuilder2 reverse()
	{
		char[] reversed = new char[length];							//Temp array to store values in
		recReverse(firstC, 0, length,reversed);
		
		MyStringBuilder2 other = new MyStringBuilder2(reversed);
		firstC = other.firstC;										//set this = new string builder
		lastC = other.lastC;
		
		return this;	
	}
	
	private void recReverse(CNode curr, int node, int loc, char[] reversed){ 
		loc--;														//Decrement loc regardless of if statement so at base case loc will -- and reach 0 so it isnt 1 twice and overwrite data
		if (node < length-1)
			recReverse(curr.next,++node,loc, reversed);
		reversed[loc] = curr.data;									//After reaching the end, add to list coming back down. loc will increase and curr will move down list
	}
	
	// Return as a String the substring of characters from index "start" to
	// index "end" - 1 within the current MyStringBuilder2
	public String substring(int start, int end)
	{
		char[] c = new char[(end-start)+1];							//Temp array 
		if (start >= end || end >= length)							//Check bounds to be valid
			return this.toString();									
		else {
			recSub(c,start,end,firstC,0,0);							//If valid return the substring
			return (new String(c));
		}
	}
	private void recSub(char[] c, int start, int end, CNode curr, int node, int loc){
		if (node >= start && node < end){							//Base case when inside region of substring add to array and keep moving up array
			c[loc] = curr.data;
			recSub(c,start,end,curr.next,++node,++loc);
			
		} else if (node > end)										//If it is past end stop so recSub doesn't go out of bounds when curr == null
			return ;
		else 
			recSub(c,start,end,curr.next,++node,loc);				//Move up the list
		
		
	}

	// Return the entire contents of the current MyStringBuilder2 as a String
	public String toString()
	{
		char [] c = new char[length];								//char array as big as current amount of characters in MSB2
		getString(c, 0, firstC);									//Add chars to array
		return (new String(c));										//Return char array as string

	}
	private void getString(char [] c, int pos, CNode curr)
	{
      if (curr != null)
		{
			c[pos] = curr.data;										//Add the current char to the array
			getString(c, pos+1, curr.next);							//Loop
		}
	}
	// Find and return the index within the current MyStringBuilder2 where
	// String str LAST matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.  For some
	// help with this see the Assignment 3 specifications.
	public int lastIndexOf(String str)
	{
		int result = recLastIndexOf(str, firstC, 0,str.length()-1, false);
		if (result == -1)											//If it returns -1 return -1
			return -1;
		else 
			return --result;										//result returned is one too big so adjust it
	}
	
	private int recLastIndexOf(String str, CNode curr, int node, int loc,boolean initalLoopFinished){
		if (node < length-1 && !initalLoopFinished){				//Initially travel to end of list, but never call again so extra boolean needed
			loc = recLastIndexOf(str, curr.next, ++node, loc, initalLoopFinished);
			initalLoopFinished = true;
		}
		
		if (loc > str.length()-1)									//Pass up location of last occurance
			return loc;
		else if (str.charAt(loc) == curr.data && loc == 0)			//Matched the entire string
			return node;
		 else if (str.charAt(loc) == curr.data)						//Matched the current chars from the string and curr
			return --loc;
		else if (node == 1)											//If it reaches the end and does not find anything node will be 1
			return -1;
		else if (loc != str.length()-1)								//If it does not find anything and is not at the end of list recurse back until loc is at begining of str
			return recLastIndexOf(str,curr.next, ++node, ++loc, initalLoopFinished);
		else 														//Pass variable back through
			return loc;	
		
	}
	
	// Find and return an array of MyStringBuilder2, where each MyStringBuilder2
	// in the return array corresponds to a part of the match of the array of
	// patterns in the argument.  If the overall match does not succeed, return
	// null.  For much more detail on the requirements of this method, see the
	// Assignment 3 specifications.
	public MyStringBuilder2[] regMatch(String [] pats)
	{
		MyStringBuilder2[] matches = new MyStringBuilder2[pats.length];
		initalizeMatches(matches, 0);
		if (recTestPat(pats, firstC, 0,0, matches)){				//Run regMatch test
			if (matches[0].length == 0)								//If there is no match matches[0] will be empty
				return null;
			
			return matches;											//If there is a match return it
		}
		else 
			return null;											//If it returns false there is not match so return null
	
	}
	
	//Recursively initalizes the matches array with empty MyStringBuilder2 objects
	private void initalizeMatches(MyStringBuilder2[] matches, int loc){
		if (loc < matches.length){									//If there are more MyStringBuilder2 objects to initalize
			matches[loc] = new MyStringBuilder2();					//Make an empty MSB2 in that spot
			initalizeMatches(matches, ++loc);						//Loop
		}
	}
	
	//If statements have extra spaces so they look cleaner and easier to read, not needed earlier in code 
	private boolean recTestPat(String[] pats, CNode curr, int patLoc, int node, MyStringBuilder2[] matches){
		if (node == length-1 && matches[0].length == 0 && patLoc == 0)
		{																		//Base case the first pattern has no matches
			return false;														//No characters match the first pattern
		}
		else if (patLoc == pats.length)											//Base case no patterns left
		{																		//All patterns have been matched
			return true;
		}	
		else if (node == length-1 && pats[patLoc].indexOf(curr.data) >= 0)
		{																		//Base case char matched and at the end of list
			matches[patLoc].append(curr.data);
			if (matches[matches.length-1].length == 0)							//If all patterns were not matched try the next pattern
			{																	//This is needed for backtracking 
				matches[patLoc].deleteCharAt(matches[patLoc].length-1);
				recTestPat(pats, curr, ++patLoc, node, matches);				//New call for next pattern with same char
				
			}
		}
		else if (pats[patLoc].indexOf(curr.data) >= 0)
		{																		//If curr.data is in the current pattern, add to matches for current pattern
			matches[patLoc].append(curr.data);
			recTestPat(pats, curr.next, patLoc, (node+1), matches);				//Test the next char with the same pattern
			if (matches[matches.length-1].length == 0)							//Needed for backtracking if there are no matches to last pattern 
			{																	//remove letter and try and match letter to next pattern
				matches[patLoc].deleteCharAt(matches[patLoc].length-1);
				recTestPat(pats, curr, ++patLoc, node, matches);
			}
		}
		else if (matches[0].length == 0 && node != length-1)					//If no character has been matched yet check next character
		{																		//Only can traverse like this in first pattern or not matching in order
			if (matches[patLoc].length >0)										//Make sure there are no chars in other matches or matches will be wrong
			{
				matches[patLoc].deleteCharAt(matches[patLoc].length-1);
				recTestPat(pats,curr.next, patLoc, (node+1), matches);
			}
			else if (patLoc == 0)												//Try the next char to match in current pattern
			{
				recTestPat(pats,curr.next, patLoc, (node+1), matches);
			}
			else
			{																	//If current patLoc is not 0, new call with pattern 0 anyway
				recTestPat(pats,curr.next, 0, (node+1), matches);
			}
		}
		else if (matches[patLoc].length == 0 && node != length-1)				//If curr is not contained in the pattern this statement is reached
		{																		//Backtrack because must match in order, cant
			return false;
		}	
		else if (matches[patLoc].length > 0 && pats[patLoc].indexOf(curr.data) == -1)
		{																		//If the characters stop matching the pattern but a character matched already move on to next pattern
			recTestPat(pats, curr, ++patLoc, node, matches);
		}
		else if (matches[matches.length-1].length == 0)							//Not all the patterns have been matched must backtrack
		{																		//Will get here if nothiing else is true
			return false;
		}
		
		return true;															//Needed for compiler
		
	}

	// You must use this inner class exactly as specified below.  Note that
	// since it is an inner class, the MyStringBuilder2 class MAY access the
	// data and next fields directly.
	private class CNode
	{
		private char data;
		private CNode next;

		public CNode(char c)
		{
			data = c;
			next = null;
		}

		public CNode(char c, CNode n)
		{
			data = c;
			next = n;
		}
	}
}

