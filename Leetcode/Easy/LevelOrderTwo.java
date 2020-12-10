import java.util.*;

/**
 * Messy, adds on way down then reverse. Rewrite to add on way up
 */
public class LevelOrderTwo{
	public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        recLevelOrderBottom(root, ans, 0);
        Collections.reverse(ans);
        return ans;
    }
    private void recLevelOrderBottom(TreeNode curr, List<List<Integer>> ans, int depth){
        if (curr == null){
            return ;
        } else {
            if (ans.size() == depth || ans.get(depth) == null){
                ans.add(new ArrayList<Integer>());
            }
            ans.get(depth).add(curr.val);
        }
        recLevelOrderBottom(curr.left, ans, depth+1);
        recLevelOrderBottom(curr.right, ans, depth+1);        
    }


}