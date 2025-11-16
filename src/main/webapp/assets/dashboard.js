const CONTEXT = "/brandtracker-1.0.0";
const BASE = `http://localhost:8080${CONTEXT}`;

let allMentions = [];

let filteredMentions = [];
let currentPage = 1;
const rowsPerPage = 5;

async function apiGet(endpoint) {
    const res = await fetch(`${BASE}/${endpoint}`);
    return await res.json();
}

async function loadSentiments() {
    const data = await apiGet("sentimentStats");
    animateValue("positiveCount", data.positive);
    animateValue("negativeCount", data.negative);
    animateValue("neutralCount", data.neutral);

    new Chart(document.getElementById("sentimentChart"), {
        type: "pie",
        data: {
            labels: ["Positive", "Negative", "Neutral"],
            datasets: [{
                data: [data.positive, data.negative, data.neutral],
                backgroundColor: ["#28a745", "#dc3545", "#6c757d"]
            }]
        }
    });
}

async function loadTrends() {
    const data = await apiGet("trendData");
    new Chart(document.getElementById("trendChart"), {
        type: "line",
        data: {
            labels: data.map(d => d.date),
            datasets: [{
                label: "Mentions",
                data: data.map(d => d.count),
                borderColor: "#157347",
                borderWidth: 2,
                tension: 0.3
            }]
        }
    });
}

async function loadMentions() {
    await applyFilters();  
}

    async function applyFilters() {
        console.log("Filters triggered");


        const sentiment = document.getElementById("sentimentFilter").value;
        const search = document.getElementById("searchBox").value.trim();
        let from = document.getElementById("fromDate").value;
        let to = document.getElementById("toDate").value;

        if (from) {
            let p = from.split("-");
            from = `${p[2]}-${p[1]}-${p[0]}`;
        }

        if (to) {
            let p = to.split("-");
            to = `${p[2]}-${p[1]}-${p[0]}`;
        }


        const query =
            `fetchMentions?sentiment=${sentiment}&q=${search}&from=${from}&to=${to}&page=1&size=999`;

        const data = await apiGet(query);

        filteredMentions = data.mentions;
        currentPage = 1;

        renderPagination();
        displayPage();
    }


function sentimentBadge(s) {
    if (s === "positive") return `<span class="badge badge-positive">Positive</span>`;
    if (s === "negative") return `<span class="badge badge-negative">Negative</span>`;
    return `<span class="badge badge-neutral">Neutral</span>`;
}

function renderMentions(list) {
    const tbody = document.getElementById("mentionsBody");
    tbody.innerHTML = "";

    list.forEach(m => {
        tbody.innerHTML += `
            <tr>
                <td>${m.platform}</td>
                <td>${m.content}</td>
                <td>${sentimentBadge(m.sentiment)}</td>
                <td>${m.postedOn}</td>
            </tr>
        `;
    });
}
async function loadSpikes() {
    const data = await apiGet("spikes"); 

    const box = document.getElementById("spikeList");

    if (!data.spike) {
        box.innerHTML = "<p>No spikes detected.</p>";
        return;
    }

    box.innerHTML = `
        <div class="spike-box">
            ⚠ Spike detected — ${data.today} mentions (Prev avg: ${data.avgPrev})
        </div>
    `;
}


function renderPagination() {
    const pages = Math.ceil(filteredMentions.length / rowsPerPage);
    const container = document.getElementById("pagination");
    container.innerHTML = "";

    for (let p = 1; p <= pages; p++) {
        container.innerHTML += `
            <button onclick="gotoPage(${p})" class="${p === currentPage ? "active" : ""}">${p}</button>
        `;
    }
}

function gotoPage(p) {
    currentPage = p;
    displayPage();
}

function displayPage() {
    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    renderMentions(filteredMentions.slice(start, end));
}

document.getElementById("exportCSV").onclick = () => {
    window.location.href = `${BASE}/exportCsv`;
};

document.getElementById("importCSV").onclick = () => {
    document.getElementById("csvFile").click();
};

document.getElementById("csvFile").onchange = async function(e) {
    const file = e.target.files[0];
    if (!file) return alert("Please select a file!");

    let fd = new FormData();
    fd.append("file", file);

    let res = await fetch(`${BASE}/uploadCsv`, { method: "POST", body: fd });
    let msg = await res.text();
    alert(msg);

    loadMentions();
    loadTrends();
    loadSentiments();
};

function animateValue(id, end, duration = 800) {
    let start = 0;
    const el = document.getElementById(id);
    const step = (end - start) / (duration / 16);

    let interval = setInterval(() => {
        start += step;
        if (start >= end) {
            start = end;
            clearInterval(interval);
        }
        el.innerText = Math.floor(start);
    }, 16);
}

window.onload = () => {
    loadSentiments();
    loadTrends();
    loadMentions();
    loadSpikes();

    document.getElementById("sentimentFilter").onchange = applyFilters;
    document.getElementById("searchBox").onkeyup = applyFilters;
    document.getElementById("fromDate").onchange = applyFilters;
    document.getElementById("toDate").onchange = applyFilters;
};

