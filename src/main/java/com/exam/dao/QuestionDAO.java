package com.exam.dao;

import com.exam.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Question entity operations.
 * Handles adding and fetching MCQ questions for exams.
 */
public class QuestionDAO {

    /**
     * Adds a new multiple choice question to an exam.
     *
     * @param q Question object
     * @return true if successfully inserted, false otherwise
     */
    public boolean addQuestion(Question q) {
        String sql = "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, q.getExamId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getOptionD());
            ps.setString(7, q.getCorrectOption());
            ps.setInt(8, q.getMarks());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all questions associated with a given exam ID.
     *
     * @param examId exam ID
     * @return List of Question objects
     */
    public List<Question> getQuestionsByExamId(int examId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks " +
                     "FROM questions WHERE exam_id = ? ORDER BY question_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question(
                            rs.getInt("question_id"),
                            rs.getInt("exam_id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_option"),
                            rs.getInt("marks")
                    );
                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    /**
     * Calculates the sum of marks of all questions for a given exam.
     *
     * @param examId exam ID
     * @return total marks from all questions
     */
    public int calculateTotalMarksForExam(int examId) {
        String sql = "SELECT COALESCE(SUM(marks), 0) AS total_sum FROM questions WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_sum");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
