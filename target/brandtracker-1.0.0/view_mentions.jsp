<%@ page import="java.util.*, models.Mention" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Mentions</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background: #f5f5f5;
        }
        h2 {
            text-align: center;
        }
        table {
            width: 100%;
            background: white;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }
        th {
            background: #009879;
            color: white;
        }
        .filter-box {
            background: white;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 6px;
        }
        .pagination a {
            padding: 8px 12px;
            background: #009879;
            color: white;
            border-radius: 4px;
            margin-right: 5px;
            text-decoration: none;
        }
        .pagination a:hover {
            background: #007a63;
        }
    </style>
</head>
<body>

<h2>All Brand Mentions</h2>

<div class="filter-box">
    <form method="get" action="fetchMentions">

        <label>Platform: </label>
        <select name="platform">
            <option value="All">All</option>
            <option>Twitter</option>
            <option>Instagram</option>
            <option>Facebook</option>
            <option>YouTube</option>
            <option>Blog</option>
            <option>News</option>
        </select>

        &nbsp;&nbsp;

        <label>Sentiment: </label>
        <select name="sentiment">
            <option value="All">All</option>
            <option value="positive">Positive</option>
            <option value="negative">Negative</option>
            <option value="neutral">Neutral</option>
        </select>

        &nbsp;&nbsp;

        <label>Date From: </label>
        <input type="date" name="from" />

        <label>To:</label>
        <input type="date" name="to" />

        &nbsp;&nbsp;

        <input type="text" name="q" placeholder="Search content..." />

        &nbsp;&nbsp;

        <button type="submit">Filter</button>
    </form>
</div>

<form method="get" action="exportCsv">
    <button type="submit">Export CSV</button>
</form>

<br>

<form method="post" action="uploadCsv" enctype="multipart/form-data">
    <input type="file" name="file" accept=".csv" required />
    <button type="submit">Upload CSV</button>
</form>

<table>
    <tr>
        <th>ID</th>
        <th>Platform</th>
        <th>Content</th>
        <th>Sentiment</th>
        <th>Posted On</th>
    </tr>

    <%
        ArrayList<Mention> list = (ArrayList<Mention>) request.getAttribute("mentionsList");
        if (list != null) {
            for (Mention m : list) {
    %>
        <tr>
            <td><%= m.getId() %></td>
            <td><%= m.getPlatform() %></td>
            <td><%= m.getContent() %></td>
            <td><%= m.getSentiment() %></td>
            <td><%= m.getPostedOn() %></td>
        </tr>
    <%
            }
        }
    %>
</table>

<%
    Integer total = (Integer) request.getAttribute("total");
    Integer page = (Integer) request.getAttribute("page");
    Integer size = (Integer) request.getAttribute("size");

    if (total == null) total = 0;
    if (page == null) page = 1;
    if (size == null) size = 20;

    int pages = (int) Math.ceil(total / (double) size);
%>

<br>

<div class="pagination">
    <% 
        for (int p = 1; p <= pages; p++) {
    %>
        <a href="fetchMentions?page=<%= p %>&size=<%= size %>"><%= p %></a>
    <% 
        }
    %>
</div>

</body>
</html>
