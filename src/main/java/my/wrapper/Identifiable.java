package my.wrapper;

import java.io.Serializable;

public interface Identifiable<ID extends Number> extends Serializable {
    ID getIdentifier();
    void setIdentifier(ID id);
}
