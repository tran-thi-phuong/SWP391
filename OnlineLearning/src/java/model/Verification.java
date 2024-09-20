/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuant
 */
public class Verification {
    private int userID;
    private String code;

    public Verification() {
    }

    public Verification(int userID, String code) {
        this.userID = userID;
        this.code = code;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "verificationDAO{" + "userID=" + userID + ", code=" + code + '}';
    }
    
}
