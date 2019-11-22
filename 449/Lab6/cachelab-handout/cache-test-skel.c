/*
Part A - Mystery Caches
*/

#include <stdlib.h>
#include <stdio.h>

#include "support/mystery-cache.h"

/*
 * NOTE: When using access_cache() you do not need to provide a "real" memory
 * addresses. You can use any convenient integer value as a memory address,
 * you should not be able to cause a segmentation fault by providing a memory
 * address out of your programs address space as the argument to access_cache.
 */

/*
   Returns the size (in B) of each block in the cache.
*/
int get_block_size(void) {
	flush_cache();
	bool_t hit = 1;
	int i = 0;
	access_cache(i);
	while (hit)
	{	
		i++;
		hit = access_cache(i);		
		 
	}
	return i;
}

/* careful with checking blocks and updataing its last used frequency artificially
   should be a power of 2
	Returns the size (in B) of the cache.
*/
int get_cache_size(int block_size) {
	flush_cache();
	int i = 0;
	int k;
	bool_t hit_zero = 1;
	while (hit_zero)
	{
		for (k = 0; k <= i; k += block_size)
		{
			access_cache(k);
		}
		hit_zero = access_cache(0);
		i += block_size;
	}
  	return i-block_size;
}

/* 
   Returns the associativity of the cache.
*/
int get_cache_assoc(int cache_size) {
	flush_cache();
	int i =0;
	int k;
	bool_t hit_zero = 1;
	while(hit_zero)
	{
		for (k = 0; k <= (i*cache_size); k += cache_size)
		{
			access_cache(k);
		}
		hit_zero = access_cache(0);
		i++;
	}
	return i-1;
}

int main(void) {
  int size;
  int assoc;
  int block_size;

  cache_init(0, 0);

  block_size = get_block_size();
  size = get_cache_size(block_size);
  assoc = get_cache_assoc(size);

  printf("Cache block size: %d bytes\n", block_size);
  printf("Cache size: %d bytes\n", size);
  printf("Cache associativity: %d\n", assoc);

  return EXIT_SUCCESS;
}
