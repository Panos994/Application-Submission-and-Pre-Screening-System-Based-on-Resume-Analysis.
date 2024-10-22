<template>
  <div>
    <h1>Applications</h1>
    <table class="job-table">



      <thead>
      <tr>
        <th>Application ID</th>
        <th>Resume Download</th>
        <th>Cv file Name</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="application in applications" :key="application.id">
        <td>{{ application.id }}</td>
        <td>
          <button @click="downloadCV(application.id)">Download CV</button>
        </td>
        <td>{{application.cvFileName}}</td>
      </tr>
      </tbody>
    </table>

    <router-link to="/admin">Return Back to Job Postings Page</router-link>


  </div>
</template>

<script>
export default {
  data() {
    return {
      applications: [],
    };
  },
  created() {
    this.fetchApplications();
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
      const token = localStorage.getItem('authToken'); // Assuming you're storing the token in local storage
      fetch(this.getDownloadLink(id), {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`, // Include the token in the request headers
          'Content-Type': 'application/pdf', // Change this if necessary
        },
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.blob(); // Convert response to a Blob
          })
          .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = `CV_${id}.pdf`; // Set the default name for the downloaded file
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url); // Clean up the URL object
          })
          .catch(error => console.error('Error downloading the CV:', error));
    },
  },
};
</script>

<style scoped>
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
