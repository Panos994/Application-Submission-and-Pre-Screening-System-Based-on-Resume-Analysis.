<template>
  <div>
    <h1 class="header">Applications</h1>

    <!-- Search input -->
    <input
        type="text"
        v-model="searchQuery"
        placeholder="Search by CV File Name"
        class="search-input"
    />

    <table class="job-table">
      <thead>
      <tr>
        <th>Application ID</th>
        <th>Resume Download</th>
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
        <td>{{ getFileName(application.cvFileName) }}</td>
      </tr>
      </tbody>
    </table>

    <router-link to="/admin" class="back-link">Return Back to Job Postings Page</router-link>
  </div>
</template>

<script>
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

.job-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.job-table th,
.job-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.job-table th {
  background-color: #f8f9fa;
}

.download-btn {
  display: inline-block;
  padding: 8px 16px;
  font-size: 16px;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.download-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
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
</style>
