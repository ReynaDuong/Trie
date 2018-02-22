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
            delete(root, s, 0, s.length());
            return true;
        }
    }

    private void delete(Node pNode, String key, int level, int length){
//        if (pNode != null){
//            // base case
//            if (level == length){
//                // if the node has children
//                if (pNode.outDegree > 0){
//                    // part of another word, remover terminal value
//                    pNode.terminal = false;
//                }
//                else{
//                    pNode = null;
//                    // update the outDegree of its parent node
//                }
//            }
//            else { // recursive case
//                int index = key[level]
//            }
//
//        }
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
        traverse("", root);
    }


    private void traverse(String prefix, Node temp){
        if (temp.outDegree > 0){
            System.out.print(prefix);
        }

        for (char index = 0; index < MIDUP2.ALPHABET_SIZE; ++index) {
            if (temp.children[index] != null)
                traverse(prefix + ('a' + index), temp.children[index]);
        }
    }



}



