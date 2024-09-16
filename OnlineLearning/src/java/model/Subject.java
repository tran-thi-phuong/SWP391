/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */
public class Subject {
    private int SubjectId;
    private String title;
    private String description;
    int Subject_CategoryId;

    public Subject() {
    }

    public Subject(int SubjectId, String title, String description, int Subject_CategoryId) {
        this.SubjectId = SubjectId;
        this.title = title;
        this.description = description;
        this.Subject_CategoryId = Subject_CategoryId;
    }

    public int getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(int SubjectId) {
        this.SubjectId = SubjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSubject_CategoryId() {
        return Subject_CategoryId;
    }

    public void setSubject_CategoryId(int Subject_CategoryId) {
        this.Subject_CategoryId = Subject_CategoryId;
    }
    
    
}
