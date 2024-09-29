<template>
  <div>
    <h1>Test a Job Application</h1>

    <label for="jobSelect">Select a Job:</label>
    <select v-model="selectedJobId">
      <option v-for="job in jobs" :key="job.id" :value="job.id">
        {{ job.title }}
      </option>
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
      jobs: [],
      scoreMessage: ''
    };
  },
  methods: {
    handleFileUpload(event) {
      this.resumeFile = event.target.files[0];
    },
    async submitResume() {
      if (!this.resumeFile || !this.selectedJobId) {
        alert('Please select a job and upload your resume.');
        return;
      }

      const formData = new FormData();
      formData.append('resume', this.resumeFile);
      formData.append('jobId', this.selectedJobId);

      const authToken = localStorage.getItem('authToken');

      try {
        const response = await axios.post('http://localhost:9090/api/jobs/apply', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${authToken}`
          }
        });

        // Extract the number from the response string
        const regex = /(\d+(\.\d+)?)/; // Matches a number with optional decimals
        const match = response.data.match(regex);

        if (match) {
          const score = parseFloat(match[0]).toFixed(2); // Round to 2 decimal places
          this.scoreMessage = `Your resume scored: ${score} points!`;
        } else {
          this.scoreMessage = response.data; // Fallback to original message if no number is found
        }

        // Call resetForm to clear the form
        this.resetForm();
      } catch (error) {
        console.error('Error applying for job:', error);
        if (error.response) {
          alert(`Error: ${error.response.data}`);
        } else {
          alert('An error occurred while applying for the job.');
        }
      }
    },
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:9090/api/jobs/topApplication', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        this.jobs = response.data;
      } catch (error) {
        console.error('Error fetching jobs:', error);
      }
    },
    resetForm() {
      // Reset form data
      this.selectedJobId = null;
      this.resumeFile = null;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>