import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ExpressionParser {
    private static Stack<Double> nums;
    private static Stack<String> ops;
    public static void main(String[] args) throws IOException {
        if (args.length < 1)
            throw new IllegalArgumentException("Invalid Input! Enter a path to a properly formatted text file");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(args[0])));
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file paht given!");
            System.exit(0);
        }
        ArrayList<String> expression = new ArrayList<String>();
        while (reader.ready()){
            expression.add(reader.readLine());
        }
        nums = new Stack<Double>();
        ops = new Stack<String>();
        double equal = 0;
        for (int i = 0; i < expression.size(); i++){
            String s = expression.get(i);
            if (s.equals("*") ||s.equals("/")){
                ops.push(s);
                i++;
                nums.push(Double.parseDouble(expression.get(i)));
                evaluateTop();
            } else if (s.equals("+") || s.equals("-")){
                ops.push(s);
            } else if (s.equals("=")){
                i++;
                equal = Double.parseDouble(expression.get(i));
                break;
            } else
                nums.push(Double.parseDouble(s));
        }
        while (nums.size() > 1){
            evaluateTop();
        }
        System.out.println("Should be " + equal + " and evaluated to " + nums.pop());

    }

    private static void evaluateTop() {
        double num1 = nums.pop(), num2 = nums.pop();
        String op = ops.pop();
        if (op.equals("+")){
            double temp = num1+num2;
            nums.push(temp);
        } else if (op.equals("-")){
            double temp = num2-num1;
            nums.push(temp);
        }else if (op.equals("*")){
            double temp = num1*num2;
            nums.push(temp);
        } else if (op.equals("/")){
            double temp = num2/num1;
            nums.push(temp);
        } else 
            throw new IllegalArgumentException("Invalid operation!");
    }
}