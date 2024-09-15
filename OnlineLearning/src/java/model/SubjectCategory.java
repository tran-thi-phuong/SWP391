/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class SubjectCategory {

    private int subjectCategoryId;
    private String title;

    // Default constructor
    public SubjectCategory() {
    }

    // Parameterized constructor
    public SubjectCategory(int subjectCategoryId, String title) {
        this.subjectCategoryId = subjectCategoryId;
        this.title = title;
    }

    // Getters and Setters

    public int getSubjectCategoryId() {
        return subjectCategoryId;
    }

    public void setSubjectCategoryId(int subjectCategoryId) {
        this.subjectCategoryId = subjectCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SubjectCategory{" +
                "subjectCategoryId=" + subjectCategoryId +
                ", title='" + title + '\'' +
                '}';
    }
}

