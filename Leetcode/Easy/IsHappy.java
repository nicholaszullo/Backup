/** Stop whenever a duplicate is found or the sum is 1
*	When you get a duplicate the cycle will just keep repeating so you can safely return false
*
*/
public class IsHappy {
	    public boolean isHappy(int n) {
        HashSet<Integer> seen = new HashSet<Integer>();
        int sum = 0;
        int num = n;
        while (seen.add(num)){
            while (num != 0){
                sum += (num%10)*(num%10);
                num /= 10;
            }
            if (sum == 1){
                return true;
            }            
            num = sum;
            sum = 0;
        }
        return false;
    }


}