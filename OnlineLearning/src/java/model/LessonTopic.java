/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author sonna
 */
public class LessonTopic {

    private int topicID;
    private String name; // Tên chương
    private List<Lesson> lessons;

    public LessonTopic() {
    }

    public LessonTopic(int topicID, String name, List<Lesson> lessons) {
        this.topicID = topicID;
        this.name = name;
        this.lessons = lessons;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

}
