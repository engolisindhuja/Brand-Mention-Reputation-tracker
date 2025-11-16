package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DatabaseManager;

@WebServlet("/topics")
public class TopicClusterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject topics = new JSONObject();
        topics.put("delivery", 0);
        topics.put("quality", 0);
        topics.put("price", 0);
        topics.put("support", 0);
        topics.put("experience", 0);

        try (Connection conn = DatabaseManager.getConnection()) {

            String sql = "SELECT content FROM mentions";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String text = rs.getString("content").toLowerCase();

                if (text.contains("delivery") || text.contains("shipping"))
                    topics.put("delivery", topics.getInt("delivery") + 1);

                if (text.contains("quality") || text.contains("good") || text.contains("bad"))
                    topics.put("quality", topics.getInt("quality") + 1);

                if (text.contains("price") || text.contains("expensive") || text.contains("cheap"))
                    topics.put("price", topics.getInt("price") + 1);

                if (text.contains("support") || text.contains("help"))
                    topics.put("support", topics.getInt("support") + 1);

                if (text.contains("experience") || text.contains("love") || text.contains("hate"))
                    topics.put("experience", topics.getInt("experience") + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.getWriter().write(topics.toString());
    }
}
