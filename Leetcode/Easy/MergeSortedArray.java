public class MergeSortedArray {

	public void merge(int[] nums1, int m, int[] nums2, int n) {
        int one = 0;
        int two = 0;
        for (int i = 0; i < nums1.length; i++){
            if (one < m && two < n){
                if (nums1[one] < nums2[two]){
                    one++;
                } else if (nums1[one] == nums2[two]){
                    one++;
                } else if (nums2[two] < nums1[one]){
                    shiftRight(nums1, i);
                    nums1[i] = nums2[two];
                    two++;
                    one++;
                    m++;
                }      
            } else if (two < n){
                shiftRight(nums1, i);
                nums1[i] = nums2[two];
                two++;
                one++;
                m++;
            }
            
        }

    }
    private void shiftRight(int[] arr, int index){
        for (int i = arr.length-1; i > index; i--){
            arr[i] = arr[i-1];
        }
        
        
    }
}