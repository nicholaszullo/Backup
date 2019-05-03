
import java.util.LinkedList;
import java.util.List;

public class Phase3 {

    /* Translate each Instruction object into
     * a 32-bit number.
     *
     * tals: list of Instructions to translate
     *
     * returns a list of instructions in their 32-bit binary representation
     *
     */
    public static List<Integer> translate_instructions(List<Instruction> tals) {
        List<Integer> binary = new LinkedList<Integer>();
		
		for (Instruction curr : tals){
			int sum = 0;
			int opcode = 0;
			int rs = 0;
			int rt = 0;
			int rd = 0;
			int shamt = 0;
			int funct = 0;
			int immediate = 0;
			switch (curr.instruction_id){
				case 1:									//addiu
					opcode = 9 << 26;					//Shift opcode to opcode location in machine code
					rs = curr.rs << 21;					//shift rs into the rs location
					rt = curr.rt << 16;					//shifr rt into rt's location
					immediate = curr.immediate << 16;	//immediate0000... 		cant just add immediate to sum. if negative, will subtract from the whole number, not place in 16 bit field, so shifting needed
					immediate = immediate >>> 16;		//0000..immediate		makes it so the add operation places all the bits together into seperated fields, allows for -#'s as immediate, only sign extended 16 bits instead of 32
					sum = opcode + rs + rt + immediate;	//Combine all the numbers into one binary number
					break;								//Repeat this process with each type of supported instruction
					
				case 2:									//addu
					opcode = 0;							//opcode of addu is 0
					rs = curr.rs << 21;				
					rt = curr.rt << 16;
					rd = curr.rd << 11;
					shamt = curr.shift_amount << 6;
					funct = 0x21;						//funct of addu is 0x21
					sum = opcode + rs + rt + rd + shamt + funct;
					break;
				case 3:									//or
					opcode = 0;							//opcode of or is 0
					rs = curr.rs << 21;				
					rt = curr.rt << 16;
					rd = curr.rd << 11;
					shamt = curr.shift_amount << 6;
					funct = 0x25;						//funct of or is 0x25
					sum = opcode + rs + rt + rd + shamt + funct;
					break;
				case 5:									//beq
					opcode = 4 << 26;					//beq opcode is 4
					rs = curr.rs << 21;
					rt = curr.rt << 16;
					immediate = curr.immediate << 16;		
					immediate = immediate >>> 16;			
					sum = opcode + rs + rt + immediate;
					break;
				case 6:									//bne
					opcode = 0x5 << 26;					//bne opcode is 5
					rs = curr.rs << 21;
					rt = curr.rt << 16;
					immediate = curr.immediate << 16;		
					immediate = immediate >>> 16;
					sum = opcode + rs + rt + immediate;
					break;
				case 8:									//slt
					opcode = 0;							//opcode of slt is 0
					rs = curr.rs << 21;				
					rt = curr.rt << 16;
					rd = curr.rd << 11;
					shamt = curr.shift_amount << 6;
					funct = 0x2a;						//funct of slt is 0x2a
					sum = opcode + rs + rt + rd + shamt + funct;
					break;
				case 9:									//lui
					opcode = 0xf << 26;					//lui opcode is f
					rs = curr.rs << 21;
					rt = curr.rt << 16;
					immediate = curr.immediate << 16;		
					immediate = immediate >>> 16;
					sum = opcode + rs + rt + immediate;
					break;
				case 10:								//ori
					opcode = 0xd << 26;					//ori opcode is d
					rs = curr.rs << 21;
					rt = curr.rt << 16;
					immediate = curr.immediate << 16;		
					immediate = immediate >>> 16;
					sum = opcode + rs + rt + immediate;
					break;
				default:
					System.out.println("Not a supported instruction!");
					break;				
				
			}
		binary.add(sum);
		}
		return binary;
		
    }
}
