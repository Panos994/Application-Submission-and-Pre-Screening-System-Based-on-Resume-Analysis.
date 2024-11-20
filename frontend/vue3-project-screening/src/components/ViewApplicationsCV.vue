<template>
  <div>

    <!--h1 class="header">Applications</h1-->
    <center><router-link to="/admin" class="back-link">Return Back to Job Postings Page</router-link></center>
    <!-- Search input -->
    <input
        type="text"
        v-model="searchQuery"
        placeholder="Search by CV File Name"
        class="search-input"
    />

    <div class="table-container">
      <table class="job-table">
        <thead>
        <tr>
          <th>Application ID</th>
          <th>Resume Download</th>
          <th>Status</th>
          <th>Applications Views</th>
          <th>CV File Name</th>
        </tr>
        </thead>
        <tbody>
        <!-- Filtered applications based on search query -->
        <tr v-for="application in filteredApplications" :key="application.id">
          <td>{{ application.id }}</td>
          <td>
            <button @click="downloadCV(application.id)" class="download-btn">Download CV</button>
          </td>


          <td>
            <label>
              <input
                  type="checkbox"
                  :checked="application.status === 'VIEWED'"
                  :disabled="application.status === 'VIEWED'"
                  @change="updateStatus(application.id, 'VIEWED')"
              />
              Mark as Viewed
            </label>
          </td>

          <td><button @click="onApplicationView(application.id)">View Application</button></td>



          <td>{{ getFileName(application.cvFileName) }}</td>
        </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script>

import axios from 'axios';
export default {
  data() {
    return {
      applications: [],
      searchQuery: '' // Holds the search input value
    };
  },
  created() {
    this.fetchApplications();
  },
  computed: {
    // Filter applications based on the search query
    filteredApplications() {
      return this.applications.filter(application => {
        const fileName = this.getFileName(application.cvFileName);
        return fileName.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    }
  },
  methods: {
    fetchApplications() {
      fetch('http://localhost:9090/api/applications')
          .then(response => response.json())
          .then(data => {
            this.applications = data;
          });
    },
    getDownloadLink(id) {
      return `http://localhost:9090/api/applications/download/${id}`;
    },
    downloadCV(id) {
      const token = localStorage.getItem('authToken');
      fetch(this.getDownloadLink(id), {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/pdf'
        },
      })
          .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.blob();
          })
          .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = `CV_${id}.pdf`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
          })
          .catch(error => console.error('Error downloading the CV:', error));
    },

    updateStatus(id, newStatus) {
      const token = localStorage.getItem("authToken");
      axios
          .patch(
              `http://localhost:9090/api/applications/${id}/status`,
              null,
              {
                params: { status: newStatus },
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
          )
          .then((response) => {
            // Update the local application status based on the response
            const updatedApplication = response.data;
            this.applications = this.applications.map((application) =>
                application.id === id ? updatedApplication : application
            );
          })
          .catch((error) => {
            console.error("Error updating status:", error);
          });
    },
    markAsViewed(id) {
      this.applications = this.applications.map(app =>
          app.id === id ? { ...app, status: "VIEWED" } : app
      );
    },
    onApplicationView(id) {
      // Directly call `updateStatus` with 'VIEWED' status
      this.updateStatus(id, "VIEWED");
    },

    getFileName(filePath) {
      const indexOfUnderscore = filePath.indexOf('_', filePath.indexOf('-') + 1);
      return filePath.substring(indexOfUnderscore + 1);
    }
  }
};
</script>

<style scoped>
.header {
  font-size: 2em;
  margin-bottom: 20px;
}

.search-input {
  display: block;
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}


.table-container {
  margin-top: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow-x: auto; /* Enable horizontal scrolling for wide tables */
  max-height: 80vh; /* Ensure it doesn't exceed the viewport height */
  overflow-y: auto; /* Enable vertical scrolling within the table if it overflows */
}






.job-table {
  width: 100%; /* Full width */
  border-collapse: collapse;
  font-size: 14px;
}

.job-table th,
.job-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}


.job-table th {
  background-color: #f8f9fa;
  font-weight: bold;
  font-size: 14px;
}
.job-table tr:nth-child(even) {
  background-color: #f9f9f9; /* Add alternating row colors */
}


.job-table tr:hover {
  background-color: #e9ecef; /* Highlight row on hover */
}
body, html {
  height: 100%; /* Ensure the height adapts to content */
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  overflow-y: auto; /* Enable vertical scrolling */
}


.download-btn {
  padding: 6px 12px;
  font-size: 14px;
}









.download-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}






/* */
.check-status-btn:hover {
  transform: translateY(-2px);
}

.check-status-btn {
  background-color: #3490dc;
}












.download-btn:active {
  transform: translateY(0);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.back-link {
  display: block;
  margin-top: 20px;
  color: #3490dc;
  text-decoration: none;
}

.back-link:hover {
  text-decoration: underline;
}


input[type="checkbox"]:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
</style>
