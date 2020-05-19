import Classificators.Classificator;
import Parametrizators.Parametrizator;

import java.io.File;
import java.util.ArrayList;

public class Model {
    private Classificator classificator;
    private Parametrizator parametrizator;

    public Model(Classificator classificator,Parametrizator parametrizator){
        this.classificator = classificator;
        this.parametrizator = parametrizator;
    }

    public void train(File[] trainData){
        //TODO natrenovat model
    }

    public double test(File[] testData){
        double testingDataLength = testData.length;
        int numOfErrors = 0;

        //TODO otestovat vse pomoci getClass a porovnat vysledky se spravnymi daty

        double acc = 1-(numOfErrors/testingDataLength);
        return acc;
    }

    public String getClass(String text){
        double parameter = parametrizator.getParameter(text);
        return classificator.getClass(parameter);
    }


    private Data getDataFromFile(){
        ArrayList<String> classes = new ArrayList<String>();
        String text = "";

        //TODO vyndat z filu tridy a text

        return new Data(classes,text);
    }


}
