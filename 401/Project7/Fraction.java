/* Fraction.java  A class (data type) definition file
   This file just defines what a Fraction is
   This file is NOT a program
   ** data members are PRIVATE
   ** method members are PUBLIC
*/
public class Fraction
{
	// DO NOT MODIFY THE CODE BELOW - USE AS GIVEN

	private int numer;
	private int denom;

	// ACCESSORS
	public int getNumer()
	{
		return numer;
	}
	public int getDenom()
	{
		return denom;
	}
	public String toString()
	{
		return numer + "/" + denom;
	}

	// MUTATORS
	public void setNumer( int n )
	{
		numer = n;
	}
	public void setDenom( int d )
	{
		if (d!=0)
			denom=d;
		else
		{
			System.out.println("DENOM CANNOT = 0");
			System.exit(0);							// error msg OR exception OR exit etc.
		}
	}

	// DEFAULT CONSTRUCTOR - no args passed in
	public Fraction(  )
	{
		this( 0, 1 ); // "this" means call a fellow constructor
	}

	// 1 arg CONSTRUCTOR - 1 arg passed in
	// assume user wants whole number
	public Fraction( int n )
	{
		this( n, 1 ); // "this" means call a fellow constructor
	}

	// FULL CONSTRUCTOR - an arg for each class data member
	public Fraction( int n, int d )
	{
		int gcd = gcd(n,d); 
		setNumer(n/gcd); 		
		setDenom(d/gcd);
	}

	// COPY CONSTRUCTOR - takes ref to some already initialized Fraction object
	public Fraction( Fraction other )
	{
		this( other.numer, other.denom ); // call my full C'Tor with other Fraction's data
	}
	
	public Fraction add(Fraction other){
		return new Fraction(((other.numer*denom) + (numer*other.denom)),(other.denom*denom));
	}
	
	public Fraction subtract(Fraction other){
		return new Fraction(((numer*other.denom)- (other.numer*denom)),(other.denom*denom));
	}

	public Fraction multiply(Fraction other){
		return new Fraction(other.numer*numer, other.denom*denom);
	}
	
	public Fraction divide(Fraction other){
		return new Fraction(numer*other.denom,denom*other.numer);
	}
	
	public Fraction reciprocal(){
		return new Fraction(denom,numer);
	}
	
	public int gcd( int n, int d)
	{
		if (n == d)					//Base case, gcd
			return d;
		if (n == 0 || d == 0)		//Base case, no gcd
			return 1;
		
		if (n > d) 					//Work down to when n == d
            return gcd(n-d, d); 
        return gcd(n, d-n); 
		
	}
	
	
	/* WORKS BUT NOT RECURSIVE
	private int gcd( int n, int d)
	{
		for (int i = n/2; i > 1; i--){
			if (n%i == 0 && d%i == 0)
				return i;
		}
		return 1;
	}
	*/
}// EOF
