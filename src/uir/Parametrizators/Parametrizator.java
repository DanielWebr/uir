package uir.Parametrizators;

import java.io.Serializable;

public abstract class Parametrizator implements Serializable {
    private static final long serialVersionUID = 2L;
    public abstract double[] getParameter(String text);
}
