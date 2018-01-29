package eus.ehu.tta.gurasapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jontx on 17/01/2018.
 */

public class Forums implements Serializable {

    private int total;
    private List<Forum> forums;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }
}
