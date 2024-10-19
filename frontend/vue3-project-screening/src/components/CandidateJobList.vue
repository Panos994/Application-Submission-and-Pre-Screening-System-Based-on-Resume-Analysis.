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
      <tr v-for="job in jobs" :key="job.id">
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
        location: '',
        sector: '',
        workType: '',
        jobLevel: '',



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
        const response = await axios.get('http://localhost:9091/api/jobs/all', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        this.jobs = response.data;
      } catch (error) {
        console.error('Error fetching jobs:', error);
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