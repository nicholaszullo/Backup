/**	10ms -> 0ms by switching from string concat with + to using Integer.toString and StringBuilder
*	Also saved 50% more memory 
*/
public class SummaryRanges {
    public List<String> summaryRanges(int[] nums) {
        List<String> ans = new ArrayList<String>();
        if (nums.length == 0) return ans;
        int start = nums[0];
        boolean finished = false;
        for (int i = 0; i < nums.length; i++){
            if (i != 0 && nums[i-1] != nums[i]-1){
                if (start == nums[i-1]){
                    ans.add(Integer.toString(start));
                } else {
                    StringBuilder app = new StringBuilder();
                    app.append(start);
                    app.append("->");
                    app.append(nums[i-1]);
                    ans.add(app.toString());
                }
                start = nums[i];
            }
        }
        if (!finished){
            if (start == nums[nums.length-1]){
                ans.add(Integer.toString(start));
            } else {
                StringBuilder app = new StringBuilder();
                app.append(start);
                app.append("->");
                app.append(nums[nums.length-1]);
                ans.add(app.toString());
            }
        }
        return ans;
    }

}