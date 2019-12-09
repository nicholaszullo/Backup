/**
 *  A binary search tree with min or max area of an apartment at the top
 *  SCRAPPED!!
 */
public class BST
{
    private boolean isMax;
    private Node root;

    public BST(boolean max, Apartment first)
    {
        isMax = max;                                            //If true, sort by max area. If false, sort by min area
        root = new Node(first);
    }

    public void add(Apartment curr)
    {
        addRec(root, new Node(curr));
    }
    private void addRec(Node tree, Node curr)
    {
        if (curr.apt.getArea() < tree.apt.getArea())
        {
            if (tree.left == null)
                tree.left = curr;
            else
                addRec(tree.left, curr);
        } else if (tree.apt.getArea() < curr.apt.getArea())
        {
            if (tree.right == null)
                tree.right = curr;
            else
                addRec(tree.right, curr);
        }
    }

    private class Node
    {
        private Apartment apt;
        private Node left;
        private Node right;
        public Node(Apartment curr)
        {
            apt = curr;
            left = null;
            right = null;
        }
        public Node(Apartment curr, Node left, Node right)
        {
            apt = curr;
            this.left = left;
            this.right = right;
        }
    }
}