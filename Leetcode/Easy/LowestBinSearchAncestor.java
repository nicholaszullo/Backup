/**	Accidentally solved the generalized version instead of using properties of a binary search tree
*	still 4ms, fastest is 3ms
*/

public class LowestBinSearchAncestor {
    TreeNode sol = null;
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        postOrder(root, p, q);
        return sol;
        
    }
    private boolean postOrder(TreeNode curr, TreeNode p, TreeNode q){
        if (curr == null){
            return false;
        }
        boolean left = postOrder(curr.left, p, q);
        boolean right = postOrder(curr.right, p, q);
        if (curr == p && (left || right)){
            sol = curr;
        } else if (curr == q && (left || right)){
            sol = curr;
        } else if (curr == p){
            return true;
        } else if (curr == q){
            return true;
        } else if (left && right){
            sol = curr;
        }
        return left || right;        
    }
}