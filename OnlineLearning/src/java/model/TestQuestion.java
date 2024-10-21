/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class TestQuestion {
    private int testID;
    private int questionID;

    // Getters and Setters

    public TestQuestion() {
    }

    public TestQuestion(int testID, int questionID) {
        this.testID = testID;
        this.questionID = questionID;
    }

    
    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }
}

