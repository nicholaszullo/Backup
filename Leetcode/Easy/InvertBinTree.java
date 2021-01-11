/**	0ms, but not the cleanest possible dfs solution. Likely can do it without deep copy of vals and just assigning references
*	Flip tree about root, so travel flipped at the same time, send old tree left when new tree goes right and copy values
*/

public class InvertBinTree {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode newRoot = new TreeNode(root.val);
        dfs(newRoot, root);
        return newRoot;
    }
    private void dfs(TreeNode newTree, TreeNode oldTree){
        if (oldTree.left == null){
            if (oldTree.right == null){		//Both are null so stop
                return ;
            }
            newTree.left = new TreeNode(oldTree.right.val);	//Right keeps going but left stopped
            dfs(newTree.left, oldTree.right);
            return ;
        } else if (oldTree.right == null){			//Left keeps going but right stopped
            newTree.right = new TreeNode(oldTree.left.val);
            dfs(newTree.right, oldTree.left);
            return ;
        } 
        else {				//Both keep going
            newTree.left = new TreeNode(oldTree.right.val);
            newTree.right = new TreeNode(oldTree.left.val);
            dfs(newTree.right, oldTree.left);
            dfs(newTree.left, oldTree.right);
        }        
    }
}