/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author sonna
 */
public class Registration {
    private int registrationID;
    private int userID;
    private int subjectID;
    private int packageID;
    private String totalCost;
    private Date registrationTime;
    private Date validFrom;
    private Date validTo;
    private String status;
    private int staffID;
    private String note;
    private int campaignID;
    
    public Registration() {}

    public Registration(int registrationID, int userID, int subjectID, int packageID, String totalCost, Date registrationTime, Date validFrom, Date validTo, String status, int staffID, String note, int campaignID) {
        this.registrationID = registrationID;
        this.userID = userID;
        this.subjectID = subjectID;
        this.packageID = packageID;
        this.totalCost = totalCost;
        this.registrationTime = registrationTime;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = status;
        this.staffID = staffID;
        this.note = note;
        this.campaignID = campaignID;
    }

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(int campaignID) {
        this.campaignID = campaignID;
    }
}
