import java.util.Arrays;
import java.util.Random;
import java.io.Serializable;
import java.math.BigInteger;		
/**
 * A smaller implementaion of BigInteger. Should be working properly 
 */
public class LargeInteger implements Comparable<LargeInteger>, Serializable{

	private static final long serialVersionUID = 5669105300613114342L;				//Randomly generated serial id for serializable
	private final byte[] ONE = { (byte) 1 };										//Easy to use byte array for quickly creating helper LargeIntegers
	private final byte[] TWO = {(byte)2};
	private final byte[] ZERO = {(byte)0};
	private boolean positive;
	private byte[] val;																//Current representation of number, stored as bytes in Big Endian order


	/**
	 * Construct the LargeInteger from a given byte array 
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = new byte[b.length];
		if (b[0] < 0){
			positive = false;
		}else
			positive = true;

		for (int i = 0; i < b.length; i++){
			val[i] = b[i];
		}
	}
	public LargeInteger(LargeInteger other){
		this(other.val);
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}
	
	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/** 
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most 
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);
	
		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's 
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);
	
		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */
	public LargeInteger multiply(LargeInteger other) {
		LargeInteger larger, smaller;
		if (this.compareTo(other) > 0){
			larger = new LargeInteger(val);								//Keeps original objects intact during internal negations
			smaller = new LargeInteger(other.val);
		} else {
			larger = new LargeInteger(other.val);
			smaller = new LargeInteger(val);
		}
		int sign = 0;													//If both are negative sign will be 2 and product should be positive
		if (larger.isNegative()){										//If either are negative make them positive to do mults
			larger = larger.negate();
			sign++;
		}
		if (smaller.isNegative()){
			smaller = smaller.negate();
			sign++;
		}

		LargeInteger place;
		LargeInteger product = new LargeInteger(new byte[1]);			//starting size of large integer as 1, will increase when each new place is added
		for (int i = larger.length()-1; i >= 0; i--){
			place = new LargeInteger(new byte[1]);						//Start each new place at 1, increase size as needed
			for (int k = smaller.length()-1; k >= 0; k--){
				int mult = ((int)smaller.val[k] & 0xFF)*((int)larger.val[i] & 0xFF);	//Take only one byte from each
				byte[] hilo = {(byte)(mult>>>8), (byte)mult};			//Place the msb from the multiplication into first index, and lsb into second index
				
				LargeInteger temp = new LargeInteger(hilo);				//Create a large integer of the bytes to perform shift on 
				temp = temp.leftShift(smaller.length()-1-k);			//Shift by byte place currently multiplying into, difference between starting and current place
				
				if(!temp.positive)										//0 extend if negative
					temp.extend((byte)0);

				place = place.add(temp);								//Add current place 
			}
			product = product.add(place.leftShift(larger.length()-1-i));
		}

		if(!product.positive) 											//0 extend if negative
			product.extend((byte)0);
		if (sign == 1)													//If it should be negative, make it negative
			product = product.negate();
		return product;



	}

	/**
	 * Performs a left shift adding 0's to right. Does not remove bits from left, so makes numbers much bigger.
	 * Can handle shifts larger than 7
	 * 
	 * @param length to shift in bits
	 * @return the shifted LargeInteger
	 */
	public LargeInteger bigBitLeftShift(int length){
		if (length == 0)														//No shift needed
			return this;
		int byteShift = length / 8;	
		LargeInteger shifted = bitwiseLeftShift(length%8, false);				//Shift by small amount first
		return shifted.leftShift(byteShift);									//Shift by bytes for rest of the way		
	}

	/**
	 * Performs a left shift in BYTES on this by creating a new array with <code>length</code> amount of 0's on right
	 * and keeps all the bytes from original
	 * 
	 * @param length the number of 0's to shift onto right
	 * @return the shifted number
	 */
	public LargeInteger leftShift(int length){
		if(length <=0) 															//No shift
			return this;

		return new LargeInteger(Arrays.copyOf(val, val.length+length));			//Transfers all of val and adds length 0's to right side
	}
	
	/**
	 * Shifts left by up to 7 bits. For more than 7, use bigBitLeftShift
	 * 
	 * @param length Only use with a max length of 7
	 * @param one True if adding 1's to right of number, false to add 0's
	 * @return the shifted large integer
	 */
	public LargeInteger bitwiseLeftShift(int length, boolean one){
		int persistant = 0;														//Bits that cross bytes
		LargeInteger shifting = new LargeInteger(this);	

		for (int i = val.length-1; i>=0; i--){
			int holder = shifting.val[i];										//Current byte
			holder = (byte)((holder&0xff) >>> (8-length));						//Contains the bits that need to move to a new byte
			shifting.val[i] = (byte)(shifting.val[i] << length);				//Perform the left shift on byte i
			shifting.val[i] = (byte)(shifting.val[i] | (persistant&0xff));		//Add the bits that cross bytes to the right
			if (i == val.length-1 && one){										//If shift should be adding 1's
				byte[] masks = {0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f,0x7f,(byte)0xff};	//Add length amount of 1's to right by using a mask with that amount of 1's
				shifting.val[i] = (byte)(shifting.val[i] | masks[length]);
			}
			persistant = holder;												//Save the bits that need to cross bytes
		}
		byte[] temp = new byte[shifting.val.length+1];							//Add persistant bits to msb, but create a new byte for the msb so that bits arent removed
		for (int i = shifting.val.length-1; i >= 0; i--){						//by shift and a big number is created
			temp[i+1] = shifting.val[i];
		}
		temp[0] = (byte)persistant;
		
		return new LargeInteger(temp);
	}

	/**
	 * A sign extended right shift by 1 bit. Used for division
	 * 
	 * @return this LargeInteger shifted right by 1
	 */
	public LargeInteger rightShiftOne(){
		byte[] result = new byte[val.length];
		int persistant = 0;
		for (int i = 0; i < val.length; i++){
			int holder = val[i];
			holder = (byte)((holder&1)<<7);
			result[i] = (byte)(((int)(val[i]&0xff) >>> 1));
			result[i] = (byte)(result[i] | (persistant&0xff));
			
			persistant = holder;
		}
		return new LargeInteger(result);
	}
	/**
	 * Calculates the number of bits of sign padding are on this LargeInteger
	 * 
	 * @return the number of bits the sign extension occupies
	 */
	private int extendSize(){
		if (this.isZero())
			return 0;
		int signBit = (val[0] >> 7) & 1;									
		int k, size = 0;
		for (k = 0; k < val.length && (byte)(-signBit) == val[k]; k++);		//All bits in byte match sign bit, for loop finds the number of bytes which are just sign extending, no body needed
		size = (k)*8;														//Do 1 multiply instead of multiple adds while looping

		if (k == val.length)
			return size;

		if ((val[k] >>7 &1) != signBit){									//case 0xff7f
			size--;
		}

		int i = 7;															//Start at msb of byte and at byte after first loop
		while (((val[k]>>i &1) == (val[k]>>(i-1) &1)) && i >=1){			//If the current bit == the next bit it is still part of the extension
			i--;
			size++;
		}

		if (signBit == 0)													//case 0x07, wont count 0 in 7(0111) in loop so add that case
			size++;
		
		return size;
	}



	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	public LargeInteger[] XGCD(LargeInteger other)
	{	
		LargeInteger larger, smaller;										//Start the algorithm with a as the larger and b as the smaller LargeInteger
		if (this.compareTo(other) > 0){
			larger = new LargeInteger(val);								
			smaller = new LargeInteger(other.val);
		} else {
			larger = new LargeInteger(other.val);
			smaller = new LargeInteger(val);
		}
		LargeInteger[] temp = runXGCD(larger, smaller);
		temp[0] = temp[0].trim();											//Remove excess bits that appeared from divide
		temp[1] = temp[1].trim();
		temp[2] = temp[2].trim();
		return temp;
	}
	private LargeInteger[] runXGCD(LargeInteger a, LargeInteger b){
		if(b.isZero()) 														//Base case b is 0 so set d as a, s as 1 and t as 0
			return new LargeInteger[]{ a, new LargeInteger(ONE), new LargeInteger(ZERO)};

		LargeInteger[] temp = a.divide(b);									//Set previous values
		LargeInteger a_div_b = temp[0];
		LargeInteger a_mod_b = temp[1];
		LargeInteger[] temp2 = runXGCD(b,a_mod_b);							//Recurse to base case
		LargeInteger s = temp2[1];											
		LargeInteger t = temp2[2];
		temp2[1] = t;														//Calculate new values
		temp2[2] = s.subtract(a_div_b.multiply(t));
		
		return temp2; 														//Return the new values

	}
	/**
	 * Determines if this is 0. Checks if all bytes are 0, stopping if a non zero one is found
	 * @return true if this is 0
	 */
	public boolean isZero() {
		
		for (int i = 0; i < this.length(); i++){
			if (val[i] != 0)
				return false;
		}
		return true;
	}
	/**
	 * Compute the result of raising this to the power of y mod n
	 * <p>
	 * Computed as a bottom up approach in binary, tried to do it recursively first but was only working for positives
	 * @param y exponent to raise this to
	 * @param n modulus value to use
	 * @return this^y mod n
	 */
	public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
		LargeInteger result = new LargeInteger(ONE);
		for (int i = 0; i < val.length; i++){									//For each byte	starting at MSB
			for (int k = 7; k >= 0; k--){										//For each bit starting at MSB
				result = result.multiply(result).mod(n);						//result = result^2 % n (ans = ans^2 in slides)
				if (((val[i] >> k) & 1) == 1)
					result = result.multiply(this).mod(n);						//result = result*this %n (ans = ans*x in slides)
			}
		}
		return result;
	}

	/**
	 * 	ONLY WORKS FOR POSITIVE VALUES... SCRAPPED
	 * 
	 * Recursive method for finding Modular Exponentiation
	 * @param base the base being raised 
	 * @param y the exponent
	 * @param n the modulus
	 * @return the LargeInteger result of base^y %m
	 */
/*	private LargeInteger runModularExp(LargeInteger base, LargeInteger y, LargeInteger n){
		base = base.mod(n);
		if (y.isZero())
			return new LargeInteger(ONE);
		else if (y.equals(new LargeInteger(ONE)))
			return base;
		else if (y.mod(new LargeInteger(TWO)).isZero())
			return runModularExp(base.multiply(base).mod(n), y.divide(new LargeInteger(TWO))[0], n);
		else {
			return base.multiply(runModularExp(base, y.subtract(new LargeInteger(ONE)),n)).mod(n);
		}
	}
	*/

	/**
	 * 
	 * @param other The divisor
	 * @return at index 0 is the result of this/other. At index 1 is the remainder(modulus) of this/other (this % other)
	 */
	public LargeInteger[] divide(LargeInteger other){
		LargeInteger[] result = new LargeInteger[2];					//Return the quotient in [0] and remander in [1]
		LargeInteger dividend = new LargeInteger(this);
		LargeInteger divisor = new LargeInteger(other);
		LargeInteger quotent = new LargeInteger(ZERO), remainder;
		
		int sign = 0;													//If both are negative sign will be 2 and product should be positive
		if (dividend.isNegative()){										//If either are negative make them positive to do mults
			dividend = dividend.negate();
			sign++;
		}
		if (divisor.isNegative()){
			divisor = divisor.negate();
			sign++;
		}
		if (dividend.equals(divisor)){									//Dividing two equal numbers is 1
			LargeInteger one = new LargeInteger(ONE);
			if (sign == 1)
				one = one.negate();
			return new LargeInteger[] {one, new LargeInteger(ZERO)};
		}
		if (divisor.compareTo(dividend) > 0){							//Dividing a smaller number by a bigger number
			if (!this.positive)											//If the dividend was negated switch it back
				dividend = dividend.negate();
			return new LargeInteger[] {new LargeInteger(ZERO), dividend};
		}
		
		int length = dividend.length()*8 -1;							//Length in bits
		divisor = divisor.bigBitLeftShift(length);						//Move to starting position
		
		remainder = dividend;											//Initialize
		quotent = new LargeInteger(ZERO);
		int i = 0;
		while (i <= length){											//While there are bits to process
			LargeInteger difference = remainder.subtract(divisor);		//Current part of dividend-divisor
			if (difference.isNegative()){								//Add 0 to right of quotent
				quotent = quotent.bitwiseLeftShift(1, false);
			}else {
				quotent = quotent.bitwiseLeftShift(1, true);			//Add 1 to right of quotent
				remainder = difference;
			}
			divisor = divisor.rightShiftOne();							//Shift the divisor to next position
			i++;														//Update amount processed
		}
		
		if (sign ==1){													//If either is negative result is negative
			quotent = quotent.negate();
		}
		if (this.isNegative()){											//If the dividend is negative mod is negative. Cant use dividend object because it was negated, check original this object
			remainder = remainder.negate();
		}
		result[0] = quotent.trim();										//Remove extra 0's
		result[1] = remainder.trim();
		return result;
	} 

	/**
	 * Computes the modulo of this by doing a division, which returns mod implicitly
	 * @param m the modulus to take 
	 * @return the value of this % m
	 */
	public LargeInteger mod(LargeInteger m){
		return this.divide(m)[1];
	}

	/**
	 * Removes extra padding bytes
	 * @return This LargeInteger without sign extension padding
	 */
	private LargeInteger trim(){
		int trimSize = extendSize()/8;										//Find size of extension in bytes
		if (trimSize == val.length)											//If the entire array is an extension, make it one byte
			return new LargeInteger(new byte[]{val[0]});
		if (trimSize-1 < 0)													//Dont go out of bounds
			return new LargeInteger(Arrays.copyOfRange(val, trimSize, val.length));
		return new LargeInteger(Arrays.copyOfRange(val, trimSize-1, val.length));	//Create a new array of this array starting after padding
	}

	/**
	* 	toString method using BigInteger's toString method
	*/
	public String toString()
	{
	 	return (new BigInteger(val)).toString();
	}

	/**
	 * Compares two LargeIntegers using subtraction
	 * @param o the LargeInteger to compare to
	 * @return -1 if this < other 1 if this > other 0 if equal
	 */
	@Override
	public int compareTo(LargeInteger o) {
		LargeInteger greater = this.subtract(o);			//Positive if greater, negative if less
		if (greater.isZero())
			return 0;
		else if (greater.positive)
			return 1;
		else
			return -1;
	}
	
}
