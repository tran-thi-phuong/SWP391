/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Lesson;
import model.LessonSubject;
/**
 *
 * @author tuant
 */
public class LessonDAO extends DBContext{
    public List<LessonSubject> getAllLessonTypeBySubjectId(int subjectID){
        List<LessonSubject> list = new ArrayList<>();
        String sql = "select lt.Name, ls.TypeID, ls.SubjectID, ls.[Order] from Lesson_Subject ls join LessonType lt "
                + "on ls.TypeID = lt.TypeID where SubjectID = ? order by [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);         
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LessonSubject lessonSubject = new LessonSubject();
                    lessonSubject.setTypeName(rs.getString("Name"));
                    lessonSubject.setTypeID(rs.getInt("TypeID"));
                    lessonSubject.setSubjectID(rs.getInt("SubjectID"));
                    lessonSubject.setOrder(rs.getInt("Order"));
                    list.add(lessonSubject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Lesson> getAllLessonBySubjectId(int subjectID){
        List<Lesson> list = new ArrayList<>();
        String sql = "select * from Lessons where SubjectID = ? order by TypeID, [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);         
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setSubjectID(rs.getInt("SubjectID"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setTypeID(rs.getInt("TypeID"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setOrder(rs.getInt("Order"));
                    lesson.setDescription(rs.getString("Description"));
                    lesson.setStatus(rs.getString("Status"));
                    list.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static void main(String[] args) {
        LessonDAO lDAO = new LessonDAO();

    // Kiểm tra hàm getAllLessonBySubjectId
    System.out.println("List of Lessons for Subject ID 11:");
    List<Lesson> lessons = lDAO.getAllLessonBySubjectId(1);
    for (Lesson lesson : lessons) {
        System.out.println("Lesson ID: " + lesson.getLessonID());
        System.out.println("Title: " + lesson.getTitle());
        System.out.println("Type ID: " + lesson.getTypeID());
        System.out.println("Content: " + lesson.getContent());
        System.out.println("Order: " + lesson.getOrder());
        System.out.println("Description: " + lesson.getDescription());
        System.out.println("Status: " + lesson.getStatus());
        System.out.println("--------------");
    }

    // Kiểm tra hàm getAllLessonTypeBySubjectId
    System.out.println("List of Lesson Types for Subject ID 11:");
    List<LessonSubject> lessonSubjects = lDAO.getAllLessonTypeBySubjectId(1);
    for (LessonSubject lessonSubject : lessonSubjects) {
        System.out.println("Type Name: " + lessonSubject.getTypeName());
        System.out.println("Type ID: " + lessonSubject.getTypeID());
        System.out.println("Subject ID: " + lessonSubject.getSubjectID());
        System.out.println("Order: " + lessonSubject.getOrder());
        System.out.println("--------------");
    }
    }
}
