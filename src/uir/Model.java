package uir;

import uir.Classificators.BayesClassificator;
import uir.Classificators.Classificator;
import uir.Classificators.KmeansClassificator;
import uir.Containers.Data;
import uir.Containers.ParamData;
import uir.Parametrizators.BOWParametrizator;
import uir.Parametrizators.TFIDFParametrizator;
import uir.Parametrizators.NGramParametrizator;
import uir.Parametrizators.Parametrizator;

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private Classificator classificator;
    private Parametrizator parametrizator;

    public Model(ClassificatorType classificatorType, ParametrizatorType parametrizatorType, ArrayList<Data> trainData, ArrayList<String> classes){

        switch(parametrizatorType){
            case BOW:{
                parametrizator = new BOWParametrizator(trainData);
                break;
            }
            case TDIDF:{
                parametrizator = new TFIDFParametrizator(trainData);
                break;
            }
            case NGRAM:{
                parametrizator = new NGramParametrizator(trainData);
                break;
            }
            default:
        }

        ArrayList<ParamData> paramData = new ArrayList<>();
        for(Data data : trainData){
            double[] parameter = parametrizator.getParameter(data.getText());
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
        double[] parameter = parametrizator.getParameter(text);
        return classificator.getClass(parameter);
    }

}
