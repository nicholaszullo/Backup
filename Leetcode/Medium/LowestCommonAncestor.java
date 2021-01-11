/**	Run a post order traversal, when they both return true it's found
*
*/
public class LowestCommonAncestor {
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
        if (curr == p && (left || right)){		//Curr can be its own ancestor so check if found this and other one
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