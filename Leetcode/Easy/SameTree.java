public class SameTree{
    public boolean isSameTree(TreeNode p, TreeNode q) {
        return dfs(p,q);
    }
    private boolean dfs(TreeNode currp, TreeNode currq){
        if (currp == null && currq == null){
          return true;
        } else if (currp == null || currq == null){
            return false;
        } else if (currp.val != currq.val){
            return false;
        } else {
            return dfs(currp.left,currq.left) && dfs(currp.right, currq.right); 
        }
    }
}