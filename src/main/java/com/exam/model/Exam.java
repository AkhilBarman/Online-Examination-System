package com.exam.model;

import java.io.Serializable;

/**
 * Model class representing an Examination.
 */
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    private int examId;
    private String title;
    private int durationMinutes;
    private int totalMarks;

    public Exam() {
    }

    public Exam(String title, int durationMinutes, int totalMarks) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
    }

    public Exam(int examId, String title, int durationMinutes, int totalMarks) {
        this.examId = examId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", title='" + title + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", totalMarks=" + totalMarks +
                '}';
    }
}
