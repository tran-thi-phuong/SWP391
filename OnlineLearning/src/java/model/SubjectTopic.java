/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuant
 */
public class SubjectTopic {
    private String topicName;
    private int topicID, subjectID, order;

    public SubjectTopic() {
    }

    public SubjectTopic(String topicName, int topicID, int subjectID, int order) {
        this.topicName = topicName;
        this.topicID = topicID;
        this.subjectID = subjectID;
        this.order = order;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "LessonSubject{" + "topicName=" + topicName + ", topicID=" + topicID + ", subjectID=" + subjectID + ", order=" + order + '}';
    }
    
}
