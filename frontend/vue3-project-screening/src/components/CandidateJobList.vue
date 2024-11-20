<template>
  <div class="job-list-container">
    <h2>Job List</h2>
    <table class="job-table">
      <thead>
      <tr>
        <th>Title</th>
        <th>Location</th>
        <th>Department</th>
        <th>Type of Employment</th>
        <th>Level of Position</th>
        <th>Description</th>
        <th>Education Level</th>
        <th>Required Skills</th>
        <th>Experience</th>
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
        <td>{{ job.requiredSkills }}</td>
        <td>{{ job.minExperience }} years</td>
      </tr>
      </tbody>
    </table>

    <!-- Pagination Controls -->
    <div class="pagination-controls">
      <button
          class="btn btn-info"
          :disabled="currentPage === 1"
          @click="currentPage--">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button
          class="btn btn-info"
          :disabled="currentPage === totalPages"
          @click="currentPage++">Next</button>
    </div>

    <router-link to="/job-application">Go to Job Application Page</router-link>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      jobs: [], // Array to store job data
      currentPage: 1, // Current page
      rowsPerPage: 5, // Jobs per page
      errorMessage: ''
    };
  },
  computed: {
    // Calculate total pages based on number of jobs and rows per page
    totalPages() {
      return Math.ceil(this.jobs.length / this.rowsPerPage);
    },
    // Get the paginated jobs based on current page and rows per page
    paginatedJobs() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.jobs.slice(start, end);
    },
  },
  methods: {
    // Fetch jobs from the backend with authentication token
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:9090/api/jobs/all', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        this.jobs = response.data;
      } catch (error) {
        console.error('Error fetching jobs:', error);
        this.errorMessage = 'There was an error fetching the job data.';
      }
    },
  },
  mounted() {
    this.fetchJobs(); // Call fetchJobs when the component is mounted
  }
};
</script>




<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}

.job-list-container {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

h1, h2 {
  color: #333;
}

.job-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.job-table th, .job-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.job-table th {
  background-color: #f8f9fa;
}

/* Pagination controls */
.pagination-controls {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.pagination-controls button {
  background-color: #6cb2eb; /* Light Blue */
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination-controls button:disabled {
  background-color: #b0c4de; /* Light Gray */
}

.pagination-controls span {
  align-self: center;
}
</style>
