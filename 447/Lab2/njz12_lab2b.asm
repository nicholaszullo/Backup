.data
  str: .asciiz "What is the first input?\n"
  str1: .asciiz "What is the second input?\n"	
  str2: .asciiz " - "
  str3: .asciiz " = "

  # First Input
  a:   .word 0
  b:	.word 0
  c:	.word 0
  
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

  li v0, 5
  syscall
  sw v0, b  

  lw t0, a	#subtract a and b and store in c
  lw t1, b
  sub t2, t0, t1	
  sw t2, c		
 
  
  li v0, 1      # a = printInteger(a)
  lw a0, a
  syscall
  
  la a0, str2	#print ' - '
  li v0, 4
  syscall
  
  lw a0, b	#print b
  li v0,1
  syscall
  
  la a0, str3	#print ' = '
  li v0, 4
  syscall
  
  li v0, 1	#print c
  lw a0, c
  syscall

  li v0, 10     # exit() - stops the program
  syscall
