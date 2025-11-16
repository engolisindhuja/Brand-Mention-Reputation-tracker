package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import utils.DatabaseManager;
import org.json.JSONObject;

@WebServlet("/sentimentStats")
public class SentimentStatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        int positive = 0, negative = 0, neutral = 0;

        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT sentiment, COUNT(*) AS cnt FROM mentions GROUP BY sentiment";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String s = rs.getString("sentiment");
                int c = rs.getInt("cnt");

                switch (s) {
                    case "positive": positive = c; break;
                    case "negative": negative = c; break;
                    case "neutral":  neutral = c; break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        json.put("positive", positive);
        json.put("negative", negative);
        json.put("neutral", neutral);

        resp.getWriter().write(json.toString());
    }
}
