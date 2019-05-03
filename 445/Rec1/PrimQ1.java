import java.util.*;
public class PrimQ1<T> implements SimpleQueue<T>, Moves
{
	private ArrayList<T> array;
	private int totalMoves = 0;

	public PrimQ1(int size)
	{
		array = new ArrayList<T>();
	}
		
	public boolean offer(T element){
		setMoves(getMoves()+1);
		if (array.add(element))				//Add the element to the end of the Queue
			return true;
		else
			return false;
	}
	public T poll(){	
		if (isEmpty())
			return null;
		
		setMoves(getMoves()+1);				//Remove the first element from the Queue
		return array.remove(0);
	}
	
	public T peek(){
		setMoves(getMoves()+1);
		return array.get(0);
	}
	
	public boolean isEmpty(){
		setMoves(getMoves()+1);
		if (array.size() == 0)
			return true;
		else 
			return false;
	}
	
	public void clear(){
		setMoves(getMoves()+1);
		array.clear();
	}
	
	public int getMoves(){
		return totalMoves;
	}
	public void setMoves(int val){
		totalMoves = val;
		
	}
		
}	
	