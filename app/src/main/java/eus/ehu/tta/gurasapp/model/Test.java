package eus.ehu.tta.gurasapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jontx on 17/01/2018.
 */

public class Test implements Serializable {
    private List<String> question;
    private List<List<String>> answer;
    private List<String> advice;
    private int correctAns;


    public List<String> getQuestion() {
        return question;
    }

    public void setQuestion(List<String> question) {
        this.question = question;
    }

    public List<List<String>> getAnswer() {
        return answer;
    }

    public void setAnswer(List<List<String>> answer) {
        this.answer = answer;
    }

    public List<String> getAdvice() {
        return advice;
    }

    public void setAdvice(List<String> advice) {
        this.advice = advice;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }
}
