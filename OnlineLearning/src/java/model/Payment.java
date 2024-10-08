/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
import java.sql.Date;

public class Payment {
    private int paymentId;
    private int userId;
    private int subjectId;
    private Date paymentDate;
    private double amount;
    private String paymentMethod;

    public Payment() {
    }

    public Payment(int paymentId, int userId, int subjectId, Date paymentDate, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.subjectId = subjectId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment{" + "paymentId=" + paymentId + ", userId=" + userId + ", subjectId=" + subjectId + ", paymentDate=" + paymentDate + ", amount=" + amount + ", paymentMethod=" + paymentMethod + '}';
    }

}

