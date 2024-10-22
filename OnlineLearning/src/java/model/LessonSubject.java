/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuant
 */
public class LessonSubject {
    private String typeName;
    private int typeID, subjectID, order;

    public LessonSubject() {
    }

    public LessonSubject(String typeName, int typeID, int subjectID, int order) {
        this.typeName = typeName;
        this.typeID = typeID;
        this.subjectID = subjectID;
        this.order = order;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
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
        return "LessonSubject{" + "typeName=" + typeName + ", typeID=" + typeID + ", subjectID=" + subjectID + ", order=" + order + '}';
    }
    
}
