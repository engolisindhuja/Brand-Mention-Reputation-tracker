package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DatabaseManager;
import utils.SentimentEngine;
import utils.TopicEngine;

@WebServlet("/addMention")
public class AddMentionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String platform = req.getParameter("platform");
        String content = req.getParameter("content");

        String sentiment = SentimentEngine.analyze(content);
        String topic = TopicEngine.detect(content);

        try {
            Connection conn = DatabaseManager.getConnection();

            String sql =
                "INSERT INTO mentions(platform, content, sentiment, topic, posted_on) " +
                "VALUES (?, ?, ?, ?, CURDATE())";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, platform);
            ps.setString(2, content);
            ps.setString(3, sentiment);
            ps.setString(4, topic);

            ps.executeUpdate();

            resp.getWriter().write("success");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("error");
        }
    }
}
