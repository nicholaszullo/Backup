
.globl main

main:
	lui	a0,0x8000	# should be 31
	jal	first1pos
	jal	printv0
	lui	a0,0x0001	# should be 16
	jal	first1pos
	jal	printv0
	li	a0,1		# should be 0
	jal	first1pos
	jal	printv0
	add	a0,$0,$0	# should be -1
	jal	first1pos
	jal	printv0
	li	v0,10
	syscall


first1pos:	# placeholder to call different versions: first1posshift of first1posmask
	addiu	sp, sp, -4
	sw	ra, 0(sp)
	jal	first1posmask
	lw	ra, 0(sp)
	addiu	sp, sp, 4
	jr	ra


first1posshift:
	move t0, zero				#set counter to 0 for a new call
	addi t0, t0, -1
loop:
	
	beq a0, zero, end			
	  srl a0, a0, 1				
	  addi t0, t0, 1			#counter++ of bit position
	  j loop
end:
	move v0, t0
	jr ra
first1posmask:
	add t0, t0, 0xffffffff
	and t1, a0, t0				#make t1 1 only in bits that a0 is 1
	add t3, zero, zero
	addi t3, t3, -1				#counter
loop1:
	beq t1, zero, end1
	  srl t1, t1, 1				#move bit positions down  
	  addi t3, t3, 1			#counter++
	  j loop1
end1:
	move v0, t3
	jr ra



printv0:
	addi	sp,sp,-4
	sw	ra,0(sp)
	add	a0,v0,0
	li	v0,1
	syscall
	li	v0,11
	li	a0,'\n'
	syscall
	lw	ra,0(sp)
	addi	sp,sp,4
	jr	ra
