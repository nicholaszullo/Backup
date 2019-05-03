
//import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

//import static org.junit.Assert.*;
/*
	I could not get JUnit to work on my computer so I commented out the JUnit code and tested 
	my code using print statements and manually comparing the data	

*/

public class AssemblerTest {
        private static int MARS_TEXT_SEGMENT_START = 0x00400000;

        private static void testHelper(List<Instruction> input, Instruction[] expectedP1, Instruction[] expectedP2, Integer[] expectedP3) {
            // Phase 1
            List<Instruction> tals = Phase1.mal_to_tal(input);
        //    assertArrayEquals(expectedP1, tals.toArray());
		
			System.out.println("PHASE 1");
			for (Instruction inst : tals)						//Print the results of phase 1 and then print the expected results
				System.out.println(inst);
			System.out.println();
			for (int i = 0; i < expectedP1.length; i++)
				System.out.println(expectedP1[i]);
			
            // Phase 2
            List<Instruction> resolved_tals = Phase2.resolve_addresses(tals, MARS_TEXT_SEGMENT_START);
        //    assertArrayEquals(expectedP2, resolved_tals.toArray());
		
			System.out.println("\nPHASE 2");						//Seperate the phases
			for (Instruction inst : resolved_tals)			//Print the results of phase 2 and then the expected results
				System.out.println(inst);
			System.out.println();
			for (int i = 0; i < expectedP1.length; i++)
				System.out.println(expectedP2[i]);
			
			System.out.println("\nPHASE 3");						//Seperate the phases
            // Phase 3
            List<Integer> translated = Phase3.translate_instructions(resolved_tals);
			for (Integer num : translated)					//Print the result of phase 3 and then the expected results
				System.out.println(num);
			System.out.println();
			for (int i = 0; i < expectedP1.length; i++)
				System.out.println(expectedP3[i]);
        //    assertArrayEquals(expectedP3, translated.toArray());
        }

    //    @Test
        public void test1() {
            // test 1
            List<Instruction> input = new LinkedList<Instruction>();
            // label1: addu $t0, $zero, $zero
            input.add(new Instruction(2, 8, 0, 0, 0, 0, 0, 1, 0));
            // addu $s0, $s7, $t4
            input.add(new Instruction(2,16,23,12,0,0,0,0,0));
            // blt  $s0,$t0,label1
            input.add(new Instruction(100,0,16,8,0,0,0,0,1));
            // addiu $s1,$s2,0xF00000
            input.add(new Instruction(1, 0, 18, 17, 0xF00000, 0, 0, 0, 0));

            // Phase 1
            Instruction[] phase1_expected = {
                    new Instruction(2,8,0,0,0,0,0,1,0), // label1: addu $t0, $zero, $zero
                    new Instruction(2, 16, 23,12,0,0,0,0,0), // addu $s0, $s7, $t4
                    new Instruction(8, 1,16,8,0,0,0,0,0),  // slt $at,$s0,$t0
                    new Instruction(6, 0,1,0,0,0,0,0,1),     // bne $at,$zero,label1
                    new Instruction(9, 0,0,1,0x00F0,0,0,0,0), // lui $at, 0x00F0
                    new Instruction(10,0,1,1,0x0000,0,0,0,0), // ori $at, $at 0x0000
                    new Instruction(2,17,18,1,0,0,0,0,0), // addu $s1,$s2,$at
            };

            // Phase 2
            Instruction[] phase2_expected = {
                    new Instruction(2,8,0,0,0,0,0,1,0),
                    new Instruction(2,16,23,12,0,0,0,0,0),
                    new Instruction(8,1,16,8,0,0,0,0,0),
                    new Instruction(6,0,1,0,0xfffffffc,0,0,0,1),
                    new Instruction(9,0,0,1,0x00F0,0,0,0,0),
                    new Instruction(10,0,1,1,0x0000,0,0,0,0),
                    new Instruction(2,17,18,1,0,0,0,0,0)
            };

            // Phase 3
            Integer[] phase3_expected = {
                    // HINT: to get these, type the input program into MARS, assemble, and copy the binary values into your test case
                    0x00004021,
                    0x02ec8021,
                    0x0208082a,
                    0x1420fffc,
                    0x3c0100f0,
                    0x34210000,
                    0x02418821
            };


            testHelper(input,
                    phase1_expected,
                    phase2_expected,
                    phase3_expected);
        }

    //    @Test
	//Tests every supported instruction, tests when addiu and ori is and isnt a psuedo
	//Tests forward and backward jumping, tests jumping to the same label from 2 different branches
        public void test2() {
            
			List<Instruction> input = new LinkedList<Instruction>();		
            //Label2: addi t1,t1,0xf2f3										
			input.add(new Instruction(1,0,9,9,0xf2f3,0,0,2,0));				
			//bge t1,$0, label1
			input.add(new Instruction(101,0,9,0,0,0,0,0,1));
			//ori s1, s0, 0xf3f3
			input.add(new Instruction(10,0,16,17,0xf3f3,0,0,0,0));
			//label1: addu s2,s1,t1
			input.add(new Instruction(2,18,17,9,0,0,0,1,0));
			//addiu s3,s2, 0xff2f4
			input.add(new Instruction(1,0,18,19,0xff2f4,0,0,0,0));
			//or s4,s3,t1
			input.add(new Instruction(3,20,19,9,0,0,0,0,0));
			//beq s3,s1,label2
			input.add(new Instruction(5,0,19,17,0,0,0,0,2));
			//addiu s1,s2, 0x33f33f
			input.add(new Instruction(1,0,18,17,0x33f33f,0,0,0,0));
			// blt  $s0,$t0,label1
			input.add(new Instruction(100,0,16,8,0,0,0,0,1));
			//ori s1,s0,0xf3f3f4
			input.add(new Instruction(10,0,16,17,0xf3f3f4,0,0,0,0));

            // Phase 1
            Instruction[] phase1_expected = {
                    new Instruction(1,0,9,9,0xf2f3,0,0,2,0),	//Label2: addi t1,t1, 0xf2f3
					new Instruction(8,1,9,0,0,0,0,0,0),			//slt at, t1, $0
					new Instruction(5,0,1,0,0,0,0,0,2),			//beq at, $0, label2
					new Instruction(10,0,16,17,0xf3f3,0,0,0,0), //ori s1, s0, 0xf3f3
					new Instruction(2,18,17,9,0,0,0,1,0),		//label1: addu s2,s1,t1
					new Instruction(9,0,0,1,0x000f,0,0,0,0),	//lui at, 0xf
					new Instruction(10,0,1,1,0xf2f4,0,0,0,0),	//ori at, at, 0xf2f4
					new Instruction(2,19,18,1,0,0,0,0,0),		//addu s3,s2,at
					new Instruction(3,20,19,9,0,0,0,0,0),		//or s4,s3,t1
					new Instruction(5,0,19,17,0,0,0,0,2),		//beq s3,s1,label2
					new Instruction(9,0,0,1,0x33,0,0,0,0),		//lui at,ox33
					new Instruction(10,0,1,1,0xf33f,0,0,0,0),	//ori at,at,0xf33f
					new Instruction(2,18,17,1,0,0,0,0,0),		//addu s1,s2,at
					new Instruction(8,1,16,8,0,0,0,0,0),  		//slt at,s0,t0
                    new Instruction(6,0,1,0,0,0,0,0,1),    		//bne at,$0,label1
					new Instruction(9,0,0,1,0xf3,0,0,0,0),		//lui at, 0xf3
					new Instruction(10,0,1,1,0xf3f4,0,0,0,0),	//ori at,at, 0xf3f4
					new Instruction(3,17,16,1,0,0,0,0,0)		//or s1,s0,at
					
            };

            // Phase 2
            Instruction[] phase2_expected = {
                    new Instruction(1,0,9,9,0xf2f3,0,0,2,0),	//Labe2: addi t1,t1, 0xf2f3
					new Instruction(8,1,9,0,0,0,0,0,0),			//slt at, t1, $0
					new Instruction(5,0,1,0,0x001,0,0,0,2),		//beq at, $0, 1
					new Instruction(10,0,16,17,0xf3f3,0,0,0,0), //ori s1, s0, 0xf3f3
					new Instruction(2,18,17,9,0,0,0,1,0),		//Label1: addu s2,s1,t1
					new Instruction(9,0,0,1,0x000f,0,0,0,0),	//lui at, 0xf
					new Instruction(10,0,1,1,0xf2f4,0,0,0,0),	//ori at, at, 0xf2f4
					new Instruction(2,19,18,1,0,0,0,0,0),		//addu s2,s1,at
					new Instruction(3,20,19,9,0,0,0,0,0),		//or s4,s3,t1
					new Instruction(5,0,19,17,-10,0,0,0,2),		//beq s3,s1,-10
					new Instruction(9,0,1,1,0x33,0,0,0,0),		//lui at,ox33
					new Instruction(10,0,1,1,0xf33f,0,0,0,0),	//ori at,at,0xf33f
					new Instruction(2,18,17,1,0,0,0,0,0),		//addu s1,s2,at
					new Instruction(8,1,16,8,0,0,0,0,0),  		//slt at,s0,t0
                    new Instruction(6,0,1,0,-11,0,0,0,1),     	//bne at,$0,-11
					new Instruction(9,0,0,1,0xf3,0,0,0,0),		//lui at, 0xf3
					new Instruction(10,0,1,1,0xf3f4,0,0,0,0),	//ori at,at, 0xf3f4
					new Instruction(3,17,16,1,0,0,0,0,0)		//or s1,s0,at
            };

            // Phase 3
            Integer[] phase3_expected = {
                    // HINT: to get these, type the input program into MARS, assemble, and copy the binary values into your test case
                    0x2529F2F3,
					0x0120082a,
					0x10200001,
					0x3611f3f3,
					0x02299021,
					0x3c01000f,
					0x3421f2f4,
					0x02419821,
					0x0269a025,
					0x1271fff6,
					0x3c010033,
					0x3421f33f,
					0x02418821,
					0x0208082a,
					0x1420fff5,
					0x3c0100f3,
					0x3421f3f4,
					0x02018825
                    
            };
			
			testHelper(input,
                    phase1_expected,
                    phase2_expected,
                    phase3_expected);
        }

}