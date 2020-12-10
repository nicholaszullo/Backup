public class SortedArrayToTree {
	public TreeNode sortedArrayToBST(int[] nums) {
        if (nums.length == 0)
            return null;
        return findNextNode(0, nums.length-1, nums);
    }
    private TreeNode findNextNode(int start, int end, int[] nums){
        TreeNode center = new TreeNode();
        if (start-end == 1){
            center.val = nums[start];
            center.left = new TreeNode(nums[end], null,null);
            center.right = null;
        } else if (end - start == 1){
            center.val = nums[end];
            center.left = new TreeNode(nums[start], null, null);
            center.right = null;
        } else if (start == end){
            center.val = nums[start];
            center.left = null;
            center.right = null;
        } else {
            center.val = nums[(start + end)/2];
            center.left = findNextNode(start,(start + end)/2 -1, nums);
            center.right = findNextNode((start + end)/2 + 1, end, nums);
        }
        return center;
        
    }
}
