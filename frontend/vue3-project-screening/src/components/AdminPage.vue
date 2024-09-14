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
      <input type="number" v-model="job.minExperience" required />

      <label for="educationLevel">Education Level:</label>
      <input type="text" v-model="job.educationLevel" required />

      <button type="submit">Save Job</button>
    </form>

    <h2>Job List</h2>
    <ul>
      <li v-for="job in jobs" :key="job.id"><strong>Title:</strong> {{ job.title }} - <strong>Brief Description:</strong> {{ job.description }} - <strong>Your Educational Level:</strong> {{job.educationLevel}} - <strong>Required Skills:</strong> {{job.requiredSkills}} - <strong>Experience in Years:</strong> {{job.minExperience}}
        <strong>Highest Score:</strong> {{ job.topMatchScore || 'N/A' }} -
        <strong>CV File:</strong> {{ job.topCvFileNames || 'N/A' }}

        <!-- {{ job.topCvFileName || 'N/A' }}  I created topCvFileNames to display CV filenames that might have the same  High Score-->

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
        minExperience: '',
        educationLevel: ''
      },
      jobs: []
    };
  },
  methods: {
    async submitJob() {
      try {
        const authToken = localStorage.getItem('authToken');
        console.log('Token:', authToken);

        await axios.post('http://localhost:9090/api/jobs/create', {
          title: this.job.title,
          description: this.job.description,
          requiredSkills: this.job.requiredSkills, // Keep this as a string
          minExperience: this.job.minExperience,
          educationLevel: this.job.educationLevel
        }, {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        alert('Job saved!');
        this.job = {
          title: '',
          description: '',
          requiredSkills: '',
          minExperience: '',
          educationLevel: ''
        };
        this.fetchJobs();
      } catch (error) {
        console.error('Error saving job:', error);
        alert('An error occurred while saving the job.');
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

      // Using window.location to trigger the file download
      window.location.href = `${url}?authToken=${authToken}`;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>

