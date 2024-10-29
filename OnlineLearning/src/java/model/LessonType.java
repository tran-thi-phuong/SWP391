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
public class LessonType {
    private int typeID;
    private String name; // Tên chương
    private List<Lesson> lessons;

    public LessonType() {
    }

    public LessonType(int typeID, String name, List<Lesson> lessons) {
        this.typeID = typeID;
        this.name = name;
        this.lessons = lessons;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
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

