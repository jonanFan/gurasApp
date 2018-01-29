package eus.ehu.tta.gurasapp.model;

import java.io.Serializable;

/**
 * Created by jontx on 28/01/2018.
 */

public class Forum implements Serializable {

    private String title;
    private String question;
    private String answer;
    private String teacher;
    private int date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
