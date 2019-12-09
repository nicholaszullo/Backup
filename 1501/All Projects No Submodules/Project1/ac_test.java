
/*  Nick Zullo CS 1501 Fall 2019
*   Project 1, build a predictive text generator with DLB tries
*
*   TODO:
*/
import java.io.*;
import java.util.*;

public class ac_test
{

    public static void main(String[] args) throws Exception
    {
        File history = new File("user_history.txt");                                                //User history file
        BufferedWriter historyWrite = new BufferedWriter(new FileWriter(history, true));            //Writer to history file
        File dictionary = new File("dictionary.txt");                                               //Dictionary file
        Scanner scan = new Scanner(System.in);                                                      //Scanner to recieve input from user
        history.createNewFile();                                                                    //Will create a new file if one does not exist

        DLBHandler dictHandler = new DLBHandler(dictionary);                                        //Create Handlers for each Trie
        DLBHandler histHandler = new DLBHandler(history);
        dictHandler.makeTrie();                                                                     //Create dictionary trie
        
        int totalWords = 0;
        double totalTime = 0;
        char input = 0;
        String word = "";
        String[] result = new String[5];                                                            //Size of 5 because assignment asks for 5 maximum, could make user input number of suggestions and use a variable here
        String[] result2 = new String[5];
        do
        {
            totalWords++;
            histHandler.makeTrie();                                                                 //Dynamically make the History trie after each addition to file. Allows words typed earlier in same run of program to appear later in same run of program 
            System.out.println("\nEnter a character: ");
            input = scan.nextLine().charAt(0);
            if (input == '1')                                                                       //Each case of a number selected by user picks string from predictions. Assumes user will not try to enter a number before suggestions appear (additional notes bullet #4)
            {
                word = result[0];
                writeWord(historyWrite, word); 
                word = "";
            } else if (input == '2')
            {
                word = result[1];
                writeWord(historyWrite, word);
                word = "";
            } else if (input == '3')
            {
                word = result[2];
                writeWord(historyWrite, word);
                word = "";
            } else if (input == '4')
            {
                word = result[3];
                writeWord(historyWrite, word);
                word = "";
            } else if (input == '5')
            {
                word = result[4];
                writeWord(historyWrite, word);
                word = "";
            } else if (input == '$')                                                            //If user is done typing word, save it to file 
            {
                Node curr = histHandler.search(word);                                           //If the word exists in the history file, update frequency of word
                if (curr != null && curr.data == '$')
                {
                    curr.freq++;
                }
                writeWord(historyWrite, word);
                word = "";
            }
            else if (input == '!')                                                              //Exit the program
            {
                totalWords--;                                                                   //Adds 1 to total words at start of loop, but no word added on exit so bring counter back down
                System.out.println("The average time for all prediction retrievals was " + (totalTime/totalWords) + " seconds");
                System.out.println("Goodbye!");
                historyWrite.close();
                scan.close();
            }
            else
            {  
                word += input;                                                                  //Add given letter to word user is typing
                double time = System.nanoTime();                                                //Start timer on finding predictions
                result = histHandler.retrieveAllStrings(word);                                  //Find all available strings with prefix
                result2 = dictHandler.retrieveStrings(word, 5);                                 //Find 5 words from dictionary with prefix
                time = System.nanoTime() - time;                                                //Stop the timer
                time /= 100000000;
                totalTime += time;
                System.out.println("\n\n("+time+" s)\nPredictions:");

                int k = 0;                                                                      //Merge the history results and dictionary results into one array
                for (int i = 0; i < 5; i++)                                                     //Only 5 predictions needed so stop at 5
                {
                    for (int j = 0; j < 5; j++)                                                 //Check for duplicates between history predictions and dictionary predictions
                    {
                        if (result[i] != null && result[i].equals(result2[j]))                  //If the dictionary word exists in the history words, skip to next dictionary word
                        { 
                            result2 = shift(result2, j);                                        //Shift values left 1 starting at index, overwriting index
                        }
                    }
                    if (result[i] == null)                                                      //If less than 5 words are found from user history, add dictionary words to list
                        result[i] = result2[k++];
                    if (result[i] == null)                                                      //If still no words in spot, none available from history or dictionary, so don't print (5) null just print blank
                        break;                                                                  //Stop printing suggestions
                    System.out.printf("(%d) %s   ", (i+1), result[i]);
                }
                if (result[0] == null)
                    System.out.println("No predictions found. Continue typing and end word with '$'");
                
            }
        } while(input != '!');                                                                  //User stops the program
        
    }

    //Same code needed to write a word to file each time, use method to make it easier
    private static void writeWord(BufferedWriter writer, String word) throws Exception
    {
        writer.write(word);                                                                     //Add word to buffer of strings to be written to file
        writer.newLine();                                                                       //Add a new line to the buffer
        writer.flush();                                                                         //Write the buffer to the file. Could write all at once when .close is called but in order to dynamically update suggestions need to write each time
        System.out.println("Word " + word + " Saved!");                                         //Confirmation to the user the given word has been saved
    }
    
    //Quick Array shifting method, no need to worry about case of index = length -1
    //because no duplicates would be present because at least one word would have to 
    //come from user history and only 4 needed from dict
    private static String[] shift(String[] list, int index)
    {
        for (int i = index; i < list.length-1; i++)
        {
            list[i] = list[i+1];                                                                //Move word from right one spot left. No temp needed because overwriting data on purpose/not needed anymore
        }
        return list;
    }
}