import java.util.*;

public class PathSumTwo {
	public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        dfs(root,sum,0,ans, new ArrayList<Integer>());
        return ans;
    }
    
    private void dfs(TreeNode curr, int sum, int currSum, List<List<Integer>> ans, List<Integer> currPath){
       if (curr == null){
            return ;
        } else if (curr.left == null && curr.right == null && sum == currSum+curr.val){
           currPath.add(curr.val);
           ans.add(new ArrayList<Integer>(currPath));
           currPath.remove(currPath.size()-1);
        } else{
            currPath.add(curr.val);
            dfs(curr.left, sum, currSum+curr.val,ans,currPath);
            dfs(curr.right,sum,currSum+curr.val,ans,currPath);
            currPath.remove(currPath.size()-1);
        }
        
    }


}