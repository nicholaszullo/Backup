/*************************************************************************
 *  
 * njz12 - Nicholas Zullo CS 1501 Project 2 due 10/13/19
 * 
 *  Scrapped original code and rewrote. Original was too janky and wouldnt expand a properly(i think) compressed all.tar in monitor mode
 *  Originally switched expanding to use a hashMap because we discussed that in class but now realized array works fine and switched back
 *  Now doesnt reimplement variable length codewords for each mode, more code reuse. Much less janky. Now expands with i == L instead of i == L-1.
 *  Overall better :) 
 * 
 *  Compilation: javac MyLZW.java 
 *  Execution: java MyLZW - n/r/m < input.txt (compress) 
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;                                       // number of input chars
    private static int L = 512;                                             // number of codewords = 2^W
    private static int W = 9;                                               // codeword width

    public static void compress(char mode) { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)                                         //Initialize code for all characters in alphabet
            st.put("" + (char) i, i);                                       //"" + (char) converts char to string
        int code = R+1;                                                     // R is codeword for EOF
        int compGenerated = 0;                                              //Compressed data created by algorithm, Increments by W bits written
        int uncompProcessed = 0;                                            //Uncompressed data processed, increments by number of bits in string used
        double currRatio = 0.0;                                             //Current rate of uncomp/comp
        double prevRatio = 0.0;                                             //Rate of uncomp/comp when codebook last filled
        BinaryStdOut.write(mode);                                           //Add the mode to be the first char in file so expand can read from file to get mode
        
        while (input.length() > 0) {                                        //While there are still characters in the uncompressed file to process
            String s = st.longestPrefixOf(input);                           // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);                               // Print s's encoding.
            int t = s.length();                                             //length of current string
            compGenerated += W;                                             //increment
            uncompProcessed += t*8;
                                                                            //t < input.length needed because on last pattern in file t+1 will go out of bounds on substring. still add to file (above) but no need to add to codebook
            if (t < input.length() && code < L)                             //If there is room for the codeword, add it
            {
                st.put(input.substring(0, t+1), code++);                    //Increment code after each insertion of new codeword
            }
            else if (W < 16)                                                //If unable to insert and can increase width of codeword
            {                                                               //Do nothing mode will stop here
                W++;                                                        //Increment
                L *= 2;
                st.put(input.substring(0, t+1), code++);                    //Add codeword to list now that room is added
            }
            else if (t < input.length() && mode == 'r')                     //If codebook is full and reset mode activated
            {   
                W = 9;                                                      //Reset to default W and max codewords
                L = 512;
                st = new TST<Integer>();                                    //Remake code of alphabet
                for (int i = 0; i < R; i++)
                    st.put("" + (char) i, i);
                code = R+1;                                                 
                st.put(input.substring(0, t+1), code++);                    //Add current word to new list
            }
            if (t < input.length() && mode == 'm')                          //Need to check if in monitor mode on each character, not just when full, so new if 
            {
                if (prevRatio == 0)                                         //If the previous ratio has not been set, set it
                {                                                       
                    prevRatio = currRatio;
                }
                currRatio = (double) uncompProcessed/compGenerated;         //Update current ratio

                if (prevRatio/currRatio > 1.1)                              //If it was compressing better before, reset and make new codebook
                {
                    W = 9;
                    L = 512;
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;                                             
                    st.put(input.substring(0, t+1), code++);
                    prevRatio = 0;                                          //Previous ratio resets 
                }
                
            }

        
            input = input.substring(t);                                     // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[65536];
        int i;                                                              // next available codeword value
        L = 512;
        W = 9;  
        int uncompGenerated = 0;
        int compProcessed = 0;
        double currRatio = 0.0;
        double prevRatio = 0.0;

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                                                       // (unused) lookahead for EOF

        int mode = BinaryStdIn.readChar();                                  //Find mode from first value of file
        char modec = st[mode].charAt(0);

        int codeword = BinaryStdIn.readInt(W);                              //First code
        if (codeword == R) return;                                          // expanded message is empty string
        String val = st[codeword];                                          //Find codeword from code

        while (true) {  
            uncompGenerated += val.length()*8;                              //Increment trackers at top because compression did
            compProcessed += W;

            if (i == L)                                                     //If current size of codebook is full
            {
                if (W < 16)                                                 //If can still expand
                {
                    W++;                                                    //Increase values of codebook
                    L *=2;  
                }                                                           //Do nothing mode stops here
                else if (modec == 'r')                                      //If in reset mode and cant expand
                {
                    W = 9;                                                  //Reset to default values
                    L = 512;
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";                                           // (unused) lookahead for EOF
                }
            }
            if (modec == 'm')                                               //If in monitor mode, need to check ratio each time. not just when i == L
            {
                if (prevRatio == 0)                                         //set prevRatio only once
                {
                    prevRatio = currRatio;
                }
                currRatio = (double) uncompGenerated/compProcessed;         //Set current ratio
                if (prevRatio/currRatio > 1.1)                              //If not compressing well, Reset codebook
                {
                    W = 9;
                    L = 512;
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";                                           // (unused) lookahead for EOF
                    prevRatio = 0;                                          //Reset prevRatio 
                }   
            }
            

            BinaryStdOut.write(val);                                        //Write values to file after any adjustments have been made based on mode and size of codebook
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;                                                        //Val is the previous pattern, s is current pattern
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if (args[0].charAt(0) == '-')
        {
            compress(args[1].charAt(0));
        }
        else if (args[0].charAt(0) == '+') 
        {
            expand();
        }  
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
