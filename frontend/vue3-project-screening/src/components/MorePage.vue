<template>
  <div class="more-container">
    <h1>About This Project</h1>
    <div class="details">
      <p><span class="label">Creator:</span> Panagiotis Foteinopoulos</p>
      <p><span class="label">Academic ID:</span> 2021154</p>
      <p><span class="label">Department:</span> Informatics and Telematics</p>
      <p><span class="label">University:</span> Harokopio University of Athens</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MorePage',
};
</script>

<style scoped>
.more-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  font-family: Arial, sans-serif;
  line-height: 1.6;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: #333;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 1.5rem;
}

.details {
  font-size: 1.1rem;
}

.label {
  font-weight: bold;
  color: #555;
}

@media (min-width: 768px) {
  .details p {
    margin-bottom: 0.8rem;
  }
}
</style>

