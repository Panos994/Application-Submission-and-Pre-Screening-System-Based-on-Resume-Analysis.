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

    <h3>Your Uploaded CVs</h3>
    <ul>
      <li v-for="cv in userCVs" :key="cv.id">
        {{ cv.fileName }}
        <button @click="selectStoredCV(cv)">Use</button>
        <button @click="deleteCV(cv.id)">Delete</button>
      </li>
    </ul>

    <h3>Upload a New CV</h3>
    <input type="file" @change="handleFileUpload($event)" accept=".pdf,.doc,.docx" />
    <button @click="uploadCV">Upload New CV</button>
    <button @click="applyWithNewCV" :disabled="!selectedStoredCV">Apply with New CV</button>

    <h3>Apply with Selected Stored CV</h3>
    <button @click="applyWithStoredCV" :disabled="!selectedStoredCV">Apply with Stored CV</button>
  </div>
</template>

<script>
import axios from "axios";
import BackButton from "@/components/BackButton.vue";

export default {
  components: { BackButton },
  data() {
    return {
      job: {},
      userCVs: [], // List of stored CVs fetched from the backend
      resumeFile: null, // File selected for upload
      selectedStoredCV: null, // Currently selected stored CV for application
    };
  },
  methods: {



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
    fetchStoredCVs() {
      const authToken = localStorage.getItem("authToken");
      axios
          .get("http://localhost:9090/api/cvs", {
            headers: { Authorization: `Bearer ${authToken}` },
          })
          .then((response) => {
            this.userCVs = response.data.map(cv => ({
              id: cv.id,
              fileName: cv.fileName,
              fileUrl: cv.fileUrl,
            }));
          })
          .catch((error) => {
            console.error("Error fetching stored CVs:", error);
          });
    },
    handleFileUpload(event) {
      this.resumeFile = event.target.files[0];
    },
    uploadCV() {
      if (!this.resumeFile) {
        alert("Please select a CV to upload.");
        return;
      }

      const authToken = localStorage.getItem("authToken");
      const formData = new FormData();
      formData.append("cv", this.resumeFile);

      axios
          .post("http://localhost:9090/api/cvs/upload", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${authToken}`,
            },
          })
          .then(() => {
            alert("CV uploaded successfully!");
            this.fetchStoredCVs(); // Refresh the list of stored CVs
          })
          .catch((error) => {
            console.error("Error uploading CV:", error);
            alert("Error uploading CV. Please try again.");
          });
    },



    deleteCV(cvId) {
      const authToken = localStorage.getItem("authToken");
      axios
          .delete(`http://localhost:9090/api/cvs/${cvId}`, {
            headers: { Authorization: `Bearer ${authToken}` },
          })
          .then(() => {
            alert("CV deleted successfully!");
            this.fetchStoredCVs(); // Refresh the list of stored CVs
          })
          .catch((error) => {
            console.error("Error deleting CV:", error);
            alert("Error deleting CV. Please try again.");
          });
    },
    selectStoredCV(cv) {
      this.selectedStoredCV = cv;
      alert(`Selected CV: ${cv.fileName}`);
    },
    applyWithStoredCV() {
      if (!this.selectedStoredCV) {
        alert("Please select a stored CV to apply.");
        return;
      }

      const authToken = localStorage.getItem("authToken");
      if (!authToken) {
        alert("Authentication token is missing. Please log in again.");
        return;
      }

      axios
          .post(
              "http://localhost:9090/api/jobs/apply-with-stored-cv",
              {
                jobId: this.job.id,
                cvId: this.selectedStoredCV.id,
              },
              {
                headers: { Authorization: `Bearer ${authToken}` },
              }
          )
          .then(() => {
            alert("Application submitted successfully with the stored CV!");
          })
          .catch((error) => {
            console.error("Error applying for job with stored CV:", error);
          });
    },
    applyWithNewCV() {
      if (!this.resumeFile) {
        alert("Please select a CV to upload.");
        return;
      }

      const authToken = localStorage.getItem("authToken");
      const formData = new FormData();
      formData.append("jobId", this.job.id);
      formData.append("resume", this.resumeFile);

      axios
          .post("http://localhost:9090/api/jobs/apply", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${authToken}`,
            },
          })
          .then(() => {
            alert("Application submitted successfully with a new CV!");
          })
          .catch((error) => {
            console.error("Error applying for job:", error);
          });
    },
  },
  mounted() {
    this.fetchStoredCVs();
    this.fetchJobDetail()
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
  margin-right: 5px;
}

button:disabled {
  background-color: #b0c4de; /* Light Gray */
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 10px;
}
</style>