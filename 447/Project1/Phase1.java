
import java.util.LinkedList;
import java.util.List;


public class Phase1 {

    /* Translates the MAL instruction to 1-3 TAL instructions
     * and returns the TAL instructions in a list
     *
     * mals: input program as a list of Instruction objects
     *
     * returns a list of TAL instructions (should be same size or longer than input list)
     */

	
    public static List<Instruction> mal_to_tal(List<Instruction> mals) {
		List<Instruction> tals = new LinkedList<Instruction>(); 
	
		for (Instruction currInst : mals){
			switch (currInst.instruction_id){				//pick code block based on instructon id
				case 1:					//addiu when i > 16 bits
					if (currInst.immediate > 0xFFFF){
						int upper = currInst.immediate / 0xFFFF;
						int lower = currInst.immediate % 65536;
						tals.add(new Instruction(9,0,0,1,upper,0,0,0,0));			//lui at, upper
						tals.add(new Instruction(10,0,1,1,lower,0,0,0,0));			//ori at, at, lower
						tals.add(new Instruction(2,currInst.rt,currInst.rs,1,0,0,0,currInst.label_id,currInst.branch_label));		//addu rt, rs, at
					} else
						tals.add(currInst);
					break;
				case 10:				//ori when i > 16 bits
					if (currInst.immediate > 0xFFFF){
						int upper = currInst.immediate / 0xFFFF;
						int lower = currInst.immediate % 65536;
						tals.add(new Instruction(9,0,0,1,upper,0,0,0,0));			//lui at, upper
						tals.add(new Instruction(10,0,1,1,lower,0,0,0,0));			//ori at,at lower
						tals.add(new Instruction(3,currInst.rt,currInst.rs,1,0,currInst.jump_address,currInst.shift_amount,currInst.label_id,currInst.branch_label));	//or rt, rs, at
					}else
						tals.add(currInst);
					break;
				case 100:				//blt to slt bne
					tals.add(new Instruction(8,1,currInst.rs,currInst.rt,0,0,0,0,0));					//slt rs, rt, at
					tals.add(new Instruction(6,0,1,0,0,0,0,currInst.label_id,currInst.branch_label));	//bne at, $0, label_id
					break;
				case 101:				//bge to slt beq
					tals.add(new Instruction(8,1,currInst.rs,currInst.rt,0,0,0,0,0));					//slt rs, rt, at
					tals.add(new Instruction(5,0,1,0,0,0,0,currInst.label_id,currInst.branch_label));	//beq at, $0, label_id
					break;
				default:
				//	System.out.println("Instruction ID not in list of pseudo instruction IDs");
					tals.add(currInst);
					break;
			
			}
			
		}
		
		return tals;
        
    }

}
