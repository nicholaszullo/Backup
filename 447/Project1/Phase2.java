
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Phase2 {

    /* Returns a list of copies of the Instructions with the
     * immediate field of the instruction filled in
     * with the address calculated from the branch_label.
     *
     * The instruction should not be changed if it is not a branch instruction.
     *
     * unresolved: list of instructions without resolved addresses
     * first_pc: address where the first instruction will eventually be placed in memory
     */
    public static List<Instruction> resolve_addresses(List<Instruction> unresolved, int first_pc) {
		List<Instruction> resolved = new LinkedList<Instruction>();
		int[] addresses = new int[unresolved.size()];
		int currPC = first_pc;
		for (Instruction currInst : unresolved){			//Find labels
			currPC += 4;													//Increment PC first
			if (currInst.label_id > 0)										//If an instruction conatins a label, add to the list of labels
				addresses[currInst.label_id] = new Integer(currPC);			//Adding to the array at the postion of the label allows any order for them to be found in and allows any branch to find the address of that label_id
			
		}
		currPC = first_pc;
        for (Instruction currInst : unresolved){			//Add distance to labels as immediate for each branch instruction
			currPC += 4;
			if (currInst.instruction_id == 6 || currInst.instruction_id == 5){							//assumes that it is jumping to a label that exists	or immediate will be -(PC+4)						
				resolved.add(new Instruction(currInst.instruction_id,currInst.rd,currInst.rs,currInst.rt,(addresses[currInst.branch_label] - (currPC+4))/4,currInst.jump_address,currInst.shift_amount,currInst.label_id,currInst.branch_label));
							
			}else
				resolved.add(currInst);
			
		}
		return resolved;
    }

}
