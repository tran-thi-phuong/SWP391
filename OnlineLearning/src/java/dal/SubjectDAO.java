/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author sonna
 */
import model.Subject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO extends DBContext {
    public List<Subject> getFeaturedSubjects() {
        List<Subject> featuredSubjects = new ArrayList<>();
        String sql = "SELECT TOP 5 * FROM Subjects ORDER BY Update_Date DESC";
        try (
             PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Subject subject = new Subject();
                // Set subject properties from ResultSet
                featuredSubjects.add(subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return featuredSubjects;
    }

    public static void main(String[] args) {
        SubjectDAO dao = new SubjectDAO();
        List<Subject> subjects = dao.getFeaturedSubjects();
        System.out.println("Subjects:");
        for (Subject subject : subjects) {
            System.out.println(subject.getTitle() + " - " + subject.getDescription());
        }
    }
}

