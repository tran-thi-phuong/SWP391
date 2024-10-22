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
        System.out.println(lDAO.getAllLessonBySubjectId(11));
    }
}
