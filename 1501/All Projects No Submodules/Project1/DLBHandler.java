import java.io.*;
import java.util.*;

public class DLBHandler 
{
    private BufferedReader reader;
    private Node root;                                                                          //Root of the DLB this class is handling, private, so save a copy when calling makeTree elsewhere

    public DLBHandler(File dict) throws Exception
    {
        reader = new BufferedReader(new FileReader(dict));                                      //Reader to read lines from a given file
        root = null;
    }

    public Node makeTrie() throws Exception
    {
        
        Node curr = null;

        while(reader.ready())                                                                   //Create DLB from passed file until no more in file
        {
            String currLine = reader.readLine();                                                //Take the current word in file
            curr = root;                                                                        //Start making the trie from the root
            for (int i = 0; i < currLine.length(); i++)                                         //Loop for each character in string
            {
                char currLetter = currLine.charAt(i);                                           //Take the current letter in string

                if (root == null)                                                               //Create root node if at begining
                {
                    root = new Node(currLine.charAt(0), 1);
                    curr = root;
                }
                else if (curr.nextData == null && curr.data !='$')                              //Case no vertical path and it is needed so create it
                {
                    curr.nextData = new Node(currLetter, 1);                                    //Create new node
                    curr = curr.nextData;
                }
                else if (curr.data == '$'&& curr.otherNode == null)                             //Case not at the end of current word, but at the end of current path, so create horizontal path
                {
                    curr.otherNode = new Node(currLetter, 1);
                    curr = curr.otherNode;   
                }
                else if (curr.data == '$' && curr.otherNode != null)                            //Case not at the end of current word, but at end of current path, and a horizontal path already exists
                {
                    for (Node loop = curr.otherNode; loop != null; loop = loop.otherNode)       //Loop until correct path is found or created
                    {
                        if (loop.data == currLetter)                                            //Correct path found, move to it
                        {
                            curr = loop.nextData;
                            break;                                                              //Leave for loop and don't check rest of paths
                        }
                        else if (loop.otherNode == null)
                        {
                            loop.otherNode = new Node(currLetter, 1);                           //Path not found for currLetter so create it
                            curr = loop.otherNode;
                            break;                                                              //Leave for loop because a path is found
                        }
                    } 
                }
                else if (curr.nextData != null)                                                 //Case there is a vertical path
                {
                    if (curr.data == currLetter)                                                //Move down correct vertical path
                    {
                        curr = curr.nextData;
                    }
                    else if (curr.otherNode != null)                                            //Correct path not immediate so find it 
                    {
                        
                        for (Node loop = curr.otherNode; loop != null; loop = loop.otherNode)   //Same for loop as before to find a horizontal path to take
                        {
                            if (loop.data == currLetter)                                         
                            {
                                curr = loop.nextData;
                                break;   
                            }
                            else if (loop.otherNode == null)
                            {
                                loop.otherNode = new Node(currLetter, 1);                      
                                curr = loop.otherNode;
                                break;
                            }
                        } 
                        
                    }
                    else                                                                        //Last case no horizontal path exists and it is needed so create it
                    {
                        curr.otherNode = new Node(currLetter, 1);
                        curr = curr.otherNode;
                    }   
                }
                
                
            }
            if (curr.data == '$')                                                               //Cases for after a word has been inserted, where to put $ character
            {                                                                                   //Case this word has already been added, so increment freq
                curr.freq++;
            }
            else if (curr.nextData == null)                                                     //Case first time a word has been added
            {
                curr.nextData = new Node('$', 1);
            }
            else if (curr.nextData.data != curr.data)                                           //Special case adding in the middle of a word that has been created already
            {
                Node temp = new Node(curr);                                                     //Create a copy of current node and where it points
                curr.data = '$';                                                                //Update values of current node. Need to keep parent node pointing at curr, but can't access parent from this part in loop so creating a new node and linking to parent not an option
                curr.freq = 1;
                curr.nextData = null;                                                           //nextData must be null for $ character
                curr.otherNode = temp;                                                          //Link previous node to new horizontal path from $ of newly added word
            }
        }
        
        return root;                                                                            //Returns a reference to the start of the tree if needed
    }

    //Finds all strings that exist in the DLB with the prefix word
    public String[] retrieveAllStrings(String word)
    {
        Node start = search(word);                                                              //Start retrieving strings at node that word ends at 
        TreeMap<Integer, ArrayList<String>> words = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder());        //Treemap to store 2 data items in parallel. Frequency of word, which is stored from greatest to least by calling Collections.reverseOrder, and a data structure storing all words found with that frequency
        String[] result = new String[5];                                                        //Only output 5 total to user so 5, could make user ask for how many suggestions dynamically and use a variable here instead
        if (start == null)                                                                      //If the prefix is not found no words to suggest
        {
            return result;
        }
        recFindAll(start, word, words);                                                         //Starting call to recursive function to find all words with given prefix
        int i = 0;                                                                              //Position in array to return

        for (Integer key : words.keySet())                                                      //For all the frequencies found
        {                                                                                       //k is position in arraylist, reset each new key
            for (int k = 0; k < words.get(key).size() && i < 5 ; k++)                           //While there are words with the frequency and 5 words total have not been found
            {
                result[i++] = words.get(key).get(k);                                            //Add suggested word to list
            }
        }
        return result;                                                                          //Return suggestions
    }

    
    private void recFindAll(Node curr, String word, TreeMap<Integer, ArrayList<String>> words)
    {                                                                                           //All are if and not else if because need to check all paths. If it was else if then a horizontal path would be skipped if node has a vertical path also
        if (curr != null && curr.data =='$')                                                    //Base case a word has been found
        {
            if (words.get(curr.freq) == null)                                                   //If this is the first word with its frequency, create a new arraylist for that frequency and add to treemap
            {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                words.put(curr.freq, temp);
            }
            else                                                                                //Already an arraylist at frequency so add word to it
            {
                words.get(curr.freq).add(word);
            }
            recFindAll(curr.nextData, word, words);                                             //Find more words
        } 
        if (curr != null && curr.data != '$')                                                   //Vertical path found, find words that exist down that path
        {
            word +=curr.data;
            recFindAll(curr.nextData, word, words);
            word = word.substring(0, word.length()-1);                                          //Remove letter from word so next if call doesn't have both the vertical letter and horizontal letter
        }
        if (curr != null && curr.otherNode != null)                                             //Horizontal path found, find all words down path
        {
            recFindAll(curr.otherNode, word, words);
        }
    }

    //Similar method to retrieveAll, but stops the search after num total have been found. Useful if not looking for every word to compare frequencies
    public String[] retrieveStrings(String word, int num)                                       //Same as retrieveAll, but just an arraylist needed and no need to nested loop
    {
        Node start = search(word);
        ArrayList<String> words = new ArrayList<String>();
        String[] result = new String[num];
        if (start == null)
        {
            return result;
        }
        
        recFindNext(start, word, words, num);
        
        int i = 0;
        for (String s : words)
        {
            result[i++] = s;
        }
        return result;
    }
    private void recFindNext(Node curr, String word, ArrayList<String> words, int end)
    {
       
        if (words.size() == end)                                                                //Base case end amount of words have been found
        {
            return ;
        }
        else if (curr != null && curr.data == '$')                                              //Base case a word has been found
        {
            words.add(word);                                                                    //Add to list of found words
            recFindNext(curr.nextData, word, words, end);
        }
        else if (curr != null)                                                                  //A vertical path exits so take it 
        {
            word += curr.data;
            recFindNext(curr.nextData, word, words, end);
            word = word.substring(0, word.length()-1);
        }
        if (curr != null && curr.otherNode != null)                                             //Not else if because might need to check horizontal paths of node as well as vertical path
        {
            recFindNext(curr.otherNode, word, words, end);                                      //Go down horizontal path
        }
    }

    //Returns the last node in prefix key to start off in other search functions
    public Node search(String key)
    {
        if (root == null)                                                                       //If there is no tree there is no prefix match
            return null;
        return recSearch(key, root, 0);                                                         //Return the result of searching for a prefix
    }

    private Node recSearch(String key, Node curr, int index)
    {
        if (curr == null && index == key.length())                                              //Base case prefix is not found
        {
            return null;
        }
        else if (index == key.length())                                                         //Base case prefix is found
        {
            return curr;
        } 
        else if (curr.data == key.charAt(index))                                                //Move down vertical path
        {
            curr = recSearch(key, curr.nextData, (index+1));
        }
        else if (curr.otherNode != null)                                                        //Move down horizontal path
        {
            curr = recSearch(key, curr.otherNode, index);
        }
        else                                                                                    //Carry up null if reach here 
        {
            return null;
        }
        return curr;
      
    }


}