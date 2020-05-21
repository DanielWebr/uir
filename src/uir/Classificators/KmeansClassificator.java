package uir.Classificators;

import uir.Containers.ParamData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KmeansClassificator extends Classificator {
    Map<String,double[]> training = new HashMap<>();

    public KmeansClassificator(ArrayList<String> classes, ArrayList<ParamData> paramData){

        for(String classificationClass : classes){
            double[] center = new double[paramData.get(0).getParameter().length];

            for(ParamData pd : paramData){
                if(!pd.getClasses().contains(classificationClass))continue;
                byte[] parameter = pd.getParameter();
                for(int index = 0; index < parameter.length; index++){
                    center[index]+= parameter[index];
                }
            }

            for(int index = 0; index < center.length; index++){
                center[index]/= center.length;
            }

            training.put(classificationClass,center);
        }


    }

    @Override
    public String getClass(byte[] parameter) {
        double minDistance = Double.MAX_VALUE;
        String minDistanceClass = "";
        for(String classificationClass : training.keySet()){
            double[] center = training.get(classificationClass);
            double sum = 0;
            for(int index = 0; index < parameter.length; index++){
                double diff = parameter[index]-center[index];
                sum+=diff*diff;
            }
            double distance = Math.sqrt(sum);
            //System.out.print(classificationClass+"  "+distance + " ");
            if(distance<minDistance){
                minDistance=distance;
                minDistanceClass=classificationClass;
            }
        }
        return minDistanceClass;
    }

}
