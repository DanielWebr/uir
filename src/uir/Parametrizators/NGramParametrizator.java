package uir.Parametrizators;
import uir.Containers.Data;

import java.util.*;

public class NGramParametrizator extends Parametrizator{
    private int N = 2;
    private int MAX_DICTIONARY_SIZE = 15000;
    private ArrayList<String> dictionary= new ArrayList<>();

    public NGramParametrizator(ArrayList<Data> trainData){
        ArrayList<Integer> gramCount= new ArrayList<>();
        ArrayList<String> localDictionary= new ArrayList<>();

        for(Data data:trainData){
            String[] words = data.getText().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            for(int i = 0; i<words.length-N;i++){
                String gram = "";
                for(int a = 0; a < N; a++){
                    gram +=  words[i+a];
                }
                if(!localDictionary.contains(gram)){
                    localDictionary.add(gram);
                    gramCount.add(1);
                }else{
                    int gramIndex = localDictionary.indexOf(gram);
                    gramCount.set(gramIndex,gramCount.get(gramIndex)+1);
                }
            }
        }

        ArrayList<Integer> countSorted = new ArrayList<>(gramCount);
        countSorted.sort(Collections.reverseOrder());
        for(int countOfWordIndex = 0; countOfWordIndex<MAX_DICTIONARY_SIZE && countOfWordIndex<countSorted.size(); countOfWordIndex++){
            int countWord = countSorted.get(countOfWordIndex);
            for(int i =0; i< localDictionary.size();i++){
                String gram = localDictionary.get(i);
                if(gram.equals(""))continue;
                int count = gramCount.get(i);
                if(countWord==count){
                    dictionary.add(gram);
                    localDictionary.set(i,"");
                    break;
                }
            }
        }
    }

    @Override
    public double[] getParameter (String text){
        int dictionarySize=dictionary.size();
        double[]parameter=new double[dictionarySize];
        Iterator<String> dictionaryIterator=dictionary.iterator();
        String[] words=text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

        for(int index = 0;index<dictionarySize;index++){
            String dictionaryWord = dictionaryIterator.next();
            for(String word : words){
                if(dictionaryWord.equals(word)){
                    parameter[index] = 1;
                    break;
                }
            }
        }
        //  System.out.print(Arrays.toString(parameter)+" ");
        return parameter;
    }




}
