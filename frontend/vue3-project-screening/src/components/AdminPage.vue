<template>
  <div>
    <h1>Manage Job Requirements</h1>
    <form @submit.prevent="submitJob">
      <label for="title">Title:</label>
      <input type="text" v-model="job.title" required />

      <label for="description">Description:</label>
      <textarea v-model="job.description" required></textarea>

      <label for="requiredSkills">Required Skills (comma-separated):</label>
      <input type="text" v-model="job.requiredSkills" required />

      <label for="minExperience">Minimum Experience (years):</label>
      <input type="number" v-model.number="job.minExperience" required min="0" />

      <label for="educationLevel">Education Level:</label>
      <input type="text" v-model="job.educationLevel" required />

      <label for="experienceWeight">Weight for Experience (%):</label>
      <input type="number" v-model.number="criteriaWeights.minExperience" required min="0" max="100" />

      <label for="educationWeight">Weight for Education (%):</label>
      <input type="number" v-model.number="criteriaWeights.education" required min="0" max="100" />

      <label for="skillsWeight">Weight for Skills (%):</label>
      <input type="number" v-model.number="criteriaWeights.skills" required min="0" max="100" />

      <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
      <button type="submit">Save Job</button>
    </form>

    <h2>Job List</h2>
    <ul>
      <li v-for="job in jobs" :key="job.id">
        <strong>Title:</strong> {{ job.title }} -
        <strong>Brief Description:</strong> {{ job.description }} -
        <strong>Education Level:</strong> {{ job.educationLevel }} -
        <strong>Required Skills:</strong> {{ job.requiredSkills }} -
        <strong>Experience (years):</strong> {{ job.minExperience }} -
        <strong>Weights:</strong> Experience: {{ job.minExperienceWeight }}%, Education: {{ job.educationWeight }}%, Skills: {{ job.skillsWeight }}% -
        <strong>Highest Score:</strong> {{ job.topMatchScore || 'N/A' }} -
        <strong>CV Files:</strong> {{ job.topCvFileNames || 'N/A' }}
      </li>
      <br><br>
      <button @click="exportToExcel">Export to Excel</button>
    </ul>
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
        educationLevel: ''
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
    async submitJob() {
      // Calculate the total weight
      const totalWeight = this.criteriaWeights.minExperience + this.criteriaWeights.education + this.criteriaWeights.skills;

      // Validate that the total weight is 100%
      if (totalWeight !== 100) {
        this.errorMessage = 'The total of all weights must equal 100%.';
        return;
      } else {
        this.errorMessage = '';
      }

      try {
        const authToken = localStorage.getItem('authToken');
        console.log('Token:', authToken);

        // Construct the job object with weights
        const jobToSave = {
          title: this.job.title,
          description: this.job.description,
          requiredSkills: this.job.requiredSkills,
          minExperience: this.job.minExperience,
          educationLevel: this.job.educationLevel,
          minExperienceWeight: this.criteriaWeights.minExperience,
          educationWeight: this.criteriaWeights.education,
          skillsWeight: this.criteriaWeights.skills
        };

        await axios.post('http://localhost:9090/api/jobs/create', jobToSave, {
          headers: {
            'Authorization': `Bearer ${authToken}`,
            'Content-Type': 'application/json'
          }
        });

        alert('Job saved successfully!');
        // Reset the form fields
        this.job = {
          title: '',
          description: '',
          requiredSkills: '',
          minExperience: null,
          educationLevel: ''
        };
        this.criteriaWeights = {
          minExperience: 0,
          education: 0,
          skills: 0
        };
        this.fetchJobs();
      } catch (error) {
        console.error('Error saving job:', error);
        if (error.response) {
          this.errorMessage = error.response.data;
        } else {
          this.errorMessage = 'An error occurred while saving the job.';
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

    exportToExcel() {
      const authToken = localStorage.getItem('authToken');
      const url = 'http://localhost:9090/api/jobs/export';

      // Trigger the file download
      window.location.href = `${url}?authToken=${authToken}`;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>
