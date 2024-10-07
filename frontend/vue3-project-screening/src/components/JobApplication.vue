<template>
  <div>
    <h1>Test a Job Application</h1>

    <!-- Manual Job Selection and Submission -->
    <label for="jobSelect">Select a Job (Manual):</label>
    <select v-model="selectedJobId">
      <option v-for="job in jobs" :key="job.id" :value="job.id">
        {{ job.title }}
      </option>
    </select>

    <input type="file" @change="handleFileUploadManual" accept=".pdf,.doc,.docx" />
    <button @click="submitManualResume">Submit Resume (Manual)</button>

    <!-- Displaying the manual score after submission -->
    <p v-if="scoreMessage">{{ scoreMessage }}</p>

    <!-- AI-based Resume Submission -->
    <h2>AI-Assisted Job Matching</h2>
    <input type="file" @change="handleFileUploadAI" accept=".pdf,.doc,.docx" />
    <button @click="submitAIResume">Submit Resume for AI Matching</button>

    <!-- Displaying the AI-based job match -->
    <!--p v-if="aiMatchMessage">{{ aiMatchMessage }}</p-->



    <h3>AI Response Data:</h3>
    <pre v-if="aiResponseData">{{ aiResponseData }}</pre>



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
      resumeFile: null,      // To store uploaded resume
      selectedJobId: null,   // To store selected job
      jobs: [],              // List of jobs
      scoreMessage: '',      // Message displaying score after manual submission
      aiMatchMessage: '',     // Message displaying AI-based job match
      aiResponseData: ''
    };
  },
  methods: {
    handleFileUploadManual(event) {
      // Store the selected file for manual submission
      this.resumeFile = event.target.files[0];
    },

    handleFileUploadAI(event) {
      // Store the selected file for AI submission
      this.resumeFile = event.target.files[0];
    },

    // Manual job resume submission
    async submitManualResume() {
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

        // Extract the score and display
        const regex = /(\d+(\.\d+)?)/;
        const match = response.data.match(regex);

        if (match) {
          const score = parseFloat(match[0]).toFixed(2);
          this.scoreMessage = `Your resume scored: ${score} points!`;
        } else {
          this.scoreMessage = response.data;
        }

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

    // AI-based resume submission
    async submitAIResume() {
      if (!this.resumeFile) {
        alert('Please upload your resume.');
        return;
      }

      this.aiMatchMessage = ''; // Reset message on new submission
      this.aiResponseData = ''; // Reset raw response data

      const formData = new FormData();
      formData.append('resume', this.resumeFile);

      const authToken = localStorage.getItem('authToken');

      try {
        const response = await axios.post('http://localhost:9090/api/match/match-candidate', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${authToken}`
          }
        });

        console.log('AI matching response:', response.data); // Log full response for debugging

        // Store the raw response data
        this.aiResponseData = JSON.stringify(response.data, null, 2); // Stringify for pretty printing

        // Assuming the backend sends a 'message' field with the full response
        if (response.data.message) {
          this.aiMatchMessage = response.data.message; // Set the AI message directly
        } else {
          this.aiMatchMessage = 'AI matched you to: No match found';
        }

        alert('AI job matching complete! Check your results.');

      } catch (error) {
        console.error('Error matching job:', error);
        if (error.response) {
          console.error('Response data:', error.response.data);
          alert(`Error: ${error.response.data}`);
        } else {
          alert('An error occurred while matching the job.');
        }
      }
    },

    // Fetch available jobs from backend for manual matching
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
      this.selectedJobId = null;
      this.resumeFile = null;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>
