package eus.ehu.tta.gurasapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jontx on 17/01/2018.
 */

public class Tests implements Serializable {

    private int total;
    private List<Test> tests;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
