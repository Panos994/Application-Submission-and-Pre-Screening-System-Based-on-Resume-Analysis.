<!-- Candidate Page for apply for Job (either manually scored or AI-assisted proposal -->

<template>
  <div>
    <br>  <br>
    <center><router-link to="/CandidateJobList">Check Our Available Job Positions</router-link></center>
    <h1>Apply for a Job</h1>

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

    <br><br><br><br><br><br>

    <hr>


    <br><br><br>
    <!-- AI-based Resume Submission -->
    <h2>AI-Assisted Job Matching</h2>
    <input type="file" @change="handleFileUploadAI" accept=".pdf,.doc,.docx" />
    <button @click="submitAIResume">Submit Resume for AI Matching</button>

    <!-- Displaying the AI-based job match -->
    <!-- Updated AI-Assisted Job Matching section -->
    <h3>Agent Smith Response:</h3>
    <div style="max-width: 100%; overflow-x: auto;" v-if="aiResponseData" :class="{ 'typing-effect': showTyping }" v-html="aiResponseData"></div>


    <br>


    <br><br><br><br><br><br>
    <router-link to="/">Return Back</router-link>



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
      aiResponseData: '',    // Raw AI response data
      showTyping: false      // To control the typing effect
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

      this.aiResponseData = ''; // Reset raw response data
      this.showTyping = false;  // Reset typing effect

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
        // Convert newline characters to <br> for HTML rendering
        //const formattedResponse = JSON.stringify(response.data, null, 2).replace(/\n/g, '<br>');
        const formattedResponse = response.data.replace(/\n/g, '<br>').replace(/\t/g, '&emsp;');

        // Set the formatted response and trigger typing effect
        this.aiResponseData = formattedResponse;
        this.showTyping = true;  // Start typing effect

        alert('AI job matching complete! Check your results.');

      } catch (error) {
        console.error('Error matching job:', error);
        if (error.response) {
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

<style scoped>
/* General Layout */
.job-application-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 auto;
  padding: 2rem;
  max-width: 900px;
  background-color: #f3f2ef;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

h1, h2 {
  color: #1f4b7b;
  margin-bottom: 1rem;
  font-size: 1.5rem;
  font-weight: bold;
}

h3 {
  color: #0073b1;
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

/* Form Elements */
label {
  font-weight: bold;
  color: #333;
  margin-top: 1rem;
  text-align: left;
  width: 100%;
  font-size: 0.9rem;
}

select, input[type="file"] {
  width: 100%;
  padding: 0.75rem;
  margin-top: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  color: #333;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #0073b1;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  margin-top: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #005f8d;
}

/* Score and Messages */
p {
  font-size: 1rem;
  color: #333;
  margin-top: 1rem;
  background-color: #e9f7fe;
  padding: 1rem;
  border: 1px solid #b2e0f6;
  border-radius: 5px;
}

pre {
  background-color: #eef3f8;
  padding: 1rem;
  font-size: 0.9rem;
  color: #333;
  border-radius: 5px;
}

/* Typing Effect */
.typing-effect {
  border-right: 2px solid rgba(0, 0, 0, 0.75);
  white-space: nowrap;
  overflow: hidden;
  animation: typing 3.5s steps(40, end), blink 0.75s step-end infinite;
}

@keyframes typing {
  from { width: 0; }
  to { width: 100%; }
}

@keyframes blink {
  50% { border-color: transparent; }
}

/* Links */
a {
  color: #0073b1;
  font-weight: bold;
}

a:hover {
  text-decoration: underline;
}



</style>