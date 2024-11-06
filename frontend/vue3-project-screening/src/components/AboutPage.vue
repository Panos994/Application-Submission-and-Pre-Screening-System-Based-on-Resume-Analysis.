<template>
  <div class="about-page">
    <h1>About</h1>
    <p>
      This is a Dissertation for Harokopio University - Informatics and Telematics Department.
      It uses distributed methods and n-tier architecture for developing a holistic system with
      different roles, security, and various use cases. It manipulates data with an AI agent (LLM -
      Open AI API) and incorporates RAG (Retrieval-Augmented Generation) methods.
      The system primarily calculates how well a candidate fits a job position by evaluating their
      CV score, which can be calculated manually or with an AI Agent (LLM tools).
      Additional services like MinIO are used for object storage.
    </p>
  </div>
</template>

<script>
export default {
  name: 'AboutPage',
};
</script>

<style scoped>
.about-page {
  padding: 2rem;
}
</style>
