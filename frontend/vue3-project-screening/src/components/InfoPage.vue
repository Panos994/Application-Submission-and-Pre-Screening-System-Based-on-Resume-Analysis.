<template>
  <div class="help-container">
    <BackButton />
    <h1>Help Center</h1>
    <p class="intro">
      This distributed system consists of three primary roles: <b>ADMIN</b>, <b>USER</b>, and <b>MODERATOR</b>. Each role has specific responsibilities and functionalities, detailed below.
    </p>

    <div class="role-section">
      <div class="role-card">
        <h2>USER</h2>
        <ul>
          <li>Sign up and log in with your credentials.</li>
          <li>Access available job positions to check your preferences.</li>
          <li>Select a job from the main USER page, upload your CV, and check your score.</li>
          <li>Use the AI agent to receive suggestions for the top 3 jobs that best fit your CV.</li>
        </ul>
      </div>

      <div class="role-card">
        <h2>MODERATOR</h2>
        <ul>
          <li>Create new job postings and save them.</li>
          <li>View the highest scores of current jobs submitted by candidates.</li>
          <li>Search for a candidate's CV and contact them based on their score.</li>
          <li>Search and download CVs from the CV repositories page using the search input.</li>
        </ul>
      </div>

      <div class="role-card">
        <h2>ADMIN</h2>
        <ul>
          <li>Add or remove users and assign roles.</li>
          <li>Delete users or roles as necessary.</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import BackButton from "@/components/BackButton.vue";
export default {
  name: 'InfoPage',
  components: {BackButton},
};
</script>


<style scoped>
.help-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  font-family: Arial, sans-serif;
  line-height: 1.6;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 1rem;
}

.intro {
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 2rem;
  color: #555;
}

.role-section {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}

.role-card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.role-card h2 {
  color: #2c3e50;
  margin-bottom: 1rem;
}

.role-card ul {
  list-style-type: disc;
  padding-left: 1.5rem;
  color: #333;
}

.role-card ul li {
  margin-bottom: 0.5rem;
}

@media (min-width: 600px) {
  .role-section {
    grid-template-columns: 1fr 1fr;
  }
}

@media (min-width: 900px) {
  .role-section {
    grid-template-columns: 1fr 1fr 1fr;
  }
}
</style>