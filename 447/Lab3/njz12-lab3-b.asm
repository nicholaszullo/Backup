.data
  a: .word 0x1a1b1c1d

.text
.globl main
main:
la t0,a
addi t0,t0,3
lbu t1,0(t0)
