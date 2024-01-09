package jx_review.java_fun_child.weather_app.src.Search;

import java.awt.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import jx_review.java_fun_child.weather_app.src.FileHelper;


public class Trie {

  
     
    public class Node {

        char c;
        ArrayList<Node> children = null;

        public Node(char c) {
            this.c = c; 
            children = new ArrayList<Node>();
        }

    }

      public class SearchNode {

        public String s;
        public Node node = null;

        public SearchNode(Node n, String s ) {
            this.s = s; 
            this.node = n;
            
        }

    }


    Node trieNode;
    JSONObject json;

    public Trie() {
        this.trieNode = new Node(' ');
        this.initLoadStates();
    }

    public void insert (String word) {
        Node curr = this.trieNode;
        Node newNode;
        char charInString; 
        boolean charFound = false; 
        
        for ( int i = 0; i < word.length(); i++) {
            charInString = word.charAt(i);
            charFound = false;
            
            for (Node node: curr.children) {

                // char from word found in tree ( step into new node )
                if (node.c == charInString) {
                    curr = node;
                    charFound = true;
                    break;
                } 

            }

            if (!charFound) {

                newNode =  new Node(charInString);
                curr.children.add(newNode);
                curr = newNode;
            }

        }

    }

    private void initLoadStates () {
        String filePath = Paths.get(FileHelper.getWorkingDirectoryPath(), "Search",  "states.json").toAbsolutePath().normalize().toString();
        String content;
        
        try {
            
            content = new String(Files.readAllBytes(Paths.get(filePath)));
            this.json = new JSONObject(content);

            for (String key : json.keySet()) {
                this.insert(key.toLowerCase());
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public boolean contains(String s) {
        Node curr = this.trieNode;
        char charInString; 
        int targetCount = s.length();
        int count = 0;
        
        for ( int i = 0; i < s.length(); i++) {
            charInString = s.charAt(i);

            for (Node childNode: curr.children) {

                // char from word found in tree ( step into new node )
                if (childNode.c == charInString) {
                    count += 1; 
                    curr = childNode;
                    break;
                } 

            }
        }

        return count == targetCount && s.length() > 0;
    }

    public ArrayList<String> findAll(String s) {
        Node curr = this.trieNode;
        char currChar;
        ArrayList<String> result = new ArrayList<String>();
        String prevString = "";
        ArrayList<SearchNode> queue_array = new ArrayList<SearchNode>();
        Node child; 
        
        for (int i = 0; i < s.length(); i++) {
            currChar = s.charAt(i);
            
            for (Node childNode: curr.children) {
                if (childNode.c == currChar) {
                    prevString += currChar; 
                    curr = childNode;
                }
            }
        }

        if (!curr.children.isEmpty() ) {  

            // // first breadth evaluation 
            queue_array.add(new SearchNode(curr, prevString) );

            while (queue_array.size() != 0) {
                
                SearchNode searchNode = queue_array.remove(0);
                
                if (searchNode.node.children.isEmpty() && searchNode.s.indexOf(s) == 0 ) {
                    result.add(searchNode.s);
                }  else {
                    
                    for (int i = 0 ; i< searchNode.node.children.size(); i++) { // indexed loop certains order 
                        child = searchNode.node.children.get(i);
                        queue_array.add (new SearchNode(searchNode.node.children.get(i), searchNode.s  + child.c ));

                    }
                }
                   
            }

        } else if (s.compareTo(prevString)  == 0) {
            result.add (s);
        }

        return result;

    }
    
    // public static void main (String [] args) {
    // }




}
