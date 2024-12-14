<template>
  <div class="about-container">
    <h1>About This Dissertation</h1>
    <div class="content">
      <p>
        This dissertation was developed for Harokopio University - Informatics and Telematics Department.
        It leverages distributed methods and an n-tier architecture to deliver a comprehensive system
        featuring multiple roles, robust security, and various use cases.
      </p>
      <p>
        The system integrates an AI agent (LLM - Open AI API) and utilizes RAG
        (Retrieval-Augmented Generation) methods to evaluate how well a candidate matches a job
        position by scoring their CV. Users can calculate the score manually or leverage AI Agent tools.
      </p>
      <p>
        Additional features include MinIO for object storage, enhancing the scalability and flexibility of the system.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AboutPage',
};
</script>

<style scoped>
.about-container {
  max-width: 800px;
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

.content {
  font-size: 1.1rem;
}

.content p {
  margin-bottom: 1rem;
  text-align: justify;
}

@media (min-width: 768px) {
  .content p {
    margin-bottom: 1.2rem;
  }
}
</style>
