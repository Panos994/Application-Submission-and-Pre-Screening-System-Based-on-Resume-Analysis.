<template>
  <div>
    <h1>Test a Job Application</h1>

    <label for="jobSelect">Select a Job:</label>
    <select v-model="selectedJobId">
      <option v-for="job in jobs" :key="job.id" :value="job.id">{{ job.title }}</option>
    </select>

    <input type="file" @change="handleFileUpload" />
    <button @click="submitResume">Submit Resume</button>
    <p v-if="scoreMessage">{{ scoreMessage }}</p>
    <br><br>
    <router-link to="/admin">Go to Admin Page</router-link>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'JobApplication',
  data() {
    return {
      resumeFile: null,
      selectedJobId: null,
      jobs: [], // Ensure you fetch jobs here as well
      scoreMessage: ''
    };
  },
  methods: {
    handleFileUpload(event) {
      this.resumeFile = event.target.files[0];
    },
    async submitResume() {
      const formData = new FormData();
      formData.append('resume', this.resumeFile);
      formData.append('jobId', this.selectedJobId);

      // Retrieve the token from localStorage
      const authToken = localStorage.getItem('authToken');
      console.log('Token:', authToken);

      axios.post('http://localhost:9090/api/jobs/apply', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${authToken}` // Add Authorization header
        }
      })
          .then(response => {
            this.scoreMessage = response.data;
          })
          .catch(error => {
            console.error('Error applying for job:', error);
            alert('An error occurred while applying for the job.');
          });
    },
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:9090/api/jobs', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        this.jobs = response.data;
      } catch (error) {
        console.error('Error fetching jobs:', error);
      }
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>
