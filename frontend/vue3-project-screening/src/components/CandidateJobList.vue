<template>
  <div class="job-list-container">
    <h2>Job List</h2>
    <table class="job-table">
      <thead>
      <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Education Level</th>
        <th>Required Skills</th>
        <th>Experience</th>

      </tr>
      </thead>
      <tbody>
      <tr v-for="job in jobs" :key="job.id">
        <td>{{ job.title }}</td>
        <td>{{ job.description }}</td>
        <td>{{ job.educationLevel }}</td>
        <td>{{ job.requiredSkills }}</td>
        <td>{{ job.minExperience }} years</td>




      </tr>
      </tbody>
    </table>
    <router-link to="/job-application">Go to Job Application Page</router-link>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      job: {
        title: '',
        description: '',
        requiredSkills: '',
        minExperience: null,
        institutionTypes: [],
        educationLevels: [], // Array to store multiple selections
        universityPreferences: []
      },
      criteriaWeights: {
        minExperience: 0,
        education: 0,
        skills: 0
      },
      jobs: [],
      errorMessage: ''
    };
  },
  methods: {



    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get('http://localhost:9090/api/jobs/topApplication', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });

        // Process each job in the response
        this.jobs = response.data.map(job => {
          // Handle cases where topMatchScore might be null or undefined
          const score = job.topMatchScore != null ? job.topMatchScore.toString() : null;

          // Extract and format the score
          const regex = /(\d+(\.\d+)?)/; // Matches a number with optional decimals
          const match = score ? score.match(regex) : null; // Only match if score is not null

          const formattedScore = match ? parseFloat(match[0]).toFixed(2) : 'N/A'; // Round to 2 decimal places

          return {
            ...job,
            topCvFileNames: Array.from(new Set(job.topCvFileNames.split(', '))).join(', '),
            topMatchScore: formattedScore // Set the formatted score
          };
        });

      } catch (error) {
        console.error('Error fetching jobs:', error);
        this.errorMessage = 'An error occurred while fetching jobs.'; // Set a general error message
      }


    },


  },
  mounted() {
    this.fetchJobs();
  }
};
</script>




<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}

.form-container,
.job-list-container {
  flex: 1;
  margin: 10px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

h1, h2 {
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.input-field,
.textarea-field,
.select-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.textarea-field {
  height: 100px;
}

.checkbox-group {
  display: flex;
  flex-direction: column;
}

.error-message {
  color: red;
  margin-top: 10px;
}

.submit-button, .export-button {
  background-color: #28a745;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-button:hover, .export-button:hover {
  background-color: #218838;
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
</style>