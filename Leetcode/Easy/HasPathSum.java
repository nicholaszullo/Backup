public class HasPathSum {

    public boolean hasPathSum(TreeNode root, int sum) {
        if (root==null || (root.val ==sum && (root.left != null || root.right != null))) return false;		//Messy account for special cases. Clean up?
        return dfs(root,sum,0);
    }
    
    private boolean dfs(TreeNode curr, int sum, int currSum){
       if (curr == null){
            return false;
        } else if (curr.left == null && curr.right == null && sum == currSum+curr.val){
            return true;
        } else{
            return dfs(curr.left, sum, currSum+curr.val) || dfs(curr.right,sum,currSum+curr.val);
        }
        
    }


}