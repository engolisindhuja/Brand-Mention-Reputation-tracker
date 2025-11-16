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

@WebServlet("/spikes")
public class SpikeAlertServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();

        try (Connection conn = DatabaseManager.getConnection()) {

            String sql =
                "SELECT posted_on, COUNT(*) AS cnt FROM mentions " +
                "GROUP BY posted_on ORDER BY posted_on ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int prev = -1;
            int today = 0;

            while (rs.next()) {
                int current = rs.getInt("cnt");

                if (prev != -1 && current > prev * 1.5) {
                    result.put("spike", true);
                    result.put("today", current);
                    result.put("avgPrev", prev);
                    resp.getWriter().write(result.toString());
                    return;
                }

                prev = current;
                today = current;
            }

            result.put("spike", false);
            resp.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            result.put("spike", false);
            resp.getWriter().write(result.toString());
        }
    }
}
