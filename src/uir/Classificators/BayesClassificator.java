package uir.Classificators;

import uir.Containers.ParamData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BayesClassificator extends Classificator {
    Map<String,ArrayList<double[]>> training = new HashMap<>();
    int numberOfTrainingData = 0;
    int numberOfUniqueWords = 0;

    public BayesClassificator(ArrayList<String> classes,ArrayList<ParamData> paramData){
        ArrayList<double[]> AllParams = new ArrayList<>();

        for(String classificationClass : classes){
            training.put(classificationClass,new ArrayList<>());
        }

        for(ParamData data : paramData){
            AllParams.add(data.getParameter());
            for(String classificationClass : data.getClasses()){
                if(classificationClass.equals("err"))continue;
                ArrayList<double[]> classificationClassParam = training.get(classificationClass);
                classificationClassParam.add(data.getParameter());
                numberOfTrainingData++;
            }
        }

        double[] wordCountInClass = getWordCountInClass(AllParams);
        numberOfUniqueWords = getNumberOfUniqueWords(wordCountInClass);
    }

    @Override
    public String getClass(double[] parameter) {
        double maxProp = Double.NEGATIVE_INFINITY;
        String maxPropClass = "";
        for(String classificationClass : training.keySet()){
            ArrayList<double[]> classificationClassParam = training.get(classificationClass);
            int classificationClassParamSize = classificationClassParam.size();
            double[] wordCountInClass = getWordCountInClass(classificationClassParam);
            int numberOfWords = getNumberOfWords(wordCountInClass);
            double classProp = Math.log10(classificationClassParamSize/(float)numberOfTrainingData);

            for(int index = 0; index < parameter.length;index++){
                if(parameter[index]==0)continue;
                classProp+=  Math.log10((wordCountInClass[index]+1) / ((double)numberOfWords+numberOfUniqueWords+1));
            }
            if(classProp>maxProp){
                maxProp = classProp;
                maxPropClass=classificationClass;
            }
        }
        return maxPropClass;
    }

    private double[] getWordCountInClass(ArrayList<double[]> classificationClassParam) {
        double[] count = new double[classificationClassParam.get(0).length];
        for(int index = 0; index < count.length;index++){
            int finalIndex = index;
            classificationClassParam.forEach(par -> count[finalIndex]+=par[finalIndex]);
        }
        return count;
    }

    private int getNumberOfWords(double[] param) {
        int count = 0;
        for(int index = 0; index < param.length;index++){
            if(param[index]!=0)count+=param[index];
        }
        return count;
    }

    private int getNumberOfUniqueWords(double[] param) {
        int count = 0;
        for(int index = 0; index < param.length;index++){
            if(param[index]!=0)count++;
        }
        return count;
    }

}
