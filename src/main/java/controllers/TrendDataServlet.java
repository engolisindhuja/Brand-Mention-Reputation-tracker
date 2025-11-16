package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DatabaseManager;

@WebServlet("/trendData")
public class TrendDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONArray arr = new JSONArray();

        try (Connection conn = DatabaseManager.getConnection()) {

            String sql =
                "SELECT posted_on, COUNT(*) AS cnt " +
                "FROM mentions " +
                "GROUP BY posted_on " +
                "ORDER BY posted_on ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("date", rs.getString("posted_on"));
                obj.put("count", rs.getInt("cnt"));
                arr.put(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.getWriter().write(arr.toString());
    }
}
