
# A note on choice of instructions:
# For pedagogical purposes, we avoided using instructions in the pseudoinstruction set
# as much as possible. Mostly, instead of li and move, we are using addiu and addu, and instead
# of bge/blt, we are using slt/slti with beq/bne. 

# These below are declarations of constants.
# So, anywhere in my MIPS program where we use NEXT_OFFSET, it will be replaced
# exactly by a 4. 
# Do not confuse these constants with things in the .data section. The assembler
# substitutes constant symbols for constant values during assembly. 
# Constants DO NOT exist in the memory of your running program.
.eqv DATA_OFFSET 0  # the first field
.eqv NEXT_OFFSET 4  # the second field
.eqv SIZEOF_LISTNODE 8   # = sizeof(int)+sizeof(reference) = 4 + 4 = 8
.eqv TESTVALUES_LEN 4  # change if you change the testvalues below

# data section is where global data goes
.data
comma: .asciiz ","
# int[] testvalues = {2,6,3,0}
testvalues: .word 2 6 3 0     # we use .word to mean each number should be 4 bytes, i.e. size of one int

.globl main

# text section is where the code goes
.text
j main

# Call this procedure to allocate
# and initialize a new ListNode
#
# Recall "new" in Java gets translated
# to two steps
# 1. allocate memory for the object
# 2. call the constructor to initialize the memory
#
# arguments:
#   data: value for the ListNode's data field
#
# return address of the new ListNode object
newListNode:
	# prolog
	addiu $sp,$sp,-8   # allocate space on the stack for two integers
	sw $ra, 0($sp)     # save the return address (because $ra will get overwritten by "syscall" below)
  	sw $s0, 4($sp)     # save $s0, because we are writing a new value to it below
  
	# 1.
	addu $s0, $zero, $a0       # save the argument because we need it later
	addiu $v0, $zero, 9        # call sbrk(SIZEOF_LISTNODE)
	addiu $a0, $zero, SIZEOF_LISTNODE
	syscall
	addu $t0, $zero, $v0      # t0 = address of the ListNode, i.e. "this"
	
	# 2.
	sw $s0, DATA_OFFSET($t0)   # this.data = data
	                           # exactly equivalent to sw $s0, 0($t0); go look at the assebled code!
	sw $zero, NEXT_OFFSET($t0) # this.next = null
				   # recall that we represent null with the address 0.
				   # again, exactly equivalent to sw $s0, 4($t0), just more convenient to write it with constants
	
	# "new" has a return value: a reference to the allocated object, which is in t0
	addu $v0, $zero, $t0
	
	# epilog
	lw $ra, 0($sp)      # restore value of $ra we had during the prolog
	lw $s0, 4($sp)      # restore value of $s0 we had during the prolog
	addiu $sp,$sp,8     # give back stack space
	jr $ra		    # jump back to caller

# Append a new ListNode with the given data value to the end of the linked list
#
# arguments
# this: the address of the node we called .append on. Notice that this is an implicit argument in Java
# data: the data of the new node
#
# returns nothing
append:
	# prolog
	addiu $sp,$sp,-8    # allocate space on the stack for two integers
	sw $ra, 0($sp)	    # save the return address because we will overwrite it when we recursively call append
	sw $s0, 4($sp)      # save $s0 because we need to use that register
	
	addu $s0, $zero, $a0       # s0 = this
	lw $t0, NEXT_OFFSET($s0)   # t0 = this.next
	bne $t0,$zero,we_have_not_reached_the_end   # if (this.next==null) 
				# the base case, we are at the end of the list
	addu $a0, $zero, $a1    # v0 = newListNode(data)
	jal newListNode         #
	sw $v0, NEXT_OFFSET($s0)# this.next = v0
	j append_end            # nothing left to do except return
	
we_have_not_reached_the_end:    # the case that we are not yet at the end of the list
	addu $a0, $zero, $t0    # call this.next.append; a0 = this.next; a1 is still set to data
	jal append              #
	# when recursive call returns, we just continue executing the next instruction,
	# which happens to be the epilog below

append_end:
	# epilog
	lw $ra, 0($sp)    # restore registers
	lw $s0, 4($sp)    #
	addiu $sp,$sp,8   # give back stack frame
	jr $ra            # return to caller
	
	
# Print data field in order in the given list;
# e.g., if the list contains [2,6,3,0], then this
# procedure should print
# 2,6,3,0,
# 
# Assume it will never be called on a NULL value
#
# exercise for the student; refer to append code for hints
printlist:
	push a0					#save node address
	beq a0, zero, endoflist			# if first != null
	   li v0, 1				#v0 = 1
	   lw a0, DATA_OFFSET(a0)
	   syscall				#print a0
	   la a0, comma
	   li v0, 4
	   syscall				#print ","
	   pop a0				#retrieve node 
	   lw a0, NEXT_OFFSET(a0)		#node = node.next
	   j printlist				#recurse until reach th eend
endoflist:
	jr ra
# Remove a data field in order in the given list;
# e.g., if the list contains [2,6,3,0] and data parameter is 3, then this
# procedure should change the list to [2,6,0]
# only need to worry about the first ocurrence (if exists)
#
# Assume it will never be called on a NULL value
#
# exercise for the student; refer to append code for hints
#Remove works for any location in array, front middle and end
remove:
	push ra
	push s0
	push a0
	lw s0, NEXT_OFFSET(a0)				#s0 = secondNode
loop:		
	lw t0, 0(a0)				#t0 = a0.data
	beq a0, zero, endoflist2		#if a0 != null
	   move t3, a0
	   lw a0, NEXT_OFFSET(a0)		#a0 = a0.next
	   bne t0, a1, loop			#if a0.data == key
	   					#2 special cases, remove from front or end
	   	beq a0, s0, firstNode		#Since a0 was moved to the next node, if it is the second node then the first node needs removed
	   	beq a0, zero, lastNode		#if it is null, the last node needs to be removed jump to last node remove code
	   	   lw t1, -NEXT_OFFSET(t3)	#set the node before deletion next node to be node after deletion
	   	   lw t2, NEXT_OFFSET(t3)
	   	   sw t2, -NEXT_OFFSET(t1)
		   j endoflist2
firstNode:
	pop a0		#make the new first node be the second node
	pop s0
	lw s0, NEXT_OFFSET(s0)
	pop ra
	jr ra
	
lastNode:
	sw zero, -NEXT_OFFSET(t3)		#New last node is lastnode.prev
	
endoflist2:
	pop a0
	pop s0
	pop ra
	jr ra

main:
	la $t0, testvalues # (REMEMBER that la is a pseudo instruction; go see what it turns into!)
	lw $a0, 0($t0)     # v0 = newListNode(testvalues[0])
	jal newListNode    #
	addu $s0, $zero, $v0  # s0 = v0 = head
	
	addiu $s1,$zero,1  # s1 is i
	
testloop:
	slti $t0, $s1,TESTVALUES_LEN # i < TESTVALUES
	beq $t0,$zero,testdone     #
	la $t0, testvalues   # a1=testvalues[i]
	sll $t1,$s1,2        #
	addu $t0,$t0,$t1     #
	lw $a1, 0($t0)       #
	addu $a0,$zero,$s0   # a0 = head
	jal append
	addiu $s1,$s1,1      # i++
	j testloop

testdone:	
	
	addu $a0,$zero,$s0   # head node
	jal printlist        # print original list
	
	addu $a0,$zero,$s0   # head node
	li $a1, 0            # data = 3
	jal remove           # remove data

    # new line
	li	a0, '\n'
	li	v0, 11
	syscall

	addu $a0,$zero,$s0   # head node	
	jal printlist        # print new list
	
exit:
	addiu $v0,$zero,10
	syscall 		 

