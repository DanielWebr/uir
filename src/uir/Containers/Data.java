package uir.Containers;

import java.util.ArrayList;

public class Data {
    private ArrayList<String> classes;
    private String text;

    public Data(ArrayList<String> classes,  String text ) {
        this.classes = classes;
        this.text = text;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public String getText() {
        return text;
    }
}
