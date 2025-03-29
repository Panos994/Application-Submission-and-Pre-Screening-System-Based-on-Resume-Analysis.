<!-- BackButton.vue for using it as a back button in some pages -->
<template>
  <button class="back-button" @click="$router.go(-1)">← Back</button>
</template>

<style scoped>
.back-button {
  background-color: transparent;
  color: #2c3e50;
  border: 1px solid #2c3e50;
  padding: 8px 16px;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 1.5rem;
  transition: 0.3s ease;
}

.back-button:hover {
  background-color: #2c3e50;
  color: white;
}
</style>
