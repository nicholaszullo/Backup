// CS 0445 Spring 2019
// Recitation Exercise 6
// Testing the numLeaves() method
// Note:  The first two trees used in this exercise are the same as those used in
// Example12.java.  For images of these trees see file E12Data.docx in the Handouts
// page of the CS 0445 Web site.

import TreePackage.*;  
public class Rec6
{
	public static void main(String [] args)
	{
		BinaryNode<Integer> T;
		T = init1();
		int leaves = T.numLeaves();
		System.out.println("T1 has " + leaves + " leaves");
		T = init2();
		leaves = T.numLeaves();
		System.out.println("T2 has " + leaves + " leaves");
		T = new BinaryNode<Integer>(new Integer(100));
		leaves = T.numLeaves();
		System.out.println("T3 has " + leaves + " leaves");	
	}

	// These trees are built in kind of a "hack" way just to give us examples with
	// which to test our operations.  We will see better ways of building trees
	// soon.  To see a visual depiction of these trees, please see document:
	// Ex12Data.docx

	public static BinaryNode<Integer> init1()
	{
		BinaryNode<Integer> temp1 = new BinaryNode<Integer>(new Integer(60));
		BinaryNode<Integer> temp2 = new BinaryNode<Integer>(new Integer(30));
		BinaryNode<Integer> temp3 = new BinaryNode<Integer>(new Integer(80), temp1, temp2);
		temp1 = new BinaryNode<Integer>(new Integer(20));
		temp2 = new BinaryNode<Integer>(new Integer(15), temp1, temp3);
		temp3 = temp2;
		temp2 = new BinaryNode<Integer>(new Integer(50));
		temp1 = new BinaryNode<Integer>(new Integer(40), null, temp2);
		temp2 = new BinaryNode<Integer>(new Integer(75));
		BinaryNode<Integer> temp4 = new BinaryNode<Integer>(new Integer(65), temp1, temp2);
		temp1 = new BinaryNode<Integer>(new Integer(90), temp4, temp3);
		return temp1;
	}

	public static BinaryNode<Integer> init2()
	{
		BinaryNode<Integer> temp1 = new BinaryNode<Integer>(new Integer(17));
		BinaryNode<Integer> temp2 = new BinaryNode<Integer>(new Integer(20), temp1, null);
		temp1 = new BinaryNode<Integer>(new Integer(10));
		BinaryNode<Integer> temp3 = new BinaryNode<Integer>(new Integer(15), temp1, temp2);
		temp2 = new BinaryNode<Integer>(new Integer(30));
		temp1 = new BinaryNode<Integer>(new Integer(25), temp3, temp2);
		temp3 = temp1;

		temp1 = new BinaryNode<Integer>(new Integer(55));
		temp2 = new BinaryNode<Integer>(new Integer(70));
		BinaryNode<Integer> temp4 = new BinaryNode<Integer>(new Integer(60), temp1, temp2);
		temp1 = new BinaryNode<Integer>(new Integer(80));
		temp2 = new BinaryNode<Integer>(new Integer(85), temp1, null);
		temp1 = new BinaryNode<Integer>(new Integer(75), temp4, temp2);

		temp4 = new BinaryNode<Integer>(new Integer(50), temp3, temp1);
		return temp4;
	}
	
	
}



