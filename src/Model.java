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

    public void train(String text, ArrayList<String> data){
        //TODO natrenovat model
    }

    public String getClass(String text){
        double parameter = parametrizator.getParameter(text);
        return classificator.getClass(parameter);
    }

}
