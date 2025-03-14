<template>
  <div class="job-list-container">
    <h2>Job List</h2>

    <input
        type="text"
        v-model="searchQuery"
        placeholder="Search job titles..."
        class="search-input"
    />

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
        <th>Actions</th>
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
        <td>
          <input type="file" @change="handleFileUpload($event, job.id)" accept=".pdf,.doc,.docx" />
          <button @click="applyForJob(job.id)">Apply</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination-controls">
      <button :disabled="currentPage === 1" @click="currentPage--">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="currentPage++">Next</button>
    </div>

    <router-link to="/job-application">Go to Job Application Page</router-link>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      jobs: [],  // Array to store job data
      currentPage: 1,
      rowsPerPage: 5,
      searchQuery: '',
      jobId: this.$route.params.jobId,  // Capture jobId from URL params
      resumeFiles: {}, // Object to store the uploaded resumes for each job
    };
  },
  computed: {
    filteredJobs() {
      let filtered = this.jobs;

      if (this.jobId) {
        // Filter jobs based on selected jobId
        filtered = this.jobs.filter(job => job.id === Number(this.jobId));
      }

      if (this.searchQuery) {
        // Additional filtering by search query
        filtered = filtered.filter(job => job.title.toLowerCase().includes(this.searchQuery.toLowerCase()));
      }

      return filtered;
    },
    totalPages() {
      return Math.ceil(this.filteredJobs.length / this.rowsPerPage);
    },
    paginatedJobs() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.filteredJobs.slice(start, end);
    },
  },
  methods: {
    handleFileUpload(event, jobId) {
      this.resumeFiles[jobId] = event.target.files[0];
    },
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:9090/api/jobs/all', {
          headers: { 'Authorization': `Bearer ${authToken}` }
        });
        this.jobs = response.data;
      } catch (error) {
        console.error('Error fetching jobs:', error);
      }
    },
    async applyForJob(jobId) {
      const resumeFile = this.resumeFiles[jobId];
      if (!resumeFile) {
        alert('Please upload your resume.');
        return;
      }

      try {
        const authToken = localStorage.getItem('authToken');
        const formData = new FormData();
        formData.append('jobId', jobId);
        formData.append('resume', resumeFile);

        await axios.post('http://localhost:9090/api/jobs/apply', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${authToken}`
          }
        });

        alert('Application submitted successfully!');
      } catch (error) {
        console.error('Error applying for job:', error);
        if (error.response) {
          alert(`Error: ${error.response.data}`);
        } else {
          alert('An error occurred while applying for the job.');
        }
      }
    }
  },
  mounted() {
    this.fetchJobs();
  },
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

.search-input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>