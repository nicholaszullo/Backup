/* 1) to compile:
   $ gcc -o mode mode.c
 2) to run:
   $ ./mode
 */
#include <stdio.h>
#include <stdlib.h>

int mode(int* arr, int len) {
  int currNum = arr[0];
  int count = 1;
  int mode = currNum;
  int modeCount = 0;
  int i;
  for (i = 1; i < len; i++) {
    if (arr[i] == currNum) {
      count++;
    }
    if (i == len - 1 || arr[i] != currNum) {
      if (count > modeCount) {
        modeCount = count;
        mode = currNum;
      }
      currNum = arr[i];
      count = 1;
    }
  }
  return mode;
}

int main(void) {
  int arr[] = {2, 2, 2, 3, 4, 6, 7, 9, 9, 9, 9};
  printf("%d\n", mode(arr, sizeof(arr) / sizeof(int)));
}
