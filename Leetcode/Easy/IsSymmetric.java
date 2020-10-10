public class IsSymmetric {
	public boolean isSymmetric(TreeNode root) {
        TreeNode p = root;
        TreeNode q = root;
        return dfs(p,q);
    }
    private boolean dfs(TreeNode currp, TreeNode currq){
        if (currp == null && currq == null){
          return true;
        } else if (currp == null || currq == null){
            return false;
        } else if ((currp != null && currq != null) && currp.val != currq.val){
            return false;
        } else {
            return dfs(currp.left,currq.right) && dfs(currp.right, currq.left); 
        }
    }
}