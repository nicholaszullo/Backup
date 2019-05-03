// CS 0445 Spring 2019
// Recitation Exercise 3 Main Program
public class CS445Rec3
{
	public static String [] data = {"first", "second", "third", "fourth", "fifth"};
	
	public static void main(String [] args)
	{
		SimpleRecList list = new SimpleRecList(data);
		System.out.println(list.toString());
		
		// Once you have implemented the reverse() method, uncomment the code
		// below to test it.
		
		list.reverse();
		System.out.println(list.toString());
		list.reverse();
		System.out.println(list.toString());
		
	}
}
