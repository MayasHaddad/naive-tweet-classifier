package tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Learner {
	double k = 0.3;
	
	HashMap<Integer, Double> pDeY;
	
	HashMap<Integer, ArrayList<Integer>> tweetsPerClasse;
	
	// i1=mot; i2=numéro de classe; d = le béta 
	HashMap<Integer, HashMap<Integer, Double>> betas;
	
	HashMap<Integer, Double> alphas;
	
	public void fillStructures(HashMap<Integer, ArrayList<Integer>> tweetPerWordsApprentissage, HashMap<Integer, Integer> estimation){

		tweetsPerClasse.put(1,new ArrayList<Integer>());
		tweetsPerClasse.put(2,new ArrayList<Integer>());
		tweetsPerClasse.put(3,new ArrayList<Integer>());
		tweetsPerClasse.put(4,new ArrayList<Integer>());
		
		
		for(Entry<Integer, Integer> entry : estimation.entrySet()){
			
			if(tweetPerWordsApprentissage.containsKey(entry.getKey())){
				
				ArrayList<Integer> arrayOfTweets = tweetsPerClasse.get(entry.getValue());
				arrayOfTweets.add(entry.getKey());
				tweetsPerClasse.put(entry.getValue(), arrayOfTweets);
			}
		}
		
		for(Entry<Integer, ArrayList<Integer>> entry : tweetsPerClasse.entrySet()){
			pDeY.put(entry.getKey(), ((double)entry.getValue().size() / (double)tweetPerWordsApprentissage.size()));
		}
	}
	
	public void fillBetaStructure(HashMap<String, Integer> vocabulary, HashMap<Integer, ArrayList<Integer>> tweetPerWordsApprentissage){
		
		for(Entry<String, Integer> entry : vocabulary.entrySet()){
			
			
			
			int word = entry.getValue();
			
			HashMap<Integer, Double> betasPerClasse = new HashMap<Integer, Double>();
			
			for(Entry<Integer, ArrayList<Integer>> entry2 : tweetsPerClasse.entrySet()){
				
				int sum = 0;
				
				for(int tweet : entry2.getValue()){
					if(tweetPerWordsApprentissage.get(tweet).contains(word)){
						sum++;
					}
				}
				
				double beta = (double)Math.max(k, Math.min(sum, entry2.getValue().size()-k)) ;
				betasPerClasse.put(entry2.getKey(), beta / (double)entry2.getValue().size());
			}
			betas.put(word, betasPerClasse);
		}
	}
	
	public void fillAlphaStructure(HashMap<String, Integer> vocabulary){
		
		alphas.put(1, 0D);
		alphas.put(2, 0D);
		alphas.put(3, 0D);
		alphas.put(4, 0D);
		
		for(Entry<String, Integer> entry : vocabulary.entrySet()){
			// TODO
			Integer word = entry.getValue();
			for(Entry<Integer, Double> entry2 : betas.get(word).entrySet()){
				double unMoinsBeta = 1 - entry2.getValue();
				double oldValue = alphas.get(entry2.getKey());
				double newValue = oldValue + Math.log(unMoinsBeta);
				alphas.put(entry2.getKey(), newValue);
			} 
		}
		
		for(Entry<Integer, Double> entry : alphas.entrySet()){
			alphas.put(entry.getKey(), entry.getValue());
		}
	}
	public Learner(){
		 pDeY = new HashMap<Integer, Double>();
		
		 tweetsPerClasse = new HashMap<Integer, ArrayList<Integer>>();
		
		// i1=mot; i2=numéro de classe; d = le béta 
		 betas = new HashMap<Integer, HashMap<Integer, Double>>();
		 
		 alphas = new HashMap<Integer, Double>();
	}
}
