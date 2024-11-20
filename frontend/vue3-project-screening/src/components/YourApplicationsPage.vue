<template>
  <div>
    <h2>Your Applications</h2>
    <div class="applications-table-container">
      <table class="applications-table">
        <thead>
        <tr>
          <th>Status</th>
          <th>CV</th>
          <th>Job Title</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="application in paginatedApplications" :key="application.id">
          <td>
            <p :class="statusClass(application.status)">
              Status: {{ application.status }}
            </p>
          </td>
          <td>{{ getFileName(application.cvFileName) }}</td>
          <td v-if="application.job">{{ application.job.title }}</td>
          <td v-else>Job information is not available</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination controls -->
    <div class="pagination">
      <button :disabled="currentPage === 1" @click="currentPage--">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="currentPage++">Next</button>
    </div>

    <router-link to="/job-application">Go to Job Application Page</router-link>
  </div>
</template>



<script>
import axios from "axios";

export default {
  data() {
    return {
      applications: [],
      currentPage: 1, // Track the current page
      itemsPerPage: 5, // Number of items per page
    };
  },
  computed: {
    // Calculate the paginated applications
    paginatedApplications() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.applications.slice(start, end);
    },
    // Calculate the total number of pages
    totalPages() {
      return Math.ceil(this.applications.length / this.itemsPerPage);
    },
  },
  methods: {
    async fetchApplications() {
      try {
        const token = localStorage.getItem("authToken");
        if (!token) {
          console.error("No authentication token found");
          return;
        }

        const response = await axios.get(
            "http://localhost:9090/api/applications/user",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if (response.status === 204) {
          console.log("No applications found for this user.");
          return;
        }

        this.applications = response.data;
      } catch (error) {
        console.error("Error fetching applications:", error);
      }
    },

    getFileName(filePath) {
      const indexOfUnderscore = filePath.indexOf("_", filePath.indexOf("-") + 1);
      return filePath.substring(indexOfUnderscore + 1);
    },

    statusClass(status) {
      // Return the appropriate class based on the status
      if (status === "PENDING") {
        return "pending-status";
      } else if (status === "VIEWED") {
        return "viewed-status";
      }
      return "";
    },
  },
  created() {
    this.fetchApplications();
  },
};
</script>



<style>
/* Container for the table with fixed height and scroll */
.applications-table-container {
  max-height: 400px; /* Adjust the height as needed */
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 4px;
}

/* Styling for the table */
.applications-table {
  width: 100%;
  border-collapse: collapse;
  font-family: Arial, sans-serif;
}

.applications-table th,
.applications-table td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.applications-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

/* Styling for the progress bar */
.progress-bar {
  width: 100%;
  background-color: #e0e0e0;
  height: 20px;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar > div {
  height: 100%;
  text-align: center;
  color: white;
  line-height: 20px;
  white-space: nowrap;
}

/* Status-specific styles */
.pending-status {
  color: orange;
  font-weight: bold;
}

.viewed-status {
  color: green;
  font-weight: bold;
}



.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.pagination button {
  margin: 0 10px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #f4f4f4;
  cursor: pointer;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination span {
  font-size: 16px;
}
</style>
