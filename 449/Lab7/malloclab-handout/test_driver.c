#include "mm.h"
#include "memlib.h"

int main()
{
   mem_init();
   mm_init();
   int* points[2000];
   int i = 0;
   for (i = 0; i < 528; i++)
   {
      points[i] = mm_malloc(2040);
      printf("Returned %p to driver of size %d\n", points[i], 2048);		//+8 from header
   }
   for (i = 527; i > 0; i--)
   {
      printf("Driver freeing %p\n", points[i]);
      mm_free(points[i]); 
   }
   for (i = 1000; i < 2000; i++)
   {
      points[i] = mm_malloc(4080);
      printf("Returned %p to driver\n", points[i]);
   }
   for (i = 1000; i < 2000; i++)
   {
      printf("Driver freeing %p\n", points[i]);
      mm_free(points[i]);
   }

   return 1;
}

