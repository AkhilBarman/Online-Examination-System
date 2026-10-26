package com.exam.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing an Exam Result for a student.
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private int resultId;
    private int userId;
    private int examId;
    private int score;
    private int totalMarks;
    private Timestamp takenOn;

    // Additional fields for display convenience
    private String userName;
    private String userEmail;
    private String examTitle;

    public Result() {
    }

    public Result(int userId, int examId, int score, int totalMarks) {
        this.userId = userId;
        this.examId = examId;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public Result(int resultId, int userId, int examId, int score, int totalMarks, Timestamp takenOn) {
        this.resultId = resultId;
        this.userId = userId;
        this.examId = examId;
        this.score = score;
        this.totalMarks = totalMarks;
        this.takenOn = takenOn;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Timestamp getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(Timestamp takenOn) {
        this.takenOn = takenOn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    /**
     * Calculates the percentage scored.
     */
    public double getPercentage() {
        if (totalMarks <= 0) return 0.0;
        double pct = ((double) score / totalMarks) * 100.0;
        return Math.round(pct * 100.0) / 100.0; // round to 2 decimal places
    }

    /**
     * Evaluates PASS or FAIL based on 40% passing benchmark.
     */
    public String getStatus() {
        return getPercentage() >= 40.0 ? "PASS" : "FAIL";
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultId=" + resultId +
                ", userId=" + userId +
                ", examId=" + examId +
                ", score=" + score +
                ", totalMarks=" + totalMarks +
                ", status=" + getStatus() +
                '}';
    }
}
