package uir;

import uir.Classificators.BayesClassificator;
import uir.Classificators.Classificator;
import uir.Classificators.KmeansClassificator;
import uir.Containers.Data;
import uir.Containers.ParamData;
import uir.Parametrizators.BOWParametrizator;
import uir.Parametrizators.Custom2Parametrizator;
import uir.Parametrizators.Custom3Parametrizator;
import uir.Parametrizators.Parametrizator;

import java.util.ArrayList;

public class Model {
    private Classificator classificator;
    private Parametrizator parametrizator;

    public Model(ClassificatorType classificatorType, ParametrizatorType parametrizatorType, ArrayList<Data> trainData, ArrayList<String> classes){

        switch(parametrizatorType){
            case BOW:{
                parametrizator = new BOWParametrizator(trainData);
                break;
            }
            case CUSTOM2:{
                parametrizator = new Custom2Parametrizator();
                break;
            }
            case CUSTOM3:{
                parametrizator = new Custom3Parametrizator();
                break;
            }
            default:
        }

        ArrayList<ParamData> paramData = new ArrayList<>();
        for(Data data : trainData){
            byte[] parameter = parametrizator.getParameter(data.getText());
            paramData.add(new ParamData(data.getClasses(),parameter));
        }

        switch(classificatorType){
            case BAYES:{
                classificator = new BayesClassificator(classes,paramData);
                break;
            }
            case KMEANS:{
                classificator = new KmeansClassificator(classes,paramData);
                break;
            }
            default:
        }
    }

    public String getClass(String text){
        byte[] parameter = parametrizator.getParameter(text);
        return classificator.getClass(parameter);
    }

}
