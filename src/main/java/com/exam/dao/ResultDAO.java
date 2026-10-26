package com.exam.dao;

import com.exam.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Result entity operations.
 * Handles saving student submissions, querying past attempts, and generating scorecards.
 */
public class ResultDAO {

    /**
     * Records a new exam result and returns the generated result ID.
     *
     * @param result Result object
     * @return generated resultId or -1 if failed
     */
    public int addResult(Result result) {
        String sql = "INSERT INTO results (user_id, exam_id, score, total_marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, result.getUserId());
            ps.setInt(2, result.getExamId());
            ps.setInt(3, result.getScore());
            ps.setInt(4, result.getTotalMarks());

            int affected = ps.executeUpdate();
            if (affected > 0) {
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
     * Checks if a student has already attempted a particular exam.
     * Prevents multiple attempts by the same student.
     *
     * @param userId student user ID
     * @param examId exam ID
     * @return true if already attempted, false otherwise
     */
    public boolean hasStudentAttempted(int userId, int examId) {
        String sql = "SELECT result_id FROM results WHERE user_id = ? AND exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, examId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves an exam result by result ID, including exam title and student details.
     *
     * @param resultId result ID
     * @return Result object or null
     */
    public Result getResultById(int resultId) {
        String sql = "SELECT r.result_id, r.user_id, r.exam_id, r.score, r.total_marks, r.taken_on, " +
                     "e.title AS exam_title, u.name AS user_name, u.email AS user_email " +
                     "FROM results r " +
                     "JOIN exams e ON r.exam_id = e.exam_id " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.result_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Result res = new Result(
                            rs.getInt("result_id"),
                            rs.getInt("user_id"),
                            rs.getInt("exam_id"),
                            rs.getInt("score"),
                            rs.getInt("total_marks"),
                            rs.getTimestamp("taken_on")
                    );
                    res.setExamTitle(rs.getString("exam_title"));
                    res.setUserName(rs.getString("user_name"));
                    res.setUserEmail(rs.getString("user_email"));
                    return res;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all results for a specific student.
     *
     * @param userId student user ID
     * @return List of Result objects
     */
    public List<Result> getResultsByUserId(int userId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT r.result_id, r.user_id, r.exam_id, r.score, r.total_marks, r.taken_on, " +
                     "e.title AS exam_title " +
                     "FROM results r " +
                     "JOIN exams e ON r.exam_id = e.exam_id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.taken_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Result res = new Result(
                            rs.getInt("result_id"),
                            rs.getInt("user_id"),
                            rs.getInt("exam_id"),
                            rs.getInt("score"),
                            rs.getInt("total_marks"),
                            rs.getTimestamp("taken_on")
                    );
                    res.setExamTitle(rs.getString("exam_title"));
                    results.add(res);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves all exam results across all students (for Admin view).
     *
     * @return List of Result objects with student and exam details
     */
    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT r.result_id, r.user_id, r.exam_id, r.score, r.total_marks, r.taken_on, " +
                     "e.title AS exam_title, u.name AS user_name, u.email AS user_email " +
                     "FROM results r " +
                     "JOIN exams e ON r.exam_id = e.exam_id " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "ORDER BY r.taken_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Result res = new Result(
                        rs.getInt("result_id"),
                        rs.getInt("user_id"),
                        rs.getInt("exam_id"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"),
                        rs.getTimestamp("taken_on")
                );
                res.setExamTitle(rs.getString("exam_title"));
                res.setUserName(rs.getString("user_name"));
                res.setUserEmail(rs.getString("user_email"));
                results.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
