package uir.Classificators;

import uir.Containers.ParamData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BayesClassificator extends Classificator {
    Map<String,int[]> training = new HashMap<>();
    int numberOfTrainingData = 0;

    public BayesClassificator(ArrayList<String> classes,ArrayList<ParamData> paramData){
        for(String classificationClass : classes){
            training.put(classificationClass,new int[paramData.get(0).getParameter().length]);
        }

        for(ParamData data : paramData){
            byte [] param = data.getParameter();
            for(String classificationClass : data.getClasses()){
                int[] classificationClassParam = training.get(classificationClass);
                for(int index = 0; index < param.length;index++){
                    classificationClassParam[index]+=param[index];
                }
            }
        }

    }

    @Override
    public String getClass(byte[] parameter) {
        double maxProp = Double.NEGATIVE_INFINITY;
        String maxPropClass = "";
        for(String classificationClass : training.keySet()){
            ArrayList<byte[]> classificationClassParam = training.get(classificationClass);
            double classProp = classificationClassParam.size()/(float)numberOfTrainingData;
            System.out.print(classificationClass+" ");
            System.out.print(classificationClassParam.size()+"/"+numberOfTrainingData) ;
            for(int index = 0; index < parameter.length;index++){
                if(parameter[index]==0)continue;
               System.out.print(" * "+getNumberOfSimilar(index,classificationClassParam, parameter)+"/"+((float)classificationClassParam.size()+1)) ;
                classProp*= getNumberOfSimilar(index,classificationClassParam, parameter)/((float)classificationClassParam.size()+1);
            }
            System.out.println(" = "+classProp+" ");
            if(classProp>maxProp){
                maxProp = classProp;
                maxPropClass=classificationClass;
            }
        }
        return maxPropClass;
    }

    private int getNumberOfSimilar(int index,ArrayList<byte[]> trainingParameters,byte[] parameter){
        int count = 0;
        for(byte[] trainingParam : trainingParameters){
            if(trainingParam[index]==parameter[index]){
                count++;
            }
        }
        return count+1;
    }

}
