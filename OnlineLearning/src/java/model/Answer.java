/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class Answer {
    private int answerID;
    private int questionID;
    private String content;
    private String explanation;
    private boolean isCorrect;

    // Constructor
    public Answer(int questionID, String content, String explanation, boolean isCorrect) {
        this.questionID = questionID;
        this.content = content;
        this.explanation = explanation;
        this.isCorrect = isCorrect;
    }
    // Getters and Setters
    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    // Alternative getter for compatibility
    public boolean getisCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerID=" + answerID +
                ", questionID=" + questionID +
                ", content='" + content + '\'' +
                ", explanation='" + explanation + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}

