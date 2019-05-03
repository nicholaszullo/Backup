
#====================================================================
#      Static data allocation and initialization - DO NOT MODIFY!
#====================================================================

.data

# Allocate space for the simulated registers.
registers: .space 128   # 32 registers x 4 bytes/register

# Allocate space for the program to simulate.
# For simplicity, we assume it won't be more than 50 instructions.
program:   .space 200   # 50 instructions x 4 bytes/instruction

#====================================================================
#       Program text
#====================================================================

.text
.globl main

main:
        # read number of instructions
        ori     $v0, $0, 5      # set up for readint syscall
        syscall                 # result will be in v0
        or      $s0, $0, $v0    # save count of number of instructions

        # loop reading instructions into simulated memory
        la $t0, program         # t0 now points at program memory

        # while (s0 > 0) read an instruction
rdPgm:  beq     $s0, $0, pgmReadDone
        ori     $v0, $0, 5      # prepare for readint call
        syscall
        sw      $v0, 0($t0)     # save instruction to program memory
        addi    $t0, $t0, 4     # increment pointer to next word of program memory
        addi    $s0, $s0, -1    # decrement count of number of instructions
        j       rdPgm

pgmReadDone:
        # initialize 0 to 0
        sw      $0, registers
        
        #  s0 will be the simulated PC.
        la $s0, program
 
        # now go into fetch/increment/decode/execute loop
        #   s0  - PC (as a pointer into program memory)
        #   t0  - IR (fetched instruction)
        #   t1  - 0x10 (literal value)
        ori     $t1, $0, 0x10   # halt opcode

fetchInst:          
        lw      $t0, 0($s0)     # fetch next instruction
        addi    $s0, $s0, 4     # increment PC
        
        #
        #---------------- YOUR CODE START HERE ... --------------------------
        #
	la t8, registers
	
	srl t1, t0, 26				#opcode of instruction in t1
	bne t1, $0, iType			#Check if i type
						#if not i type it is R type (7 instructions given are not J type so dont check since input is good)
	sll t1, t0, 26				#funct of instruction
	srl t1, t1, 26
	ori t2, $0, 0x20			#add
	beq t1, t2, addOp
	ori t2, $0, 0x22			#sub
	beq t1, t2, subOp
	ori t2, $0, 0x25			#or 
	beq t1, t2, orOp
	ori t2, $0, 0x26			#xor
	beq t1, t2, xorOp
	j exit
	   
iType:
	ori t2, $0, 0x04			#beq
	beq t1, t2, beqOp
	ori t2, $0, 0x08			#addi
	beq t1, t2, addiOp
	j exit

findRegR:
	sll t1, t0, 6				#rsrtrdshamtfunct00000
	srl t1, t1, 27				#00000..rs
	addi t9, $0, 4				
	mul t1, t1, t9				
	add s1, t1, t8				#location of rs in simulated registers
	
	sll t1, t0, 11				#rtrdshamtfunct00000...
	srl t1, t1, 27				#00000..rt
	mul t1, t1 t9
	add s2, t1, t8				#location of rt in simulated registers
	
	sll t1, t0, 16				#rdshamtfunct0000..
	srl t1, t1, 27				#00000...rd
	mul t1, t1, t9
	add s3, t1, t8				#location of rd in simulated reg

	jr ra					#return to the operation who called 
	
findRegI:
	sll t1, t0, 6				#rsrtimmediate00000
	srl t1, t1, 27				#00000..rs
	addi t9, $0, 4				
	mul t1, t1, t9				
	add s1, t1, t8				#location of rs in simulated registers
	
	sll t1, t0, 11				#rtrdimmediate00000...
	srl t1, t1, 27				#00000..rt
	mul t1, t1 t9
	add s2, t1, t8				#location of rt in simulated registers
	
	sll t1, t0, 16				#immediate000...
	srl t1, t1, 16				#0000...immediate
	addi s3, t8, 4
	sw t1, 0(s3)				#save immediate in AT
	
	jr ra
	

#Section of code where operations are performed
addOp:	
	jal findRegR			
	lw t1, 0(s1)				#load values of rs and rt 
	lw t2, 0(s2)
	add t3, t1, t2				#perform opperation
	sw t3, 0(s3)				#store in simulated register
	j exit
	
subOp:
	jal findRegR			
	lw t1, 0(s1)				#load values of rs and rt 
	lw t2, 0(s2)
	sub t3, t1, t2				#perform opperation
	sw t3, 0(s3)				#store in simulated register rd refered to
	j exit
orOp:
	jal findRegR			
	lw t1, 0(s1)				#load values of rs and rt 
	lw t2, 0(s2)
	or t3, t1, t2				#perform opperation
	sw t3, 0(s3)				#store in simulated register
	j exit
xorOp:
	jal findRegR			
	lw t1, 0(s1)				#load values of rs and rt 
	lw t2, 0(s2)
	xor t3, t1, t2				#perform opperation
	sw t3, 0(s3)				#store in simulated register
	j exit
	
#How to handle negative address?
beqOp:
	jal findRegI
	lw t1, 0(s1)				#Load values of rs and rt		
	lw t2, 0(s2)
	lw t3, 0(s3)				#Load the immediate (how far away to branch)
	mul t3, t3, 4				#Word-align the immediate
	beq t1, t2, jump			#perform the operation
	j exit					#Dont jump if not equal
jump:	
	add s0, s0, t3				#Branch to (PC+4) +(immediate *4) ... PC already +4 at top of fetchInst
	j exit
addiOp:
	jal findRegI
	lw t1, 0(s1)				#Load rs and immediate
	lw t2, 0(s3)
	add t3, t1, t2
	sw t3, 0(s2)				#Save result to rt
	j exit
		

        # skeleton just looks for halt instruction
exit:
        srl     $t7, $t0, 26
        ori     $t1, $0, 0x10   		# halt opcode
        bne     $t7, $t1, fetchInst

        #
        #---------------- UP TO HERE ----------------------------------------
        #

# exit

addi s3, t8, 4					#Set AT to 0 to match shell program output... Not necessary otherwise
sw $0, 0(s3)		

li $v0, 10
syscall        


