// Demonstrates JPanel, GridLayout and a few additional useful graphical features.
// adapted from an example by john ramirez (cs prof univ pgh)
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SimpleCalc
{
	JFrame window;  // the main window which contains everything
	Container content ;
	JButton[] digits = new JButton[12]; 
	JButton[] ops = new JButton[4];
	JTextField expression;
	JButton equals;
	JTextField result;
	
	
	public SimpleCalc()
	{
		window = new JFrame( "Simple Calc");
		content = window.getContentPane();
		content.setLayout(new GridLayout(2,1)); // 2 row, 1 col
		ButtonListener listener = new ButtonListener();
		
		// top panel holds expression field, equals sign and result field  
		// [4+3/2-(5/3.5)+3]  =   [3.456]
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,3)); // 1 row, 3 col
		
		expression = new JTextField();
		expression.setFont(new Font("verdana", Font.BOLD, 16));
		expression.setText("");
		
		equals = new JButton("=");
		equals.setFont(new Font("verdana", Font.BOLD, 20 ));
		equals.addActionListener( listener ); 
		
		result = new JTextField();
		result.setFont(new Font("verdana", Font.BOLD, 16));
		result.setText("");
		
		topPanel.add(expression);
		topPanel.add(equals);
		topPanel.add(result);
						
		// bottom panel holds the digit buttons in the left sub panel and the operators in the right sub panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2)); // 1 row, 2 col
	
		JPanel  digitsPanel = new JPanel();
		digitsPanel.setLayout(new GridLayout(4,3));	
		
		for (int i=0 ; i<10 ; i++ )
		{
			digits[i] = new JButton( ""+i );
			digitsPanel.add( digits[i] );
			digits[i].addActionListener( listener ); 
		}
		digits[10] = new JButton( "C" );
		digitsPanel.add( digits[10] );
		digits[10].addActionListener( listener ); 

		digits[11] = new JButton( "CE" );
		digitsPanel.add( digits[11] );
		digits[11].addActionListener( listener ); 		
	
		JPanel opsPanel = new JPanel();
		opsPanel.setLayout(new GridLayout(4,1));
		String[] opCodes = { "+", "-", "*", "/" };
		for (int i=0 ; i<4 ; i++ )
		{
			ops[i] = new JButton( opCodes[i] );
			opsPanel.add( ops[i] );
			ops[i].addActionListener( listener ); 
		}
		bottomPanel.add( digitsPanel );
		bottomPanel.add( opsPanel );
		
		content.add( topPanel );
		content.add( bottomPanel );
	
		window.setSize( 640,480);
		window.setVisible( true );
	}

	public String calcResult(String equation){
		StringTokenizer st = new StringTokenizer(equation, "+-/*",true);
		ArrayList<String> firstOperators = new ArrayList<String>();
		ArrayList<String> secondOperators = new ArrayList<String>();
		ArrayList<String> all = new ArrayList<String>();
		int total = 0;
		
		
		while (st.hasMoreTokens()){						//Split the equation into 3 parts
			String token = st.nextToken();
			all.add(token);
			if ("/*".contains(token))						//First precedence ops
				firstOperators.add(token);
			else if ("+-".contains(token))					//Next precedence
				secondOperators.add(token);
		}		
	
		for (String firstOp : firstOperators){					//Calculate equation after performing first operations
			int location = all.indexOf(firstOp);
			if (location+1 >= all.size() || "+-/*".contains(all.get(location-1)) || "+-/*".contains(all.get(location+1))){
				return "INVALID EXPRESSION";				//If there is nothing after the operator or what comes before or after is not a number, the expression is invalid
			} else if (firstOp.equals("*")){
				total = Integer.parseInt(all.get(location-1)) * Integer.parseInt(all.get(location+1));
				all.remove(location+1);							//Remove the operands and operator
				all.remove(location);
				all.remove(location-1);
				all.add(location-1,Integer.toString(total));	//Update the total in the array of the equation
			} else {
				total = Integer.parseInt(all.get(location-1)) / Integer.parseInt(all.get(location+1));	
				all.remove(location+1);
				all.remove(location);
				all.remove(location-1);
				all.add(location-1,Integer.toString(total));
			}
			
		}
		
		for (String nextOp : secondOperators){				//Same as before but with + - 
			int location = all.indexOf(nextOp);
			if (location+1 >= all.size() || "+-/*".contains(all.get(location-1)) || "+-/*".contains(all.get(location+1))){
				return "INVALID EXPRESSION";
			} else if (nextOp.equals("+")){
				total = Integer.parseInt(all.get(location-1)) + Integer.parseInt(all.get(location+1));
				all.remove(location+1);
				all.remove(location);
				all.remove(location-1);
				all.add(location-1,Integer.toString(total));
			} else {
				total = Integer.parseInt(all.get(location-1)) - Integer.parseInt(all.get(location+1));
				all.remove(location+1);
				all.remove(location);
				all.remove(location-1);
				all.add(location-1,Integer.toString(total));
			}
			
		}
		
		if (all.size() > 1){
			return "INVALID EXPRESSION";
		}
		return Integer.toString(total);
		
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Component whichButton = (Component) e.getSource();
			
			result.setText("");												//If a button is pressed clear result
			
			for (int i=0 ; i<10 ; i++ )
				if (whichButton == digits[i])
					expression.setText( expression.getText() + i );			//Add the digit to the expression
				
			for (int i = 0; i < 4; i++){									//If operator add character
				if (whichButton == ops[i])
					expression.setText(expression.getText() + ops[i].getText());
			}
			
			if (whichButton == digits[10])									//If C clear expression
				expression.setText("");
			else if (whichButton == digits[11])								//If CE clear last character 
				expression.setText(expression.getText().substring(0,expression.getText().length()-1));
			
			if (whichButton == equals)										//If the equals button, calc the result
				result.setText(calcResult(expression.getText()));
			
		}
	}

	public static void main(String [] args) 
	{
		new SimpleCalc();
		
	}
}

