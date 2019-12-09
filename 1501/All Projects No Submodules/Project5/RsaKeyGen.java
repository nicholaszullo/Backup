import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
/**
 * Generate RSA Keys using LargeInteger and store in a class and file. Probably works right
 */
public class RsaKeyGen{
    public static void main(String[] args) throws Exception{
        final byte[] ONE = { (byte) 1 };
        Random rand = new Random();
        LargeInteger p, q, n,phi,e,d;
        p = new LargeInteger(512, rand);                                                        //p is a random 512 bit number
        q = new LargeInteger(512, rand);                                                        //q is a random 512 bit number 
        n = p.multiply(q);                                                                      //n is p*q
        phi = (p.subtract(new LargeInteger(ONE))).multiply(q.subtract(new LargeInteger(ONE)));  //phi(n) is (p-1)(q-1)
        e = new LargeInteger(phi.length(), rand);                                               //e is a random such that it shares no factors with phi and is 1<e<phi
        LargeInteger[] holder = phi.XGCD(e);
        while (holder[0].compareTo(new LargeInteger(ONE)) != 0){                                //Loop until a value of e with no shared factors is found
            e = new LargeInteger(phi.length(), rand);
            holder = phi.XGCD(e);
        }
        d = holder[2].mod(phi);                                                                 //XGCD(phi, e) = 1 = phi*x + e*d 
        PubKey pub = new PubKey(e, n);                                                          //Store the pub and priv keys in a file and in classes
        PrivKey priv = new PrivKey(d, n);
        FileOutputStream write  = new FileOutputStream("pubkey.rsa");
        ObjectOutputStream writer = new ObjectOutputStream(write);
        writer.writeObject(pub);
        writer.close();

        write = new FileOutputStream("privkey.rsa");
        writer = new ObjectOutputStream(write);
        writer.writeObject(priv); 
        writer.close();
    }




}