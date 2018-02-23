import java.io.*;
import java.io.IOException;
import java.util.*;

public class MIDUP2 {
    static final int ALPHABET_SIZE = 26;

    public static void main(String [] args){
        // read from a file
        Scanner input = new Scanner(System.in);

        try {

            File file = new File(input.nextLine());
            input = new Scanner(file);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        String line = "";               // to read new line
        String key = "";                // key to insert/ search/ delete
        boolean isSuccessful = false;

        // Make a new Trie
        Trie myTrie = new Trie();

        // read each line of the file

        final String INSERT = "A";      // Insert word
        final String DELETE = "D";      // Delete word
        final String SEARCH = "S";      // Search word
        final String PRINT = "M";       // Print membership
        final String SPELLING = "C";    // Check spelling
        final String LIST = "L";        // List all the words
        final String END = "E";         // End of file


        do{
            key = "";
            isSuccessful = false;
            line = input.nextLine();
            String [] tokens = line.split("\\s+");

            switch (tokens[0]){
                case INSERT:
                    key = tokens[1];
                    // insert the new string
                    isSuccessful = myTrie.insert(key);

                    if (isSuccessful){
                        System.out.println("Word inserted");
                    }
                    else {
                        System.out.println("Word already exists");
                    }
                    break;

                case DELETE:
                    key = tokens[1];
                    isSuccessful = myTrie.delete(key);

                    if (isSuccessful){
                        System.out.println("Word deleted");
                    }
                    else{
                        System.out.println("Word not found");
                    }
                    break;

                case SEARCH:
                    key = tokens[1];
                    isSuccessful = myTrie.isPresent(key);
                    if (isSuccessful){
                        System.out.println("Word found");
                    }
                    else{
                        System.out.println("Word not found");
                    }
                    break;

                case PRINT:
                    System.out.println("Membership is " + myTrie.membership());
                    break;

                case SPELLING:
                    key = tokens[1];
                    isSuccessful = myTrie.isPresent(key);
                    if (isSuccessful){
                        System.out.println("Spelling OK");
                    }
                    else{
                        System.out.println("Spelling mistake");
                    }
                    break;

                default:    // Print all the words
                    myTrie.listAll();
            }
        }
        while(!line.equals(END));

    }
}

class Node{
    boolean terminal;
    int outDegree;
    Node [] children;

    Node(){
        terminal = false;
        outDegree = 0;
        children = new Node[26];        // create an array of 26 Node

        // initialize the array
        for (int i = 0; i < 26; i++){
            children[i] = null;
        }
    }
}


class Trie{
    private Node root;

    Trie(){
        root = new Node();
    }

    /**
     * return false if the string is already present otherwise true
     * @param s string to be inserted
     * @return false
     */
    boolean insert(String s){
        if (isPresent(s)){
            return false;
        }
        else{
            int level;
            int length = s.length();
            int index;
            Node temp = root;

            for (level = 0; level < length; level++)
            {
                index = s.charAt(level) - 'a';

                if (temp.children[index] == null){
                    temp.children[index] = new Node();
                    temp.outDegree += 1;                    // add 1 to the degree

                }

                temp = temp.children[index];
            }

            temp.terminal = true;
            return true;
        }
    }

    /**
     * return true if the string is present otherwise false
     * @param s string to be searched for
     * @return false
     */
    boolean isPresent(String s){
        int level;
        int length = s.length();
        int index;
        Node temp = root;

        for (level = 0; level < length; level++)
        {
            index = s.charAt(level) - 'a';

            if (temp.children[index] == null)
                return false;

            temp = temp.children[index];
        }

        return (temp != null && temp.terminal);
    }

    /**
     * return false if s is not present, otherwise true
     * @param s string to be deleted
     * @return false
     */
    boolean delete(String s){
        if (!isPresent(s)){
            return false;
        }
        else{
            // delete the node
            deleteHelper(s, root, s.length(), 0);
            return true;
        }
    }

    private boolean deleteHelper(String key, Node current, int length, int level){
        boolean deleteSelf = false;

        // base case
        if (level == length){

            // if the node has no children
            if (hasNoChildren(current)){
                // affect the parent node degree
                current = null;
                deleteSelf = true;
                updateDegree(key);
            }
            else{ // if the node is prefix of another string
                // unmark the end of the word
                // degree is not affected
                current.terminal = false;
                deleteSelf = false;
            }
        }
        else{   // recursive case
            Node childNode = current.children[getIndex(key.charAt(level))];

            boolean childDeleted = deleteHelper(key, childNode, length, level + 1);

            updateDegree(key);

            if (childDeleted){
                // making the children node null
                current.children[getIndex(key.charAt(level))] = null;

                countChildren(current);

                // part of another node
                // cannot delete
                if (current.terminal){
                    deleteSelf = false;
                }
                else if (!hasNoChildren(current)){
                    // have more than one children
                    // cannot delete
                    deleteSelf = false;
                }
                else{
                    // delete node
                    current = null;
//                    updateDegree(key);
                    deleteSelf = true;
                }
            }
            else{
                deleteSelf = false;
            }
        }

        return deleteSelf;
    }

    private int getIndex(char ch){
        return (ch - 'a');
    }

    private boolean hasNoChildren(Node temp){
        for (int i = 0; i < temp.children.length; i++){
            if (temp.children[i] != null){
                return false;
            }
        }
        return true;
    }


    private void countChildren(Node temp){
        int degree = 0;

        for (int i = 0; i < MIDUP2.ALPHABET_SIZE; i++){
            if (temp.children[i] != null){
                degree++;
            }
        }

        temp.outDegree = degree;
    }

    private void updateDegree(String prefix){
        // goes from the top to search for the prefix until getting a null
        int level;
        int length = prefix.length();
        int index;
        Node temp = root;

        countChildren(root);

        for (level = 0; level < length; level++)
        {
            index = getIndex(prefix.charAt(level));

            if (temp.children[index] == null) {
                break;
            }
            else{
                countChildren(temp.children[index]);
            }

            temp = temp.children[index];
        }

    }

    /**
     * return the number of words in the data structure
     * @return number of words in Trie
     */
    int membership(){
        return membership(root);
    }


    private int membership(Node temp){
        int result = 0;

        // Leaf denotes end of a word
        if (temp.terminal) {
            result++;
        }

        for (int i = 0; i < MIDUP2.ALPHABET_SIZE; i++) {
            if (temp.children[i] != null) {
                result += membership(temp.children[i]);
            }
        }
        return result;
    }



    /**
     * list all the members of the Trie in alphabetical order
     */
    void listAll(){
    }


}



