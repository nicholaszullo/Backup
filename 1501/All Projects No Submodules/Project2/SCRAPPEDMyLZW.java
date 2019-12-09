import java.util.HashMap;

/*************************************************************************
 * 
 * njz12 - Nicholas Zullo CS 1501 Project 2 due 10/13/19
 * 
 * Compilation: javac MyLZW.java Execution: java MyLZW - n/r/m < input.txt (compress)
 * Execution: java MyLZW + < input.txt (expand) Dependencies: BinaryIn.java
 * BinaryOut.java
 *
 * Compress or expand binary input from standard input using LZW.
 *
 * WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING METHOD TAKES
 * TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED SUBSTRING (INSTEAD OF
 * CONSTANT SPACE AND TIME AS IN EARLIER IMPLEMENTATIONS).
 *
 * See <a href =
 * "http://java-performance.info/changes-to-string-java-1-7-0_06/">this
 * article</a> for more details.
 *
 *************************************************************************/

public class SCRAPPEDMyLZW {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9; // codeword width

    public static void compress(char mode) {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        int uncompProcessed = 0;
        int compGenerated = 0;
        double currRatio = 0.0;
        double prevRatio = 0.0;


        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R + 1; // R is codeword for EOF
        BinaryStdOut.write(mode);
        if (mode == 'n')
        {
            while (input.length() > 0) 
            {
                String s = st.longestPrefixOf(input); // Find max prefix match s.
                BinaryStdOut.write(st.get(s), W); // Print s's encoding.
                int t = s.length();

                if (t < input.length() && code < L)    // Add s to symbol table.
                {
                    st.put(input.substring(0, t + 1), code++);
                }
                else if (t < input.length() && W < 16)
                {
                    W++;
                    L *= 2;
                    st.put(input.substring(0, t + 1), code++);
                }
                input = input.substring(t);            // Scan past s in input.
            }
        } else if (mode == 'r')
        {
            while (input.length() > 0) 
            {
                String s = st.longestPrefixOf(input); // Find max prefix match s.
                BinaryStdOut.write(st.get(s), W); // Print s's encoding.
                int t = s.length();

                if (t < input.length() && code < L)    // Add s to symbol table.
                {
                    st.put(input.substring(0, t + 1), code++);
                }
                else if (t < input.length() && W < 16)
                {
                    W++;
                    L *= 2;
                    st.put(input.substring(0, t + 1), code++);
                }
                else if (t != input.length())                   //Case codebook filled or end of input, so dont reset if at the end
                {
                    System.err.println("Reseting..." + code);
                    W = 9;
                    L = 512;
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++){            
                        st.put("" + (char) i, i);
                    }
                    s = st.longestPrefixOf(input);
                    t = s.length();
                    code = R+1;
                    st.put(input.substring(0, t + 1), code++);

                }
                input = input.substring(t);            // Scan past s in input.
            }
        }else if (mode == 'm')              //When to update prevRatio???? figure out then fix compression
        {
            boolean dothis = true;
            while (input.length() > 0) 
            {
               
                String s = st.longestPrefixOf(input); // Find max prefix match s.
                BinaryStdOut.write(st.get(s), W); // Print s's encoding.
                int t = s.length();
                currRatio = (double)uncompProcessed/compGenerated;
                
                if (t < input.length() && code < L)    // Add s to symbol table.
                {
                    st.put(input.substring(0, t + 1), code++);
                }
                else if (t < input.length() && W < 16)
                {
                    System.err.println("upping codebook " + code);
                    W++;
                    L *= 2;
                    st.put(input.substring(0, t + 1), code++);
                }
                else if (dothis && W == 16 && code == L)
                {
                    System.err.println("codebook filled");
                    prevRatio = currRatio;
                    dothis = false;

                } 
                
                if ((prevRatio/currRatio) > 1.1)
                {
                    System.err.println("Reseting..." + code + " " + prevRatio/currRatio + " " + compGenerated + " " + uncompProcessed);
                    W = 9;
                    L = 512;
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++){            
                        st.put("" + (char) i, i);
                    }
                    s = st.longestPrefixOf(input);
                    t = s.length();
                    code = R+1;
                    st.put(input.substring(0, t + 1), code++);
                    dothis = true;
                    prevRatio = 0;
                    currRatio = 0;
                } 
                uncompProcessed += s.length()*8;
                compGenerated += W;
              
                
                input = input.substring(t);            // Scan past s in input.
            }
        }


        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        L = 512;
        W = 9;
        HashMap<Integer, String> st = new HashMap<Integer, String>();
        int uncompGenerated = 0;
        int compProcessed = 0;
        double currRatio = 0.0;
        double prevRatio = 0.0;

        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st.put(i, ""+ (char) i);
        st.put(i++, "");                       // (unused) lookahead for EOF

        int mode = BinaryStdIn.readChar();
        char modec = st.get(mode).charAt(0);

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) 
        {
            return;           // expanded message is empty string
        }
        String val = st.get(codeword);

        if (modec == 'n')
        {
            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                //System.err.println("CODE IS " + codeword + " i IS " + i + " L IS " + L);
                if (codeword == R) 
                {
                    break;
                }
                String s = st.get(codeword);
                if (i == codeword)
                {
                    s = val + val.charAt(0);   // special case hack
                }
                if (i < L-1)
                {
                    st.put(i++, val + s.charAt(0));
                }
                else if (W < 16)
                {
                    W++;
                    L *=2;
                    st.put(i++, val + s.charAt(0));
                }
                
                val = s;
            }
        } else if (modec == 'r')
        {
            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                //System.err.println("CODE IS " + codeword + " i IS " + i + " L IS " + L);
                if (codeword == R) 
                {
                    break;
                }
                String s = st.get(codeword);        //Val is previous pattern s is current pattern
                if (i == codeword)
                {
                    s = val + val.charAt(0);   // special case hack
                }
                if (i < L-1)
                {
                    st.put(i++, val + s.charAt(0));
                }
                else if (W < 16)
                {
                    W++;
                    L *=2;
                    st.put(i++, val + s.charAt(0));
                }
                else 
                {
                    System.err.println("Reseting..."  + i);
                    W = 9;
                    L = 512;
                    st = new HashMap<Integer, String>();
                    for (i = 0; i < R; i++){            
                        st.put(i, "" + (char) i);
                    }
                    st.put(i++, "");
                }
                val = s;
            }
        }else if (modec == 'm')
        {
            boolean dothis = true;
            while (true)
            {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                currRatio = (double) uncompGenerated/compProcessed;

                if (codeword == R) 
                {
                    break;
                }
                
                String s = st.get(codeword);
                
                if (i == codeword)
                {
                    s = val + val.charAt(0);   // special case hack
                }
                
                if (i < L-1)
                {
                    st.put(i++, val + s.charAt(0));
                }
                else if (W < 16)
                {
                    System.err.println("upping codebook " + i);
                    
                    W++;
                    L *=2;
                    st.put(i++, val + s.charAt(0));
                }
                else if (dothis && W == 16)
                {
                    System.err.println("codebook filled");
                    prevRatio = currRatio;
                    dothis = false;
                }                
                
                if ((prevRatio/currRatio) > 1.1)
                {
                    System.err.println("Reseting..." + i +" " + prevRatio/currRatio + " " + compProcessed + " " + uncompGenerated );
                    W = 9;
                    L = 512;
                    st = new HashMap<Integer, String>();
                    for (i = 0; i < R; i++){            
                        st.put(i, "" + (char) i);
                    }
                    st.put(i++, "");
                    dothis = true;
                    prevRatio = 0;
                    currRatio = 0;
                }
                
                uncompGenerated += s.length()*8;
                compProcessed += W;
                val = s;
            }
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
