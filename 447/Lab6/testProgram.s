
# Put a test program (of no more than 50 instructions) in here

.globl main

main:

        addi t0, $0, 10
        addi t1, $0, 11
        beq t1, t2, end
        or t2, t1, t0
        addi t3, $0, 100
        sub t3, t3, t2
        xor t2, t1, t0
end:    
        # this (not MIPS) instruction will be automatically included later (no worries)
        #halt
        
        
