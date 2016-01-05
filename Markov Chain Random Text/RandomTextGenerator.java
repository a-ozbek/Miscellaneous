// Name: Ahmet Can Ozbek

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class generates the HashMap of pairs Prefixes and their successors and
 * then generates the random text.
 * @author ahmetcanozbek
 *
 */
public class RandomTextGenerator {	
	//constant
	public static final int SEED_VALUE = 1;	
	//Instance variables	
	private ArrayList<String> wordList;//The words in the input source text
	private int prefixLength;
	private int numWords;//The number of words that are going to be in our random output text.
	private boolean isDebugMode;//Whether we are in the debug mode or not
	private Random numberGenerator;
		
	//Constructor
	public RandomTextGenerator(ArrayList<String> wordList, int prefixLength, int numWords, boolean isDebugMode){
		this.wordList = wordList;
		this.prefixLength = prefixLength;
		this.numWords = numWords;
		this.isDebugMode = isDebugMode;
		//If we are in the debug mode
		if(isDebugMode){
			//Setting the seed parameter if we are in the debug mode
			numberGenerator = new Random(SEED_VALUE);
		}else{
			numberGenerator = new Random();
		}		
	}	
	
	//Methods	
	/**
	 * Generating the hashMap
	 * In this hashMap, we have all the possible prefixes in the text as keys of the map.
	 * The values corresponding to these keys are successors(the words that are coming
	 * after) the prefixes.Therefore one key in the map may have more than one value.
	 * Each key(Prefix type) corresponds to one value(myContainer) type, however
	 * myContainer class is capable of containing multiple words
	 * @param wordList the words of the input sample text from the user.	 
	 * @return the HashMap that maps each Prefix in the sample text to its successors.
	 */
	private HashMap<Prefix, myContainer> generateHashMap(ArrayList<String> wordList){
		
		//A temporary prefix to be used later in the method
		Prefix tempPrefix = new Prefix();
		//Successor of the tempPrefix
		String successorOftempPrefix = new String();
		//Our HashMap to be generated
		HashMap<Prefix, myContainer> prefixMap = new HashMap<Prefix, myContainer>();
		
		/**
		 * In this for loop, we are iterating one by one and getting a word group in 
		 * the size of the prefixLength, therefore we are able to get every possible
		 * prefix in the sample text.
		 */
		for(int i=0;i<wordList.size()-prefixLength;i++){
			//Temporary prefix from the sample text for each iteration
			tempPrefix = new Prefix(new ArrayList<String>(wordList.subList(i, i+prefixLength)));
			//Successor of that temporary prefix
			successorOftempPrefix = wordList.get(i+prefixLength);
			
			if(!prefixMap.containsKey(tempPrefix)){
				//If the map does not contain the Prefix create a new container to hold its successor
				myContainer newContainer = new myContainer(isDebugMode);
				newContainer.add(successorOftempPrefix);
				//Put the prefix and its container pair into the map
				prefixMap.put(tempPrefix, newContainer);				
			}else{
				//If the map already contains the prefix add it to the already existing myContainer
				prefixMap.get(tempPrefix).add(successorOftempPrefix);				
			}			
		}		
		return prefixMap;			
	}
	
	/**
	 * This method generates our random text.	 
	 * @return The random text generated which is in String format.
	 */
	public String generateRandomText(){
		
		//The HashMap(prefixMap variable) that maps prefixes to successors is generated
		Map<Prefix, myContainer> prefixMap = generateHashMap(wordList);
		//Choosing initial random Prefix
		Prefix currentPrefix = new Prefix();		
		currentPrefix  = chooseRandomPrefix(wordList,prefixLength);
		if(isDebugMode){System.out.println("DEBUG: chose a new initial prefix: " + currentPrefix.toString());} //Debugging
		//The random output text that is going to be generated by the help of the HashMap
		ArrayList<String> outputText = new ArrayList<String>();
		String randomWord = new String();
		
		//for loop to generate our random text.
		for(int i=0;i<numWords;i++){
			//The prefix that is at the end of the file does not have a key in the HashMap.
			//Therefore, the purpose of this if statement is to check whether the current
			//prefix is the one that is at the end of the file or not.
			if(prefixMap.containsKey(currentPrefix)){
				//currentPrefix is not the prefix that is at the end of the file.
				if(isDebugMode){System.out.println("DEBUG: prefix: " + currentPrefix.toString());} //Debugging
				if(isDebugMode){System.out.println("DEBUG: successors: " + prefixMap.get(currentPrefix).toString());} //Debugging
				//Choose a random successor of the current prefix (choose a random word from the corresponding "myContainer")
				randomWord = prefixMap.get(currentPrefix).getRandomWord();				
				if(isDebugMode){System.out.println("DEBUG: word generated: " + randomWord);} //Debugging
				//Add that chosen random word to the output text
				outputText.add(randomWord);
				//Add the chosen word to the prefix and change the current prefix while keeping prefixLength constant.
				currentPrefix = currentPrefix.shiftIn(randomWord);
			}else{
				//currentPrefix is the prefix that is at the end of the file.
				if(isDebugMode){System.out.println("DEBUG: prefix: " + currentPrefix.toString());} //Debugging
				if(isDebugMode){System.out.println("<END OF FILE>");} //Debugging
				//Choose a new random prefix that is not at the end of the file
				while(!prefixMap.containsKey(currentPrefix)){
					currentPrefix = chooseRandomPrefix(wordList,prefixLength);
				}	
				if(isDebugMode){System.out.println("DEBUG: chose a new initial prefix: " + currentPrefix.toString());} //Debugging
				if(isDebugMode){System.out.println("DEBUG: prefix: " + currentPrefix.toString());} //Debugging
				if(isDebugMode){System.out.println("DEBUG: successors: " + prefixMap.get(currentPrefix).toString());} //Debugging
				//Choose a random successor of the current prefix (choose a random word from the corresponding "myContainer")
				randomWord = prefixMap.get(currentPrefix).getRandomWord();				
				if(isDebugMode){System.out.println("DEBUG: word generated: " + randomWord);} //Debugging
				//Add that chosen random word to the output text
				outputText.add(randomWord);
				//Add the chosen word to the prefix and change the current prefix while keeping prefixLength constant.
				currentPrefix = currentPrefix.shiftIn(randomWord);
			}
		}
		//Convert the arrayList of Strings to one String block in which words are separated by one whitespace.
		return mergeToString(outputText);
	}
	
	/**
	 * Choosing random Prefix in prexixLength from the sample text
	 * @return random chosen Prefix in Prefix type
	 */
	private Prefix chooseRandomPrefix(ArrayList<String> wordList,int prefixLength){
		int i = numberGenerator.nextInt(wordList.size() - (prefixLength) + 1);
		return new Prefix(new ArrayList<String>(wordList.subList(i, i+prefixLength)));
	}	
	
	/**
	 * Merges the String elements of an ArrayList in to one
	 * String in which words are separated with one white space
	 * @param inArr
	 * @return merged String
	 */
	public static String mergeToString(ArrayList<String> inArr){		
		String merged = "";		
		for(int i=0;i<inArr.size()-1;i++){
			merged = merged + inArr.get(i) + " ";			
		}
		merged = merged + inArr.get(inArr.size()-1);
		
		return merged;			
	}
}
