Brand Mention & Reputation Tracker
Overview
The Brand Mention & Reputation Tracker is a full-stack web application designed to monitor brand mentions across platforms, analyze sentiment, visualize trends, and detect spikes in activity. It enables organizations to understand audience perception and respond appropriately.
This project was built as part of a 48-hour rapid development challenge.
Features
1. Mention Monitoring
  View all brand mentions from multiple platforms.
  Search, filter, and paginate results.
2. Sentiment Analysis Dashboard
  Positive, Negative, and Neutral sentiment counts.
  Sentiment distribution pie chart.
  Trends over time using line charts.
3. Advanced Filters
  Filter by:
  Sentiment
  Search keywords
  Date range (From/To)
4. Spike Detection
  Identifies sudden abnormal increases in brand mentions.
  Displays alerts when spikes occur.
5. CSV Import/Export
  Export all mentions as CSV.
  Upload CSV files to import new mentions.
6. Dark Mode Support
Toggle between light and dark mode for a better user experience.

Tech Stack
Frontend
  HTML, CSS, JavaScript
  Chart.js for visualizations
Backend
  Java Servlets (Jakarta EE)
  JSP
  Apache Tomcat 10
Database
  MySQL
  SQL table: mentions
Tools & Build
  Maven
  VS Code
  Git and GitHub

  | Column    | Type        | Description                     |
| --------- | ----------- | ------------------------------- |
| id        | INT (PK)    | Auto increment                  |
| platform  | VARCHAR(80) | Name of platform (Twitter etc.) |
| content   | TEXT        | Mention content                 |
| sentiment | VARCHAR(15) | positive / negative / neutral   |
| posted_on | DATE        | Mention date                    |

Demo Video  
Click the link below to watch the full demo:
[Watch Demo Video](https://github.com/engolisindhuja/Brand-Mention-Reputation-tracker/raw/main/Brand%20Mention%20and%20Reputation%20Tracker%20Overview.mp4)


