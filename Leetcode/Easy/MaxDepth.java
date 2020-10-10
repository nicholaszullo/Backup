public class MaxDepth{
    public int maxDepth(TreeNode root) {
        return recMaxDepth(root, 0);
    }
    private int recMaxDepth(TreeNode curr, int curr2){
        if (curr == null){
            return curr2;
        } else {
            return Math.max(recMaxDepth(curr.left,curr2+1), recMaxDepth(curr.right, curr2+1));
        }
        
    }
}