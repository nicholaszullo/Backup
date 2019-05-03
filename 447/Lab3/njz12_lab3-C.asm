.data
	myArray: .word 0, 4, -3, 5 ,2 ,-1, 6, 15, -8, 10
	str: .asciiz "What is the first index?(0-9)\n"
  	str1: .asciiz "What is the second index? (0-9)\n"	
  	a: .word 0
  	b: .word 0
.text
.globl main
main:
  la a0, str    # printString(str)
  li v0, 4
  syscall

  li v0, 5      # a = getInteger()
  syscall
  sw v0, a

  la a0, str1    #print str1
  li v0, 4
  syscall

  li v0, 5	# b = getInteger()
  syscall
  sw v0, b  
  
  la t3, myArray	#store location of array
  lw t1, a
  lw t2, b
  
  add t1,t1,t1		#move a to length of a word index
  add t1,t1,t1		
  add t4,t1,t3		#move to location in array + index
  lw t5, 0(t4)
  
  add t2,t2,t2		#move b to length of a word index
  add t2,t2,t2
  add t4, t2,t3		#move to location in array + index
  lw t6, 0(t4)
  
  add a0, t5, t6	#add values of array
  li v0,1
  syscall