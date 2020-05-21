package uir.Containers;

import java.util.ArrayList;

public class ParamData {
    private ArrayList<String> classes;
    private byte[] parameter;

    public ParamData(ArrayList<String> classes, byte[] parameter ) {
        this.classes = classes;
        this.parameter = parameter;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public byte[] getParameter() {
        return parameter;
    }
}
