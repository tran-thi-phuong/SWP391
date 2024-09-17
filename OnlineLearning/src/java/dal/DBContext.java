package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FPT University - PRJ30X
 */
public class DBContext {
    protected Connection connection;
    public DBContext()
    {
        //@Students: You are allowed to edit user, pass, url variables to fit 
        //your system configuration
        //You can also add more methods for Database Interaction tasks. 
        //But we recommend you to do it in another class
        // For example : StudentDBContext extends DBContext , 
        //where StudentDBContext is located in dal package, 
        try {
            String user = "sa";
            String pass = "123";
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=OnlineLearning";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
































//<!--        <form id="subjectForm" action="show" method="GET">
//            List of Subject:
//            <select name="subject" onchange="this.form.submit()">
//                <option value="all" <c:if test="${empty param.subject}">selected</c:if>>All Subject</option>
//                <c:forEach items="${listSj}" var="i">
//                    <option value="${i.subid}" <c:if test="${param.subject eq i.subid}">selected</c:if>>${i.name}</option>
//                </c:forEach>
//            </select><br>
//        </form>
//
//        <table border="1">
//            <tr>
//                <th>Code</th>
//                <th>Name</th>
//                <th>Date of birth</th>
//                <th>Gender</th>
//                <th>Subject</th>
//                <th>Select</th>
//            </tr>
//            <c:forEach items="${requestScope.listSt}" var="s">
//                <tr>
//                    <th>${s.stid}</th>
//                    <th>${s.name}</th>
//                    <th>${s.dob}</th>
//                    <th>
//                        ${s.gender == 0 ? 'Female' : s.gender == 1 ? 'Male' : ''}
//                    </th>
//                    <th>${s.subject}</th>
//                    <th><a href="show?id=${s.stid}">Select</a></th>
//                </tr>
//            </c:forEach>
//        </table>
//        <br>
//
//        <c:set var="i" value="${requestScope.s}"/>
//        Detail Information
//        <form>
//            <table border="0">
//                <tbody>
//                    <tr>
//                        <td>Code:<input type="text" name="stid" value="${s.stid}"/></td>
//                        <td>Name:<input type="text" name="name" value="${s.name}"/></td>
//                    </tr>
//                    <tr>
//                        <td>Date of birth:<input type="date" name="dob" value="${s.dob}"/></td>
//                        <td>Gender:<input type="radio" name="gender" value="male" ${s.gender == '1' ? 'checked' : ''}/> Male
//                            <input type="radio" name="gender" value="female" ${s.gender == '0' ? 'checked' : ''}/> Female
//                        </td>
//                    </tr>
//                    <tr>
//                        <td> Subject<input type="text" name="sbid" value="${s.subject}"/></td>
//                    </tr>
//                </tbody>
//            </table>
//        </form>-->