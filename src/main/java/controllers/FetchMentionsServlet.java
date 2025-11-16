package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DatabaseManager;

@WebServlet("/fetchMentions")
public class FetchMentionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String sentiment = req.getParameter("sentiment");
        String search = req.getParameter("q");
        String from = req.getParameter("from");
        String to = req.getParameter("to");

        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        int size = req.getParameter("size") == null ? 20 : Integer.parseInt(req.getParameter("size"));
        int offset = (page - 1) * size;

        JSONArray arr = new JSONArray();
        int total = 0;

        try {

            Connection conn = DatabaseManager.getConnection();

        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter sqlFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (from != null && !from.isEmpty()) {
            from = from.replace(" ", "");   
            from = LocalDate.parse(from, inputFmt).format(sqlFmt);
        }

        if (to != null && !to.isEmpty()) {
            to = to.replace(" ", "");       
            to = LocalDate.parse(to, inputFmt).format(sqlFmt);
        }


            if (from != null && !from.isEmpty()) {
                from = LocalDate.parse(from, inputFmt).format(sqlFmt);
            }

            if (to != null && !to.isEmpty()) {
                to = LocalDate.parse(to, inputFmt).format(sqlFmt);
            }

            String baseQuery = " FROM mentions WHERE 1=1 ";
            List<Object> params = new ArrayList<>();

            if (sentiment != null && !sentiment.equalsIgnoreCase("all")) {
                baseQuery += " AND sentiment = ? ";
                params.add(sentiment);
            }

            if (search != null && !search.trim().isEmpty()) {
                baseQuery += " AND content LIKE ? ";
                params.add("%" + search.trim() + "%");
            }

            if (from != null && !from.isEmpty()) {
                baseQuery += " AND posted_on >= ? ";
                params.add(from);
            }

            if (to != null && !to.isEmpty()) {
                baseQuery += " AND posted_on <= ? ";
                params.add(to);
            }

            String countSql = "SELECT COUNT(*) " + baseQuery;
            PreparedStatement psCount = conn.prepareStatement(countSql);

            for (int i = 0; i < params.size(); i++) {
                psCount.setObject(i + 1, params.get(i));
            }

            ResultSet rsCount = psCount.executeQuery();
            if (rsCount.next()) total = rsCount.getInt(1);

            String sql = "SELECT * " + baseQuery + " ORDER BY posted_on DESC LIMIT ? OFFSET ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            int idx = 1;
            for (Object p : params) {
                ps.setObject(idx++, p);
            }

            ps.setInt(idx++, size);
            ps.setInt(idx, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("platform", rs.getString("platform"));
                obj.put("content", rs.getString("content"));
                obj.put("sentiment", rs.getString("sentiment"));
                obj.put("postedOn", rs.getString("posted_on"));

                arr.put(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        json.put("total", total);
        json.put("page", page);
        json.put("size", size);
        json.put("mentions", arr);

        resp.getWriter().write(json.toString());
    }
}
