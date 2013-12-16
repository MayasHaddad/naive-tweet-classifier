package tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Classifier {

	public HashMap<Integer, Integer> predictions;

	public int classify(ArrayList<Integer> tweet, HashMap<Integer, HashMap<Integer, Double>> betas, HashMap<Integer, Double> alphas, HashMap<Integer, Double> pDeY){

		Map<Integer, Double> probaPerClasse = new HashMap<Integer, Double>();

		for(int i = 1; i <= 4; i++){
			double sumBeta = 0D;
			double sumUnMoinsBeta = 0D;
			
			for(int word : tweet){
				try{
					sumBeta += Math.log(betas.get(word).get(i));
					sumUnMoinsBeta += Math.log(1 - betas.get(word).get(i));
				}catch (NullPointerException e){
					System.out.println("word " + word + " not in the vocabulary!");
				}
			}
			Double proba = alphas.get(i) + sumBeta - sumUnMoinsBeta;

			Double pDeYSachantX = proba + Math.log(pDeY.get(i));

			probaPerClasse.put(i, pDeYSachantX);
		}
		
		int classe = 1;
		double maxProba = (double)Integer.MIN_VALUE;

		for(Entry<Integer, Double> entry : probaPerClasse.entrySet()){

			if(entry.getValue() > maxProba){
				maxProba = entry.getValue();
				classe = entry.getKey();
			}
		}
		return classe;
	}

	public void classifyGroup(HashMap<Integer, ArrayList<Integer>> tweetPerWordsEvaluation, HashMap<Integer, HashMap<Integer, Double>> betas, HashMap<Integer, Double> alphas, HashMap<Integer, Double> pDeY){

		for(Entry<Integer, ArrayList<Integer>> entry : tweetPerWordsEvaluation.entrySet()){
			predictions.put(entry.getKey(), classify(entry.getValue(), betas, alphas, pDeY));
		}
	}

	public double getErrorRate(HashMap<Integer, Integer> predictions, HashMap<Integer, Integer> estimation){
		int erreur = 0;
		for(Entry<Integer, Integer> entry : predictions.entrySet()){
			int nTweet = entry.getKey();
			if(estimation.get(nTweet) != entry.getValue()){
				erreur++;
			}
		}
		return (double)erreur / predictions.size();
	}

	public Classifier(){
		predictions = new HashMap<Integer, Integer>();
	}

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Loader l = new Loader();
		Learner ln = new Learner();
		Classifier c = new Classifier();
		l.fillStructures("/net/k3/u/etudiant/mhadda1/ECT/twitter/train.txt");
		l.fillEvaluationStructure();
		ln.fillStructures(l.tweetPerWordsApprentissage, l.estimation);
		ln.fillBetaStructure(l.vocabulary, l.tweetPerWordsApprentissage);
		ln.fillAlphaStructure(l.vocabulary);
		c.classifyGroup(l.tweetPerWordsEvaluation, ln.betas, ln.alphas, ln.pDeY);
		System.out.println(c.getErrorRate(c.predictions, l.estimation));
		//System.out.println(c.predictions);
	}

}
