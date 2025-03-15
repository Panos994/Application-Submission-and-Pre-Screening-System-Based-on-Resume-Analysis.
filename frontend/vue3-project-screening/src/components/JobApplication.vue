<template>
  <div class="job-application-container">
    <div id="progress-bar-container" v-if="scoreMessage">
      <div class="progress-container">
        <!-- Name/Label before progress bar -->
        <div class="score-label">
          <strong> Score: {{ manualScore }}%</strong>
        </div>
        <svg class="progress-circle" viewBox="0 0 36 36">
          <path class="circle-bg" d="M18 2a16 16 0 1 1 0 32 16 16 0 0 1 0-32" />
          <path
              class="circle-progress"
              :d="progressPath"
              :style="{ strokeDasharray: strokeDashArray }"
          />
        </svg>
        <div class="progress-value"></div>
      </div>
    </div>

    <div class="split-container">
      <!-- Left section -->
      <div class="left-section">
        <center>
          <button class="buttonJobAvailaibility">
            <router-link to="/CandidateJobList">Check Our Available Job Positions</router-link>
          </button>
        </center>
        <button class="buttonJobStatus">
          <router-link to="/yourapplications">Check your applications Status</router-link>
        </button>
        <br>
        <h1>Welcome, {{username}}. Search, Apply and Get your Job Score:</h1>

        <!-- Manual Job Selection and Submission -->
        <label for="jobSelect">Select a Job (Manual):</label>
        <select v-model="selectedJobId">
          <option v-for="job in jobs" :key="job.id" :value="job.id">
            {{ job.title }}
          </option>
        </select>

        <input type="file" @change="handleFileUploadManual" accept=".pdf,.doc,.docx" />
        <button @click="submitManualResume">Submit Resume (Manual)</button>

        <hr>

        <!-- AI-based Resume Submission -->
        <h2>AI-Assisted Job Matching</h2>
        <input type="file" @change="handleFileUploadAI" accept=".pdf,.doc,.docx" />
        <button @click="submitAIResume">Submit Resume for AI Matching</button>
      </div>

      <!-- Right section -->
      <div class="right-section">
        <h3>
          <em>Agent Smith Responses propose you the Top 3 best matching jobs to get high scores (using LLM AI agent):</em>
        </h3>

        <div v-if="aiResponseData" :class="{ 'typing-effect': showTyping }" class="ai-response-container" v-html="aiResponseData"></div>

        <!-- Display AI suggested jobs as clickable links -->
        <div v-if="aiJobs && aiJobs.length">
          <p>Click on the job title to view the full details:</p>
          <ul>
            <li v-for="job in aiJobs" :key="job.id">
              <router-link :to="`/candidate-job-list/${job.id}`">{{ job.title }}</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <v-dialog v-model="showSuccessModal" max-width="400px">
      <v-card>
        <v-card-title class="text-h5">Success</v-card-title>
        <v-card-text>AI job matching complete! Check your results.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="closeModal">OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'JobApplication',
  data() {
    return {
      username: localStorage.getItem("username") || "User",
      resumeFile: null,      // To store uploaded resume
      selectedJobId: null,   // To store selected job
      jobs: [],              // List of jobs
      scoreMessage: '',      // Message displaying score after manual submission
      manualScore: 0,
      aiResponseData: '',    // Raw AI response data
      showTyping: false,     // To control the typing effect

      showSuccessModal: false, // Controls the visibility of the modal //////

      aiJobs: [] // Array to hold AI proposed jobs
    };
  },

  computed: {
    progressPath() {
      return `
      M18 2
      a 16 16 0 1 1 0 32
      a 16 16 0 1 1 0 -32
    `;
    },
    strokeDashArray() {
      return `${this.manualScore * 100 / 100} 100`;
    }
  },

  methods: {
    handleFileUploadManual(event) {
      this.resumeFile = event.target.files[0];
    },

    handleFileUploadAI(event) {
      this.resumeFile = event.target.files[0];
    },

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

        const regex = /(\d+(\.\d+)?)/;
        const match = response.data.match(regex);

        if (match) {
          this.manualScore = parseFloat(match[0]).toFixed(2);
          this.scoreMessage = `Your resume scored: ${this.manualScore} points!`;
        } else {
          this.manualScore = 0;
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

        console.log('API Response:', response);

        if (response.data && Array.isArray(response.data.jobs)) {
          this.aiJobs = response.data.jobs;
        } else {
          console.error('Jobs data is missing or not an array:', response.data);
        }

        const formattedResponse = response.data.replace(/\n/g, '<br>').replace(/\t/g, '&emsp;');
        this.aiResponseData = formattedResponse;
        this.showTyping = true;

        this.showSuccessModal = true;
      } catch (error) {
        console.error('Error matching job:', error);
        if (error.response) {
          alert(`Error: ${error.response.data}`);
        } else {
          alert('An error occurred while matching the job.');
        }
      }
    },

    closeModal() {
      this.showSuccessModal = false;   ////////////
    },

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
  max-width: 1200px;
  background-color: #f3f2ef;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.split-container {
  display: flex;
  width: 100%;
}

.left-section, .right-section {
  flex: 1;
  padding: 1rem;
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
  max-width: 200px;
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
  overflow: hidden;
  animation: typing 3.5s steps(40, end), blink 0.75s step-end infinite;
  max-width: 100%; /* Ensure it doesn't overflow */
  overflow-wrap: break-word; /* Wrap long words */
  white-space: pre-wrap; /* Keeps formatting */
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
  color: white; /* Change the text color to white */
  font-weight: bold;
}

a:hover {
  text-decoration: underline;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.buttonJobAvailaibility {
  width: 80%;
  max-width: 300px;
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

.buttonJobAvailaibility:hover {
  background-color: #005f8d;
}

.buttonJobStatus {
  width: 80%;
  max-width: 300px;
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

.buttonJobStatus:hover {
  background-color: #005f8d;
}

#progress-bar-container {
  position: fixed;
  top: 20px; /* Adjust spacing from the top */
  right: 20px; /* Adjust spacing from the right */
  z-index: 1000; /* Ensure it appears above other content */
}

.progress-container {
  position: fixed; /* Ensures it stays relative to the viewport */
  top: 50%; /* Aligns vertically in the middle */
  right: 5%; /* Positions it towards the right */
  transform: translateY(-50%); /* Ensures perfect vertical centering */
  width: 100px;
  height: 140px;
  z-index: 1000; /* Keeps it above other elements */
}

.progress-circle {
  width: 100%;
  height: 100%;
}

.circle-bg {
  fill: none;
  stroke: #e6e6e6;
  stroke-width: 3.8;
}

.circle-progress {
  fill: none;
  stroke: #0073b1;
  stroke-width: 3.8;
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;
}

.progress-value {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

.ai-response-container {
  font-size: 0.9rem; /* Adjust this value as needed to make the text smaller */
  color: #333; /* Adjust text color as necessary */
  line-height: 1.5; /* Optional: Adjust line height for readability */
  max-height: 400px; /* Set a maximum height */
  overflow-y: auto; /* Enable vertical scrolling if content overflows */
}

/* Responsive Design */
@media (max-width: 600px) {
  .job-application-container {
    padding: 1rem;
  }

  .split-container {
    flex-direction: column;
  }

  h1, h2 {
    font-size: 1.2rem;
  }

  h3 {
    font-size: 1rem;
  }

  button {
    max-width: 100%;
  }

  .buttonJobAvailaibility {
    width: 100%;
  }

  .buttonJobStatus {
    width: 100%;
  }

  .button-container {
    flex-direction: column;
    align-items: center;
    width: 100%;
  }

  #progress-bar-container {
    top: 10px;
    right: 10px;
  }

  .progress-container {
    top: 40%;
    right: 2%;
  }
}
</style>