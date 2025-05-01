<template>
  <div class="job-list-container">
    <!-- Navigation Links -->
    <center><router-link to="/admin" class="back-link">Return Back to Job Post Page</router-link></center>
    <router-link to="/applications" class="back-link">Find and Download CVs through Candidates CV Files</router-link>

    <h2>Job List</h2>
    <button @click="exportToExcel" class="export-button">Export to Excel</button>

    <div class="table-container">
      <table class="job-table">
        <thead>
        <tr>
          <th>Title</th>
          <th>Location</th>
          <th>Department</th>
          <th>Type of Employment</th>
          <th>Job Level</th>
          <th>Description</th>
          <th>Education Level</th>
          <th>Institution Type</th>
          <th>Preferred Universities</th>
          <th>Required Skills</th>
          <th>Experience</th>
          <th>Score Weights</th>
          <th>Highest Scores</th>
          <th>Candidates CV Files</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="job in paginatedJobs" :key="job.id">
          <td>{{ job.title }}</td>
          <td>{{ job.location }}</td>
          <td>{{ job.sector }}</td>
          <td>{{ job.workType }}</td>
          <td>{{ job.jobLevel }}</td>
          <td>{{ job.description }}</td>
          <td>{{ job.educationLevel }}</td>
          <td>{{ job.institutionType || 'N/A' }}</td>
          <td>{{ job.universityPreference || 'N/A' }}</td>
          <td>{{ job.requiredSkills }}</td>
          <td>{{ job.minExperience }} years</td>
          <td>
            Experience: {{ job.minExperienceWeight }}%,
            Education: {{ job.educationWeight }}%,
            Skills: {{ job.skillsWeight }}%
          </td>
          <td>{{ job.topMatchScore || 'N/A' }}</td>
          <td>{{ job.topCvFileNames || 'N/A' }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination Controls -->
    <div class="pagination-controls">
      <button :disabled="currentPage === 1" @click="currentPage--">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="currentPage++">Next</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ModeratorJobList",
  data() {
    return {
      jobs: [], // All jobs
      currentPage: 1, // Current page
      rowsPerPage: 2, // Jobs per page
    };
  },
  computed: {
    // Paginated jobs
    paginatedJobs() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.jobs.slice(start, end);
    },
    // Total pages for pagination
    totalPages() {
      return Math.ceil(this.jobs.length / this.rowsPerPage);
    },
  },
  methods: {
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem("authToken");
        const response = await axios.get("http://localhost:9090/api/jobs/all", {
          headers: {
            Authorization: `Bearer ${authToken}`,
          },
        });
        this.jobs = response.data;
      } catch (error) {
        console.error("Error fetching jobs:", error);
      }
    },
    exportToExcel() {
      const authToken = localStorage.getItem("authToken");
      const url = "http://localhost:9090/api/jobs/export";
      window.location.href = `${url}?authToken=${authToken}`;
    },
  },
  mounted() {
    this.fetchJobs();
  },
};
</script>

<style scoped>
/* Align styles with ViewApplicationsCV */
.job-list-container {
  padding: 10px 20px; /* Reduced padding to bring table higher up */
  position: relative; /* For sticky pagination */
}

.back-link {
  display: block;
  margin-top: 10px; /* Reduced margin */
  color: #3490dc;
  text-decoration: none;
}

.back-link:hover {
  text-decoration: underline;
}

.export-button {
  background-color: #6cb2eb;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px; /* Reduced padding */
  cursor: pointer;
  margin-bottom: 10px; /* Reduced margin */
}

.table-container {
  margin-top: 10px; /* Reduced margin */
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow-x: auto; /* Enable horizontal scrolling for wide tables */
  max-height: calc(85vh - 50px); /* Prevent table from exceeding viewport height */
  overflow-y: auto; /* Enable vertical scrolling within the table */
}

.job-table {
  width: 100%; /* Full width */
  border-collapse: collapse;
  font-size: 12px; /* Smaller font size for responsiveness */
}

.job-table th,
.job-table td {
  border: 1px solid #ccc;
  padding: 6px; /* Reduced padding for compactness */
  text-align: left;
}

.job-table th {
  background-color: #f8f9fa;
  font-weight: bold;
  font-size: 12px; /* Smaller header font size */
}

.job-table tr:nth-child(even) {
  background-color: #f9f9f9; /* Add alternating row colors */
}

.job-table tr:hover {
  background-color: #e9ecef; /* Highlight row on hover */
}

.pagination-controls {
  position: sticky; /* Ensure pagination stays visible */
  bottom: 0; /* Stick to the bottom of the container */
  left: 0;
  background-color: white; /* Keep it readable */
  display: flex;
  justify-content: space-between;
  padding: 10px;
  border-top: 1px solid #ccc;
  z-index: 2; /* Ensure it stays above the table */
}

.pagination-controls button {
  padding: 8px; /* Adjusted for smaller size */
  font-size: 12px; /* Smaller font size */
  background-color: #3490dc;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.pagination-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination-controls span {
  align-self: center;
  font-size: 12px; /* Smaller font size */
}
</style>