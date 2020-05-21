package uir.Parametrizators;

import uir.Containers.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TFIDFParametrizator extends Parametrizator {
    ArrayList<Integer> wordCount= new ArrayList<>();
    ArrayList<String> dictionary= new ArrayList<>();
    int dictionaryWords = 0;

    public TFIDFParametrizator(ArrayList<Data> trainData){


        for(Data data:trainData){
            String[] words = data.getText().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            for(String word:words){
                dictionaryWords++;
                if(!dictionary.contains(word)){
                    dictionary.add(word);
                    wordCount.add(1);
                }else{
                    int index = dictionary.indexOf(word);
                    wordCount.set(index,wordCount.get(index)+1);
                }
            }
        }
    }


    @Override
    public double[] getParameter(String text) {
        double[] parameter=new double[dictionary.size()];
        String[] words=text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for(int index = 0; index <dictionary.size();index++){
            String word = dictionary.get(index);
            int countInDocument = contains(words, word);
            int countInCluster = wordCount.get(index);
            double rateInDocument = countInDocument/(double)text.length();
            double rateInCluster= Math.log10(countInCluster/(double)dictionaryWords);
            parameter[index]=rateInDocument*rateInCluster;
        }
        return parameter;
    }

    private int contains(String[] text, String searchedWord){
        int count =0;
        for(String word : text){
            if(word.equals(searchedWord))count++;
        }
        return count;
    }

}
