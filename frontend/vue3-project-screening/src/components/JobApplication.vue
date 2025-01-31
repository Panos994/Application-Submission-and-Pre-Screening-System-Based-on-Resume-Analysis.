<!-- Candidate Page for apply for Job (either manually scored or AI-assisted proposal  -->

<template>
  <div>


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
        <div class="progress-value">

        </div>
      </div>
    </div>









    <center><button class="buttonJobAvailaibility"><router-link to="/CandidateJobList">Check Our Available Job Positions</router-link></button></center>

    <button><router-link to="/yourapplications">Check your applications Status</router-link></button>
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

    <!-- Displaying the manual score after submission -->


    <br><br>

    <hr>



    <!-- AI-based Resume Submission -->
    <h2>AI-Assisted Job Matching</h2>
    <input type="file" @change="handleFileUploadAI" accept=".pdf,.doc,.docx" />
    <button @click="submitAIResume">Submit Resume for AI Matching</button>

    <!-- Displaying the AI-based job match -->
    <!-- Updated AI-Assisted Job Matching section -->
    <h3><em>Agent Smith Responses propose you the Top 3 best matching jobs to get high scores (using LLM AI agent):</em></h3>
    <div style="max-width: 100%; overflow-x: auto;" v-if="aiResponseData" :class="{ 'typing-effect': showTyping }" v-html="aiResponseData"></div>


    <br>


    <br><br><br><br><br><br>


    <br><br>


<!-- stripe's trial -->


    <!--div>
      <h1>Subscription Plans</h1>
      <button @click="subscribe('monthly')">Subscribe Monthly (€1.50)</button>
      <button @click="subscribe('6month')">Subscribe for 6 Months (€6.00)</button>
      <button @click="subscribe('yearly')">Subscribe Annually (€10.00)</button>
    </div-->

<!-- end of stripe's trial -->

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
      showTyping: false      // To control the typing effect
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

        // Extract the score and update circular progress
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

    //stripe - trial
/*
    async subscribe(plan) {
      const customerId = localStorage.getItem('customerId');
      if (!customerId) {
        alert('Customer ID is missing. Please log in.');
        return;
      }

      try {
        const response = await axios.post('http://localhost:9090/api/match/subscribe', {
          customerId,
          plan
        });
        alert(response.data);
      } catch (error) {
        console.error('Subscription error:', error);
        alert('Subscription failed. Please try again.');
      }
    },
*/
    // end of stripe's trial


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
  width: 20%;
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
  color: #0073b1;
  font-weight: bold;
}

a:hover {
  text-decoration: underline;
}








.buttonJobAvailaibility{
  width: 30%;
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
  background-color: #ffffff;
}



a {
  color: rgb(0, 0, 0);
  font-weight: bold;
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



</style>