<template>
  <div class="job-detail-container">
    <BackButton />

    <h2>{{ job.title }}</h2>
    <p><strong>Location:</strong> {{ job.location }}</p>
    <p><strong>Department:</strong> {{ job.sector }}</p>
    <p><strong>Type of Employment:</strong> {{ job.workType }}</p>
    <p><strong>Level of Position:</strong> {{ job.jobLevel }}</p>
    <p><strong>Description:</strong> {{ job.description }}</p>
    <p><strong>Education Level:</strong> {{ job.educationLevel }}</p>
    <p><strong>Required Skills:</strong> {{ job.requiredSkills }}</p>
    <p><strong>Experience:</strong> {{ job.minExperience }} years</p>
    <input type="file" @change="handleFileUpload($event)" accept=".pdf,.doc,.docx" />
    <button @click="applyForJob">Apply</button>
  </div>
</template>

<script>
import axios from 'axios';
import BackButton from "@/components/BackButton.vue";

export default {
  components: {BackButton},
  data() {
    return {
      job: {},
      resumeFile: null,
    };
  },
  methods: {
    handleFileUpload(event) {
      this.resumeFile = event.target.files[0];
    },
    async fetchJobDetail() {
      const id = this.$route.params.id;
      try {
        const authToken = localStorage.getItem('authToken');
        const response = await axios.get(`http://localhost:9090/api/jobs/${id}`, {
          headers: { 'Authorization': `Bearer ${authToken}` }
        });
        this.job = response.data;
      } catch (error) {
        console.error('Error fetching job detail:', error);
      }
    },
    async applyForJob() {
      if (!this.resumeFile) {
        alert('Please upload your resume.');
        return;
      }

      try {
        const authToken = localStorage.getItem('authToken');
        const formData = new FormData();
        formData.append('jobId', this.job.id);

        formData.append('resume', this.resumeFile);

        await axios.post('http://localhost:9090/api/jobs/apply', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${authToken}`
          }
        });

        alert('Application submitted successfully!');
      } catch (error) {
        console.error('Error applying for job:', error);
        if (error.response) {
          alert(`Error: ${error.response.data}`);
        } else {
          alert('An error occurred while applying for the job.');
        }
      }
    }
  },
  mounted() {
    this.fetchJobDetail();
  },
};
</script>

<style scoped>
.job-detail-container {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

h2 {
  color: #333;
}

p {
  margin-bottom: 10px;
}

button {
  background-color: #6cb2eb; /* Light Blue */
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #b0c4de; /* Light Gray */
}
</style>