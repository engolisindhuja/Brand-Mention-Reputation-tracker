package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import utils.DatabaseManager;

@WebServlet("/exportCsv")
public class ExportCsvServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=mentions_export.csv");

        PrintWriter out = resp.getWriter();

        out.println("ID,Platform,Content,Sentiment,Posted On");

        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "SELECT * FROM mentions ORDER BY posted_on DESC, id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.print(rs.getInt("id"));
                out.print(",");

                out.print(rs.getString("platform"));
                out.print(",");

                String content = rs.getString("content").replace("\"", "\"\"");
                if (content.contains(","))
                    out.print("\"" + content + "\"");
                else
                    out.print(content);

                out.print(",");

                out.print(rs.getString("sentiment"));
                out.print(",");

                out.println(rs.getString("posted_on"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.flush();
        out.close();
    }
}
