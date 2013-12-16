package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Loader {
	
	public HashMap<String, Integer> vocabulary;
	
	public HashMap<Integer, ArrayList<Integer>> tweetPerWordsApprentissage;
	
	public HashMap<Integer, ArrayList<Integer>> tweetPerWordsEvaluation;
	
	public HashMap<Integer, Integer> estimation;
	// positif = 1; negatif = 2; neutre = 3; 4 = irrelevant
	
	
	public int getClassNumber(String classe){
		
		if(classe.equals("positive")){
			return 1;
		}else if(classe.equals("negative")){
			return 2;
		}else if(classe.equals("neutral")){
			return 3;
		} 
			return 4;		
	}
	
	public void fillEvaluationStructure(){
		int numberOfEntries = this.tweetPerWordsApprentissage.size();
		int ratio = (int) (0.2 * numberOfEntries);
		while(ratio > 0){
			int randomIndex = (int)(Math.random() * (numberOfEntries-1)) + 1;
			if(tweetPerWordsApprentissage.get(randomIndex) != null){
				tweetPerWordsEvaluation.put(randomIndex, tweetPerWordsApprentissage.get(randomIndex));
				tweetPerWordsApprentissage.remove(randomIndex);
				ratio--;
			}
		}
	}
	
	public void fillStructures(String filePath) throws IOException{
		
		InputStream ips = new FileInputStream(filePath);
		InputStreamReader ipsr = new InputStreamReader(ips, "UTF-8"); 
		BufferedReader br = new BufferedReader(ipsr);
		
		String line = br.readLine();
		int nTweet = 1;
		int wordNumber = 1;
		while(line != null){
			String[] content = line.split(" ");
			String[] markers = content[0].split(",");
			
			String classe = markers[0].substring(1, markers[0].length());
			String mark = markers[1].substring(0, markers[1].length() - 1);
			
			estimation.put(nTweet, getClassNumber(classe));
			ArrayList<Integer> tweetWordsNumbers = new ArrayList<Integer>();
			tweetWordsNumbers.clear();
			for(int i = 1; i < content.length; i++){
				
				if(vocabulary.get(content[i]) == null){
					vocabulary.put(content[i], wordNumber);
					
					tweetWordsNumbers.add(wordNumber);
					wordNumber++;
				}else{
					tweetWordsNumbers.add(vocabulary.get(content[i]));
				}
			}
			tweetPerWordsApprentissage.put(nTweet, tweetWordsNumbers);
			line = br.readLine();
			nTweet++;
		}
		br.close();
	}
	
	public Loader(){
		vocabulary = new HashMap<String, Integer>();
		tweetPerWordsApprentissage = new HashMap<Integer, ArrayList<Integer>>();
		tweetPerWordsEvaluation = new HashMap<Integer, ArrayList<Integer>>();
		estimation = new HashMap<Integer, Integer>();
	}
}