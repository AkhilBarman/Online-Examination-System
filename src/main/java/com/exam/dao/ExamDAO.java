package com.exam.dao;

import com.exam.model.Exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Exam entity operations.
 * Handles creating, retrieving, updating, and deleting exams.
 */
public class ExamDAO {

    /**
     * Adds a new exam and returns its generated primary key ID.
     *
     * @param exam Exam object
     * @return generated examId, or -1 if failed
     */
    public int addExam(Exam exam) {
        String sql = "INSERT INTO exams (title, duration_minutes, total_marks) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, exam.getTitle());
            ps.setInt(2, exam.getDurationMinutes());
            ps.setInt(3, exam.getTotalMarks());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all exams available in the system.
     *
     * @return List of Exam objects
     */
    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT exam_id, title, duration_minutes, total_marks FROM exams ORDER BY exam_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Exam exam = new Exam(
                        rs.getInt("exam_id"),
                        rs.getString("title"),
                        rs.getInt("duration_minutes"),
                        rs.getInt("total_marks")
                );
                exams.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    /**
     * Retrieves an exam by its primary key ID.
     *
     * @param examId exam ID
     * @return Exam object or null if not found
     */
    public Exam getExamById(int examId) {
        String sql = "SELECT exam_id, title, duration_minutes, total_marks FROM exams WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Exam(
                            rs.getInt("exam_id"),
                            rs.getString("title"),
                            rs.getInt("duration_minutes"),
                            rs.getInt("total_marks")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes an exam by its ID. Dependent questions and results are deleted via CASCADE.
     *
     * @param examId exam ID to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteExam(int examId) {
        String sql = "DELETE FROM exams WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates total marks of an exam (e.g., when questions are added).
     *
     * @param examId     exam ID
     * @param totalMarks new total marks
     * @return true if updated, false otherwise
     */
    public boolean updateExamTotalMarks(int examId, int totalMarks) {
        String sql = "UPDATE exams SET total_marks = ? WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, totalMarks);
            ps.setInt(2, examId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
