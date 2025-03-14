<template>
  <div class="job-list-container">
    <router-link to="/applications">Find and Download CVs through Candidates CV Files</router-link>
    <h2>Job List</h2>
    <router-link to="/admin">Post a New Job</router-link>
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
          <th>Highest Candidates' Scores</th>
          <th>Candidates CV Files</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="job in jobs" :key="job.id">
          <td>{{ job.title }}</td>
          <td>{{ job.location }}</td>
          <td>{{ job.sector }}</td>
          <td>{{ job.workType }}</td>
          <td>{{ job.jobLevel }}</td>
          <td>{{ job.description }}</td>
          <td>{{ job.educationLevel }}</td>
          <td>{{ job.institutionType }}</td>
          <td>{{ job.universityPreference }}</td>
          <td>{{ job.requiredSkills }}</td>
          <td>{{ job.minExperience }} years</td>
          <td>Experience: {{ job.minExperienceWeight }}%, Education: {{ job.educationWeight }}%, Skills: {{ job.skillsWeight }}%</td>
          <td>Highest Score: {{ job.topMatchScore || 'N/A' }} </td>
          <td>CV Files: {{ job.topCvFileNames || 'N/A' }} </td>
        </tr>
        </tbody>
      </table>
    </div>
    <button @click="exportToExcel" class="export-button">Export to Excel</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'JobApplication',
  data() {
    return {
      jobs: []
    };
  },
  methods: {
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
      }
    },
    exportToExcel() {
      const authToken = localStorage.getItem('authToken');
      const url = 'http://localhost:9090/api/jobs/export';
      window.location.href = `${url}?authToken=${authToken}`;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>

<style scoped>
.job-list-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 auto;
  padding: 2rem;
  max-width: 1200px;
  background-color: #f3f2ef;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.table-container {
  width: 100%;
  max-height: 60vh; /* Adjust the max height as needed */
  overflow-y: auto;
  margin-top: 1rem;
}

.job-table {
  width: 100%;
  border-collapse: collapse;
}

.job-table th,
.job-table td {
  border: 1px solid #ccc;
  padding: 0.5rem;
  text-align: left;
}

.job-table th {
  background-color: #f8f9fa;
}

.export-button {
  background-color: #0073b1;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  margin-top: 1rem;
}

.export-button:hover {
  background-color: #005f8d;
}

@media (max-width: 600px) {
  .job-list-container {
    padding: 1rem;
  }

  .export-button {
    width: 100%;
  }
}
</style>