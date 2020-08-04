/**
 * 	Runtime O(log(N))? bigger numbers dont cause very many more loops, still only 10-15
 * 	Memory O(1)
 */
public class MySqrt {
	public static void main(String[] args) {
		System.out.println(mySqrt(23456999) + " expected 4843");
	}
	public static int mySqrt(int x){			//Newtons Method
		double curr = x;
		double ans = (curr+(x/curr))/2;
		double prev = x;
		curr = ans;
		while (prev - ans > .00002){
			prev = ans;
			ans = (curr+(x/curr))/2;
			curr = ans;
		}
		return (int) ans;
	}
}