/* Lab0.java

	compilation:         C:\> javac Lab0.java
	sample execution:    C:\> java Lab0   Tim Hoffman hoffmant@pitt Computer Science 
*/

public class Lab0
{
	public static void main( String[] args )
	{
		for (int i=0 ; i<args.length ; ++i)
			System.out.format( "args[%d] %s\n", i, args[i] );
		System.out.println("Successfully ran program"); 
	}

	
}