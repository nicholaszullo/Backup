import java.util.*;
public class PrimQ2<T> implements SimpleQueue<T>, Moves
{
	private ArrayList<T> array;
	private int totalMoves = 0;
	
	public PrimQ2(int size)
	{
		array = new ArrayList<T>(size);
		
	}
	public boolean offer(T element){
		setMoves(getMoves()+1);
		array.add(0, element);
		if (array.get(0).equals(element))
			return true;
		else
			return false;
	}
	
	public T poll(){
		if (isEmpty())
			return null;
		setMoves(getMoves()+1);
		return array.remove(array.size()-1);
	}
	
	public T peek(){
		setMoves(getMoves()+1);
		return array.get(array.size()-1);
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
		