public class MyString
{
	private char[] letters;
	
	public MyString( String other )
	{	
		letters = other.toCharArray();
	}
	public MyString( MyString other )	//Initializing to null since toString only works after construction
	{	
		this(other.toString());		//Call the other constuctor to reuse code
	}	
	public int length()
	{
		return letters.length;
	}
	public char charAt(int index) // IF INDEX OUT OF BOUNDS. EXIT PROGRAM! (dont return the null char)
	{
		if (index > letters.length-1){
			System.out.println("INDEX OUT OF BOUNDS; MUST BE LESS THAN LENGTH-1");
			System.exit(0);
		}
		return letters[index];
	}
	int compareTo(MyString other)		//Must account for words that have same prefix
	{
		if (other.length() > letters.length){
			if (other.indexOf(this) == 0)
				return -1;
			
		}
		if (letters.length > other.length()){
			if (this.indexOf(other) == 0)
				return 1;
		}
		
		//works for words not identical with 1 having extra letters
		for (int i = 0; i < letters.length; i++){
			if (letters[i] < other.charAt(i))
				return -1;
			else if (letters[i] > other.charAt(i))
				return 1;
		}
		
		return 0;
			
	}	
	public boolean equals(MyString other)
	{
		if (compareTo(other) == 0)
			return true;
		return false;
	}
	// LOOKING for c but starting at [startIndex],  not at [0]
	// You should use this in your other Indexof Method
	public int indexOf(int startIndex, char ch)	
	{
		for (int i = startIndex; i < letters.length; i++){
			if (letters[i] == ch)
				return i;
		}
		return -1;
	}
	public int indexOf(MyString other)
	{	
		int temp = indexOf(0, other.charAt(0));					//If the starting letter of other is not contained in this, return -1
		if (temp == -1)
			return -1;											//i is set to -1 if no letter is found and goes out of bounds when for loop starts
		
		for (int i = temp; i < letters.length; i++){			
			for (int k = 0; k < other.length(); k++){
				
				if (i+k >= letters.length)						//Prevents going out of bounds if this.length() < other.length()
					k = other.length();							//else so that if k is set to exit conditon it doesn't try and access letters[i+k] and go out of bounds
				else if (letters[i+k] != other.charAt(k)){		//If the letters dont match reset sliding window
					k = other.length();
				}
				if (k == other.length()-1){						//if all the letters match it will get here and return the start of the window
					return i;
				}
			}
		}
		return -1;												//If no match found return -1
	}
	
	public String toString()
	{
		return new String(letters);
	}
} // END MYSTRING CLASS
