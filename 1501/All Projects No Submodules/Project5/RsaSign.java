import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Sign or verify an RSA file. Not Working
 */
public class RsaSign {
    public static void main(String[] args) throws Exception {
        if (args.length != 2)                                                                   //Check input format
            throw new IllegalArgumentException("Invalid Command Line Input!"); 
        
        if (args[0].charAt(0) == 'v')                                                           //Verify mode
            verify(args[1]);
        else if (args[0].charAt(0) == 's')                                                      //Sign mode
            sign(args[1]);
        else
            throw new IllegalArgumentException("Invalid Command Line Input!");


    }
    private static void sign(String file){
        LargeInteger hash = HashEx.hash(file);                                                  //Hash the file
        try {
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream("privkey.rsa"));
            PrivKey privKey = (PrivKey)reader.readObject();                                     //Get the key values
            LargeInteger sign = hash.modularExp(privKey.d, privKey.n);                          //Sign the file
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file+".sig"));
            writer.writeObject(sign);                                                           //Write the signed file to the file with a .sig extension 
            reader.close();                                                                     //Close reader and writer
            writer.close();
        } catch (IOException e) {                                                               //Cannot read the file 
            System.out.println("No file found!");
            System.exit(0);
        } catch (ClassNotFoundException e) {                                                    //Cannot make an object
            System.out.println("Invalid RSA File!");
            System.exit(0);
        }
    }
    private static void verify(String file){
        LargeInteger hash = HashEx.hash(file);                                                  //Hash the file
        try {
            ObjectInputStream readerKey = new ObjectInputStream(new FileInputStream("pubkey.rsa"));
            PubKey pubKey = (PubKey)readerKey.readObject();                                     //Get the key values
            ObjectInputStream readerFile = new ObjectInputStream(new FileInputStream(file+".sig"));
            LargeInteger signedFile = (LargeInteger)readerFile.readObject();                    //Read the signed file
            LargeInteger verify = signedFile.modularExp(pubKey.e, pubKey.n);                    //Perform verification check on file
            if (!verify.equals(hash))
                System.out.println("Valid RSA File!");
            else 
                System.out.println("WARNING: INVALID SIGNATURE");   
            readerKey.close();                                                                  //Close readers
            readerFile.close();
        } catch (IOException e) {                                                               //Cannot read file
            System.out.println("No file found!");
            System.exit(0);
        } catch (ClassNotFoundException e) {                                                    //Cannot Create Object
            System.out.println("Invalid RSA File!");
            System.exit(0);
        }
        


    }

}