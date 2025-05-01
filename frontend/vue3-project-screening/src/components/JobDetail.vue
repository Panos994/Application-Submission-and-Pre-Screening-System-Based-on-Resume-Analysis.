<template>
  <div class="job-detail-container">
    <BackButton />

    <!-- Job Details Section -->

    <div class="job-details">
      <h2>{{ job.title }}</h2><br>
      <p><strong>Location:</strong> {{ job.location }}</p>
      <p><strong>Department:</strong> {{ job.sector }}</p>
      <p><strong>Type of Employment:</strong> {{ job.workType }}</p>
      <p><strong>Level of Position:</strong> {{ job.jobLevel }}</p><br><br><hr>
      <p><strong>Description:</strong> {{ job.description }}</p>
      <p><strong>Education Level:</strong> {{ job.educationLevel }}</p>
      <p><strong>Required Skills:</strong> {{ job.requiredSkills }}</p>
      <p><strong>Experience:</strong> {{ job.minExperience }} years</p>
<br><hr>
      <h3>Upload a New CV</h3>
      <input type="file" @change="handleFileUpload($event)" accept=".pdf,.doc,.docx" />
      <button @click="uploadCV">Upload New CV</button>
      <button @click="applyWithNewCV" :disabled="!selectedStoredCV">Apply with New CV</button>

      <h3>Apply with Selected Stored CV</h3>
      <button @click="applyWithStoredCV" :disabled="!selectedStoredCV">Apply with Stored CV</button>
    </div>

    <!-- Uploaded CVs Section -->
    <div class="uploaded-cvs">
      <h3>Your Uploaded CVs</h3>
      <table class="uploaded-cvs-table">
        <thead>
        <tr>
          <th>CV Name</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="cv in paginatedCVs" :key="cv.id">
          <td>{{ cv.fileName }}</td>
          <td>
            <button @click="selectStoredCV(cv)">Use</button>
            <button @click="deleteCV(cv.id)">Delete</button>
            <button @click="downloadCV(cv.id)">Preview</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination-controls">
        <button :disabled="currentPage === 1" @click="currentPage--">Previous</button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages" @click="currentPage++">Next</button>
      </div>
    </div>
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
      currentPage: 1, // Current page for paginated CVs
      rowsPerPage: 3, // Number of CVs per page
    };
  },
  computed: {
    paginatedCVs() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.userCVs.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.userCVs.length / this.rowsPerPage);
    },
  },
  methods: {
    async fetchJobDetail() {
      const id = this.$route.params.id;
      try {
        const authToken = localStorage.getItem("authToken");
        const response = await axios.get(`http://localhost:9090/api/jobs/${id}`, {
          headers: { Authorization: `Bearer ${authToken}` },
        });
        this.job = response.data;
      } catch (error) {
        console.error("Error fetching job detail:", error);
      }
    },
    fetchStoredCVs() {
      const authToken = localStorage.getItem("authToken");
      axios
          .get("http://localhost:9090/api/cvs", {
            headers: { Authorization: `Bearer ${authToken}` },
          })
          .then((response) => {
            this.userCVs = response.data.map((cv) => ({
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
    getDownloadLink(id) {
      return `http://localhost:9090/api/applications/download/${id}`;
    },
    downloadCV(id) {
      const token = localStorage.getItem("authToken");
      fetch(this.getDownloadLink(id), {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/pdf",
        },
      })
          .then((response) => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.blob();
          })
          .then((blob) => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.style.display = "none";
            a.href = url;
            a.download = `CV_${id}.pdf`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
          })
          .catch((error) => console.error("Error downloading the CV:", error));
    },
    applyWithStoredCV() {
      if (!this.selectedStoredCV) {
        alert("Please select a stored CV to apply.");
        return;
      }

      const authToken = localStorage.getItem("authToken");
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
    this.fetchJobDetail();
  },
};
</script>

<style scoped>
.job-detail-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px;
  position: relative;
}

.back-button {
  font-size: 12px;
  padding: 5px 10px;
  margin-bottom: 10px;
}

.job-details {
  width: 60%;

}

.uploaded-cvs {
  width: 40%;
  position: absolute;
  right: 20px;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
  padding: 10px;
}

.uploaded-cvs-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

.uploaded-cvs-table th,
.uploaded-cvs-table td {
  border: 1px solid #ddd;
  padding: 5px;
  text-align: left;
}

.pagination-controls {
  text-align: center;
  margin-top: 10px;
}

button {
  background-color: #6cb2eb;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #b0c4de;
}
</style>