package uir.Parametrizators;

import uir.Containers.Data;

import java.util.*;

public class TFIDFParametrizator extends Parametrizator {
    ArrayList<Integer> wordCount= new ArrayList<>();
    ArrayList<String> dictionary= new ArrayList<>();
    int numOfdocuments = 0;

    public TFIDFParametrizator(ArrayList<Data> trainData){
        numOfdocuments = trainData.size();
        for(Data data:trainData){
            String[] words = data.getText().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            Set<String> uniqueWords = new HashSet<String>(Arrays.asList(words));
            for(String word:uniqueWords){
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
            double countInDocument = contains(words, word);
            double countInClusterDocuments= Math.log10(numOfdocuments/(double)wordCount.get(index));
            parameter[index]=countInDocument*countInClusterDocuments;
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
