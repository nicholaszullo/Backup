public class Misc
{
	public static void main(String[] args)
	{
		int[] values = {80,20,40,10,60};
		int ans = doit(values,0,values.length-1);
		System.out.println("The answer is: " + ans);
	}
	public static int doit(int[] data, int left, int right)
	{
		System.out.println("Looking at data: " + left + " to " + right);
		if (left == right)
		{
			System.out.println("\t\tBase case returning: " + data[left]);
			return data[left];
		}else 
		{
			int mid = (left+right)/2;
			int oneAns = doit(data, left, mid);
			int twoAns = doit(data, mid+1,right);
			System.out.println("\tRec we hav: " + oneAns + " and " + twoAns);
			if (oneAns <= twoAns)
			{
				System.out.println("\t\tReturning " + oneAns);
				return oneAns;
			}
			else{
				System.out.println("\t\tReturning " + twoAns);
				return twoAns;
			}
		}
	}





}