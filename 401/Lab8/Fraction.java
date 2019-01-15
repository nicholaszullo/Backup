public class Fraction implements Comparable<Fraction>
{
	private int numer;
	private int denom;

	// ACCESSORS (SETTERS)
	public int getNumer()
	{	return numer;
	}
	public int getDenom()
	{	return denom;
	}
	// MUTATORS (GETTERS)
	public void setNumer( int n )
	{	numer = n;
	}
	public void setDenom( int d )
	{
		if (d==0) { System.out.println("Can't have 0 in denom"); System.exit(0); }
		else if (d < 0 && numer < 0){				//If both are negative, it is positive
			setNumer(-1*numer);
			denom = -1*d;
		}
		else if (d < 0 && numer > 0){				//If the denominator is negative, put the negative sign in the numerator 
			setNumer(numer*-1);
			denom = -1*d;
		} 
		else denom = d;
	}
	// FULL CONSTRUCTOR - an arg for each class data member
	public Fraction( int n, int d )
	{	int gcd = gcd( n, d );
		setNumer(n/gcd);
		setDenom(d/gcd);
	}
	// COPY CONSTRUCTOR - takes ref to some already initialized Comparable<Fraction> object
	public Fraction( Fraction other )
	{
		this( other.getNumer(), other.getDenom() ); // call my full C'Tor with other Fraction's data
	}

	//  GIVING YOU A WORKING (ITERATIVE) GCD J.I.C. YOU NEVER GOT YOURS FROM PROJ 7 TO WORK.
	private int gcd( int n, int d )
	{	int gcd = n<d ? n : d;    // same as => if (n<d) gcd=n; else gcd=d;
		while( gcd > 0 )
		{	if (n%gcd==0 && d%gcd==0) 
				return gcd; 
			else --gcd;
		}
		return 1; // they were co-prime no GCD exceopt 1 :(
	}
	
	
	public Fraction subtract(Fraction other){
		return new Fraction(((numer*other.denom)- (other.numer*denom)),(other.denom*denom));
	}
	

	public int compareTo( Fraction other )		//Works with positive and negative numbers now with changes to setDenom
	{	
		Fraction dif = subtract(other);
		if (dif.numer == 0)
			return 0;
		else if (dif.numer > 0)
			return 1;
		else
			return -1;

	}
	public String toString() // USE AS IS. DO NOT DELETE OR MODIFY
	{
		return getNumer() +  "/" + getDenom() + "\t=" +  + ((double)getNumer()/(double)getDenom()); 
	}
}// EOF

