public class listNodejava
{
	
	public static void main(String args[]){
		
		printList(first);
		remove(first, 132);
		
		
		
	}
	
	public void printList(Node n){
		if (n != null){
			System.out.println(n.data + ",");
			printList(n.next);
		}
		
	}
	
	
	public void remove(Node first, T key){
		for (first; first != null; first = first.next){
			if (first.data == key)
				first.previous.next = first.next;
			
		}
		
		
	}
	
	
}