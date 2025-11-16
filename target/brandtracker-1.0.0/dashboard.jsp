<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>BrandTracker Dashboard</title>

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body { font-family: Arial; background: #f4f5f7; margin: 0; padding: 0; transition: 0.3s; }
        header { background: #198754; padding: 15px 20px; color: white; font-size: 22px;
                 display: flex; justify-content: space-between; }
        .dark-mode { background: #121212; color: #f8f9fa; }
        .card-row { display: flex; justify-content: center; gap: 20px; margin: 25px auto; }
        .card { background: white; padding: 20px; width: 250px; border-radius: 12px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center; }
        .dark-mode .card { background: #1e1e1e; color: white; }
        .count { font-size: 28px; margin-top: 10px; }
        .chart-container { width: 70%; margin: 30px auto; background: white; padding: 20px;
                           border-radius: 12px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .dark-mode .chart-container { background: #1e1e1e; }
        .spike-box { background: #ffe8a1; padding: 12px; width: 70%; margin: 20px auto;
                     border-radius: 6px; border-left: 5px solid #ffc107; color: #856404; }
        table { width: 85%; margin: 30px auto; border-collapse: collapse; background: white; 
                border-radius: 10px; overflow: hidden; }
        th, td { padding: 12px; border-bottom: 1px solid #dee2e6; text-align: center; }
        th { background: #198754; color: white; }
        .badge { padding: 6px 10px; border-radius: 20px; color: white; font-size: 12px; }
        .badge-positive { background: #28a745; }
        .badge-negative { background: #dc3545; }
        .badge-neutral { background: #6c757d; }
        .filter-box { width: 85%; margin: 10px auto; display: flex; gap: 10px; justify-content: center; }
        button { background: #198754; color: white; padding: 8px 12px; border-radius: 6px;
                 border: none; cursor: pointer; }
        #pagination button { margin: 5px; padding: 6px 10px; border-radius: 5px; }
        #pagination .active { background: #0b5137; }
    </style>
</head>

<body>

<header>
    <span>BrandTracker Dashboard</span>
    <button id="darkToggle">Dark Mode</button>
</header>

<div class="card-row">
    <div class="card"><h3>Positive</h3><div id="positiveCount" class="count">0</div></div>
    <div class="card"><h3>Negative</h3><div id="negativeCount" class="count">0</div></div>
    <div class="card"><h3>Neutral</h3><div id="neutralCount" class="count">0</div></div>
</div>

<div class="chart-container">
    <h3 style="text-align:center;">Sentiment Distribution</h3>
    <canvas id="sentimentChart"></canvas>
</div>

<div class="chart-container">
    <h3 style="text-align:center;">Daily Mention Trend</h3>
    <canvas id="trendChart"></canvas>
</div>

<div id="spikeList"></div>

<div class="filter-box">
    <select id="sentimentFilter">
        <option value="all">All</option>
        <option value="positive">Positive</option>
        <option value="negative">Negative</option>
        <option value="neutral">Neutral</option>
    </select>
    <input type="text" id="searchBox" placeholder="Search mentions...">
    <input type="date" id="fromDate">
    <input type="date" id="toDate">
</div>

<div class="filter-box">
    <button id="exportCSV">Export CSV</button>

    <button id="importCSV">Upload CSV</button>

    <form id="uploadForm" action="uploadCsv" method="post" enctype="multipart/form-data">
        <input type="file" id="csvFile" name="file" style="display:none;">
    </form>
</div>

<table>
    <thead>
        <tr>
            <th>Platform</th>
            <th>Content</th>
            <th>Sentiment</th>
            <th>Date</th>
        </tr>
    </thead>
    <tbody id="mentionsBody"></tbody>
</table>

<div id="pagination" style="text-align:center;"></div>

<script src="assets/dashboard.js"></script>

</body>
</html>
