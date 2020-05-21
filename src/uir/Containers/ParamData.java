package uir.Containers;

import java.util.ArrayList;

public class ParamData {
    private ArrayList<String> classes;
    private double[] parameter;

    public ParamData(ArrayList<String> classes, double[] parameter ) {
        this.classes = classes;
        this.parameter = parameter;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public double[] getParameter() {
        return parameter;
    }
}
