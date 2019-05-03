/*	
*	Nick Zullo Spring CS 445 Assig2B 
*/

public class Assig2B
{
	public static void main(String[] args){
		if (args.length != 1){
			System.out.println("Improper argument usage! Only use 1 number, the number of characters to append");
			System.exit(0);
		}
		int n = Integer.parseInt(args[0]);							//N characters to add 
		int i = 0;													//Counter for how many times program has looped
		
		
		
		StringBuilder builder = new StringBuilder();				//Initialize starting data
		MyStringBuilder myBuilder = new MyStringBuilder();
		String str = null;
		
		double start = System.nanoTime();							//Adds a character to the string N times and records run time
		for (int k = 0; k < n; k++)
			builder.append("a");
		double end = System.nanoTime() - start;
		System.out.println("Testing append:\n\tPredefined StringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " appends\n\t\tTime per append: " + end/n + " ns");
		
		start = System.nanoTime();									//Removes a character from the string and records run time
		for (int k = 0; k < n; k++)
			builder.delete(0,1);
		end = System.nanoTime() - start;
		System.out.println("Testing remove(0):\n\tPredefined StringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " remove(0)\n\t\tTime per remove(0): " + end/n + " ns");

		start = System.nanoTime();			
		for (int k = 0; k < n; k++){								//Adds a character to the middle of the string and records run time
			if (builder.length() <= 1)
				builder.insert(0, "a");								//Special case there is no middle
			else
				builder.insert(builder.length()/2,"a");
		}
		end = System.nanoTime() - start;
		System.out.println("Testing insert:\n\tPredefined StringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " inserts\n\t\tTime per insert: " + end/n + " ns");

		start = System.nanoTime();									//Every other group of code has the same process as first 3
		for (int k = 0; k < n; k++)
			myBuilder.append("a");
		end = System.nanoTime() - start;
		System.out.println("Testing append:\n\tMyStringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " appends\n\t\tTime per append: " + end/n + " ns");
		
		start = System.nanoTime();
		for (int k = 0; k < n; k++)
			myBuilder.delete(0,1);
		end = System.nanoTime() - start;
		System.out.println("Testing remove(0):\n\tMyStringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " remove(0)\n\t\tTime per remove(0): " + end/n + " ns");

		start = System.nanoTime();		
		for (int k = 0; k < n; k++){
			if (myBuilder.length() <= 1)							//Special case there is no middle
				builder.insert(0, "a");
			else
				myBuilder.insert(myBuilder.length()/2,"a");
			
		}
		end = System.nanoTime() - start;
		System.out.println("Testing insert:\n\tMyStringBuilder:\n\t\tTotal time: " + end + " ns for " + n + " inserts\n\t\tTime per insert: " + end/n + " ns");
		
		start = System.nanoTime();
		for (int k = 0; k < n; k++)
			str += "a";
		end = System.nanoTime() - start;
		System.out.println("Testing append:\n\tString:\n\t\tTotal time: " + end + " ns for " + n + " appends\n\t\tTime per append: " + end/n + " ns");
		
		start = System.nanoTime();
		for (int k = 0; k < n; k++)
			str = str.substring(1);
		end = System.nanoTime() - start;
		System.out.println("Testing remove(0):\n\tString:\n\t\tTotal time: " + end + " ns for " + n + " remove(0)\n\t\tTime per remove(0): " + end/n + " ns");
		
		start = System.nanoTime();
		for (int k = 0; k < n; k++){
			if (str.length() <= 1)									//Special case there is no middle
				str += "a";
			else {
				int mid = str.length()/2;
				String temp = str.substring(mid);
				str = str.substring(0,mid-1) + "a" + temp;
			}
				
			
		}
		end = System.nanoTime() - start;				
		System.out.println("Testing insert:\n\tString:\n\t\tTotal time: " + end + " ns for " + n + " inserts\n\t\tTime per insert: " + end/n + " ns");
				
		
		
		
	}
	

}