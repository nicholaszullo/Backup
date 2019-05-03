	.data
# do not modify
LIST1:	
	.word 2 3 3 4 5 6 7 7 7 8
LIST2:	
	.word 1 3 4 4 5 6 6 9 9 9
LIST3:
	.word 35
received1:   
	.asciiz "LIST1: received "
received2:   
	.asciiz "LIST2: received "
received3:
	.asciiz "LIST3: received "
newline: 
	.asciiz "\n"
expected:
	.asciiz ", expected "
	
	.text

.globl main

main:
	# test case 1
	la a0 LIST1 # get address of the array
	li a1 10 # length of list
	jal mode
	move s0, v0
	la a0 received1
	li v0 4
	syscall # PRINT "LIST1: received "
	move a0 s0
	li v0 1
	syscall # PRINT value returned from call to mode
	la a0 expected
	li v0 4
	syscall # PRINT ", expected "
	li a0 7
	li v0 1
	syscall # PRINT expected result
	la a0 newline
	li v0 4
	syscall # PRINT newline
	
	# test case 2
	la a0 LIST2 # get address of the array
	li a1 10 # length of list
	jal mode
	move s0, v0
	la a0 received2
	li v0 4
	syscall # PRINT "LIST2: received "
	move a0 s0
	li v0 1
	syscall # PRINT value returned from call to mode
	la a0 expected
	li v0 4
	syscall # PRINT ", expected "
	li a0 9
	li v0 1
	syscall # PRINT expected result
	la a0 newline
	li v0 4
	syscall # PRINT newline

	# test case 3
	la a0 LIST3 # get address of the array
	li a1 1 # length of list
	jal mode
	move s0, v0
	la a0 received3
	li v0 4
	syscall # PRINT "LIST3: received "
	move a0 s0
	li v0 1
	syscall # PRINT value returned from call to mode
	la a0 expected
	li v0 4
	syscall # PRINT ", expected "
	li a0 35
	li v0 1
	syscall # PRINT expected result

	li v0 10
	syscall # EXIT
	
mode: # mode(a0,a1)
	# ****** YOUR CODE STARTS HERE ******
	#
        # a0 is the address of the list, and a1 is the length.
        #
	# Try not to deviate too greatly (if at all) from the C version.
        #
	# DO NOT look up the values of the s registers, LIST,
        # etc from above. You will receive no credit if you do.
        #
        # This problem can be solved using only the a0-a3,t0-t?,
        # v0,ra registers.

	lw t1 0(a0) # currNum = arr[0]
        li t2 1 # count = 1
        move v0 t1 # mode = currNum
        li t3 0 # modeCount = 0
        li t4 1 # initialize i to 1
        
        # Begin your code here, starting at the for loop in the C code.
loop:
	li t9, 4			#constant to multiply offset by
	
	bge t4, a1, return		#end for if i>= length, or loop while i < length
	  mul t5,t4,t9			# k = i*4, k is offset size
	  add t6, t5, a0		# point to address arr[i] is located
	  lw t6, 0(t6)			# load arr[i] 
	  bne t6, t1, else1		#if arr[i] == currNum
	    addi t2, t2, 1		#count++
else1:
	addi t7, a1, -1			#t7 = len-1
 	beq t4, t7, true		#i == len-1
 	  bne t6, t1, true		#arr[i] != currNum
 	    j exit1
true:
	ble t2, t3, exit		#if count > modeCount
	  move t3, t2			#modeCount = count
	  move v0, t1			#mode = currNum
exit:
	move t1, t6			#currNum = arr[i]
 	li t2, 1			#count = 1
exit1:
 	addi, t4, t4, 1			#i++
 	j loop
return:
	jr ra # return
