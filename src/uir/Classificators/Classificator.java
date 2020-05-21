package uir.Classificators;

import java.io.Serializable;

public abstract class Classificator implements Serializable {
    private static final long serialVersionUID = 3L;
    public abstract String getClass(double[] parameter);

}
