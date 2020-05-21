package uir.Parametrizators;
import uir.Containers.Data;

import java.util.*;

//bagsofwords
public class BOWParametrizator extends Parametrizator{
    private int MAX_DICTIONARY_SIZE = 4000;
    private int NUM_OF_MOST_FREQ_WORD_IGNOR = 0;
   private ArrayList<String> dictionary= new ArrayList<>();

    public BOWParametrizator(ArrayList<Data> trainData){
        ArrayList<Integer> wordCount= new ArrayList<>();
        ArrayList<String> localDictionary= new ArrayList<>();

        for(Data data:trainData){
            String[] words = data.getText().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            for(String word:words){
                if(!localDictionary.contains(word)){
                    localDictionary.add(word);
                    wordCount.add(1);
                }else{
                    int index = localDictionary.indexOf(word);
                    wordCount.set(index,wordCount.get(index)+1);
                }
            }
        }

        ArrayList<Integer> mapValuesSorted = new ArrayList<>(wordCount);
        mapValuesSorted.sort(Collections.reverseOrder());
        for(int countOfWordIndex = NUM_OF_MOST_FREQ_WORD_IGNOR; countOfWordIndex<MAX_DICTIONARY_SIZE+NUM_OF_MOST_FREQ_WORD_IGNOR && countOfWordIndex<mapValuesSorted.size(); countOfWordIndex++){
            int countWord = mapValuesSorted.get(countOfWordIndex);
            for(int i =0; i< localDictionary.size();i++){
                String word = localDictionary.get(i);
                if(word.equals(""))continue;
                int count = wordCount.get(i);
                if(countWord==count){
                    dictionary.add(word);
                    //System.out.println(countOfWordIndex+" "+countWord+" "+ word);
                    localDictionary.set(i,"");
                    break;
                }
            }
        }
        //System.out.println(dictionary.toString());
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
