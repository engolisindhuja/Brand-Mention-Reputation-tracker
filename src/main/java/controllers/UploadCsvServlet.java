package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.DatabaseManager;
import utils.SentimentEngine;

@WebServlet("/uploadCsv")
@MultipartConfig
public class UploadCsvServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");

        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.getWriter().write("No file uploaded.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream()))) {

            Connection conn = DatabaseManager.getConnection();
            String line;
            boolean skip = true;
            int count = 0;

            while ((line = reader.readLine()) != null) {

                if (skip) { skip = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] c = line.split(",");

                if (c.length < 4) continue;

                String platform = c[0].trim();
                String content = c[1].trim().replace("\"", "");
                String sentiment = c[2].trim();
                String date = c[3].trim();

                if (!(sentiment.equals("positive") || sentiment.equals("negative") || sentiment.equals("neutral"))) {
                    sentiment = SentimentEngine.analyze(content);
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO mentions(platform, content, sentiment, posted_on) VALUES (?, ?, ?, ?)"
                );

                ps.setString(1, platform);
                ps.setString(2, content);
                ps.setString(3, sentiment);
                ps.setString(4, date);

                ps.executeUpdate();
                count++;
            }

            resp.getWriter().write("Upload successful: " + count + " records added!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error!");
        }
    }
}
