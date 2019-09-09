/* 
 * CS:APP Data Lab 
 * 
 * <Nick Zullo njz12>
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:

Replace the "return" statement in each function with one
or more lines of C code that implements the function. Your code 
must conform to the following style:

int Funct(arg1, arg2, ...) {
	/* brief description of how your implementation works */
	int var1 = Expr1;
	...
		int varM = ExprM;

	varJ = ExprJ;
	...
		varN = ExprN;
	return ExprR;
}

Each "Expr" is an expression using ONLY the following:
1. Integer constants 0 through 255 (0xFF), inclusive. You are
not allowed to use big constants such as 0xffffffff.
2. Function arguments and local variables (no global variables).
3. Unary integer operations ! ~
4. Binary integer operations & ^ | + << >>

Some of the problems restrict the set of allowed operators even further.
Each "Expr" may consist of multiple operators. You are not restricted to
one operator per line.

You are expressly forbidden to:
1. Use any control constructs such as if, do, while, for, switch, etc.
2. Define or use any macros.
3. Define any additional functions in this file.
4. Call any functions.
5. Use any other operations, such as &&, ||, -, or ?:
6. Use any form of casting.
7. Use any data type other than int.  This implies that you
cannot use arrays, structs, or unions.


You may assume that your machine:
1. Uses 2s complement, 32-bit representations of integers.
2. Performs right shifts arithmetically.
3. Has unpredictable behavior when shifting if the shift amount
is less than 0 or greater than 31.


EXAMPLES OF ACCEPTABLE CODING STYLE:
/*
 * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
 */
int pow2plus1(int x) {
	/* exploit ability of shifts to compute powers of 2 */
	return (1 << x) + 1;
}

/*
 * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
 */
int pow2plus4(int x) {
	/* exploit ability of shifts to compute powers of 2 */
	int result = (1 << x);
	result += 4;
	return result;
}

FLOATING POINT CODING RULES

For the problems that require you to implement floating-point operations,
	the coding rules are less strict.  You are allowed to use looping and
	conditional control.  You are allowed to use both ints and unsigneds.
	You can use arbitrary integer and unsigned constants. You can use any arithmetic,
	logical, or comparison operations on int or unsigned data.

	You are expressly forbidden to:
	1. Define or use any macros.
	2. Define any additional functions in this file.
	3. Call any functions.
	4. Use any form of casting.
	5. Use any data type other than int or unsigned.  This means that you
	cannot use arrays, structs, or unions.
	6. Use any floating point data types, operations, or constants.


	NOTES:
	1. Use the dlc (data lab checker) compiler (described in the handout) to 
	check the legality of your solutions.
	2. Each function has a maximum number of operations (integer, logical,
			or comparison) that you are allowed to use for your implementation
	of the function.  The max operator count is checked by dlc.
	Note that assignment ('=') is not counted; you may use as many of
	these as you want without penalty.
	3. Use the btest test harness to check your functions for correctness.
	4. Use the BDD checker to formally verify your functions
	5. The maximum number of ops for each function is given in the
	header comment for each function. If there are any inconsistencies 
	between the maximum ops in the writeup and in this file, consider
	this file the authoritative source.

	/*
	 * STEP 2: Modify the following functions according the coding rules.
	 * 
	 *   IMPORTANT. TO AVOID GRADING SURPRISES:
	 *   1. Use the dlc compiler to check that your solutions conform
	 *      to the coding rules.
	 *   2. Use the BDD checker to formally verify that your solutions produce 
	 *      the correct answers.
	 */


#endif
	//1
	/* 
	 * bitXor - x^y using only ~ and & 
	 *   Example: bitXor(4, 5) = 1
	 *   Legal ops: ~ &
	 *   Max ops: 14
	 *   Rating: 1
	 */
int bitXor(int x, int y) {
	int xandy = x & y;
	int xnandy = ~x & ~y;
	return ~xandy & ~xnandy;
}
/* 
 * tmin - return minimum two's complement integer 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
int tmin(void) {
	return 1 << 31;									//tmin is 1000..0
}
//2
/* 
 * allOddBits - return 1 if all odd-numbered bits in word set to 1
 *   where bits are numbered from 0 (least significant) to 31 (most significant)
 *   Examples allOddBits(0xFFFFFFFD) = 0, allOddBits(0xAAAAAAAA) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 2
 */
int allOddBits(int x) {
	int odd1 = (((((0xAA << 8) | 0xAA) << 8) | 0xAA) << 8) | 0xAA;	//Make mask of 0xAAAAAAAA
	int result = x & odd1;
	int even1 = ~odd1;								//Mask of all even bits
	result = result | even1;						//At this point if all are odd are 1 result == 0xFFFFFFFF
	return (!(~result));							
}
//3
/* 
 * conditional - same as x ? y : z 
 *   Example: conditional(2,4,5) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 16
 *   Rating: 3
 */
int conditional(int x, int y, int z) {
	int pick = !(!x);				//1 if x is 1 or 0 if x is 0
	int mask = ~pick + 1;				//-1 if x is 1 (~1 = 1111..10 +1 = all 1's) or 0 (~0 is all 1's +1 is 0) if x is 0
	int result = y & mask;				//either y or 0
	int result2 = z & ~mask;			//either 0 or z
	return (result | result2);			//0 or (y or z) 
		
}
//4
/* 
 * logicalNeg - implement the ! operator, using all of 
 *              the legal operators except !
 *   Examples: logicalNeg(3) = 0, logicalNeg(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4 
 */
int logicalNeg(int x){
	int result = ~x+1;                      //Flip number
	result = ~(result | x);                 //If 0 result is -1 otherwise result has MSB of 0
	result = (result >> 31) & 1;            //Take MSB down to LSB and return 1 if it is 1 or 0 if it is 0 
	return result;
}
//float
/* 
 * floatFloat2Int - Return bit-level equivalent of expression (int) f
 *   for floating point argument f.
 *   Argument is passed as unsigned int, but
 *   it is to be interpreted as the bit-level representation of a
 *   single-precision floating point value.
 *   Anything out of range (including NaN and infinity) should return
 *   0x80000000u.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
int floatFloat2Int(unsigned uf) {
	int sign = (uf >> 31) & 1;
	int exp = uf >> 23;	
	int mask = 0xFF;
	int frac, sum;
	int i = 0;
	exp = exp & mask;				
	
	if (exp == 0xFF)			//If uf is infinity or NaN return designated value 
		return 0x80000000;//return 0x7FC00000u;		//Directions say return 0x7fc but autograder uses 0x8000????
 	if (exp < 127) 				//If number is less than 0 return 0
		return 0;
	
	//Continue with deciphering bits
	exp = exp - 127;			//Apply bias	
	if (exp > 24)				//If 2^exp is larger than max int value overflow so return invalid
		return 0x80000000;
				
	sum = 0;
	frac = uf >> (23 - exp);		//Move frac bits into position (will be left with bits in frac that come before the decimal point)	
	
	for (; i < exp ; i++){
		if ((sum + (frac%2)*(1<<i)) < sum)			//If overflow return invalid
			return 0x80000000;
		sum += (frac % 2)*(1<<i);	//Interpret bits into value they are representing
		frac = frac >> 1;		//Move to next bit
	}
	exp = 1 << exp;				//exp = 2^exp 
	sum = sum + exp;			//Number being represented is value of exp and value of whole numbers in frac
	if (sign == 1)				//make negative if negative
		sum = sum *-1;

	return sum;
}
