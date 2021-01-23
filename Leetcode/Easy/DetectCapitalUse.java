/**	Must either have no capital letters, all capital letters, or first letter capital
*	O(N)
*/
public class DetectCapitalUse {
    public boolean detectCapitalUse(String word) {
        boolean none = word.charAt(0) >= 'a';	//If the first letter is capital, initialize to false
        boolean first = !none;	//First letter is capital initialize to true
        boolean all = first;	//First letter is capital initialize to true
        for (int i = 1; i < word.length(); i++){
            if (word.charAt(i) < 'a'){
                none = false;	//Capital letter so not none
                if (first) first = false;	//If this is a repeat capital not first
            } else {
                all = false;	//Not a capital so not all
            }
        }
        return none || first || all;	//If one case is true that condition held 
    }

}