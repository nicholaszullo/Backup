public class ValidPalindrome{
    public boolean isPalindrome(String s) {
        s = s.toLowerCase();
        for (int i = 0, k = s.length()-1; i < k; i++, k--){
            for (; i <= k && (s.charAt(i) < 'a' || s.charAt(i) > 'z') && (s.charAt(i) < '0' || s.charAt(i) > '9'); i++);
            for (; k >= i && (s.charAt(k) < 'a' || s.charAt(k) > 'z') && (s.charAt(k) < '0' || s.charAt(k) > '9'); k--);
            //System.out.println(i + " " + k);
            if (i > k){
                return true;
            }
            if (s.charAt(i) != s.charAt(k)){
                return false;
            }
            
        }
        return true;
        
    }

}