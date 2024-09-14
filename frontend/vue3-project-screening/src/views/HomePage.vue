<!-- HomePage.vue -->
<template>
  <div>
    <h1>Welcome to the Job Application Portal</h1>
    <p>Please <router-link to="/login">Login</router-link> or <router-link to="/signup">Sign Up</router-link> to continue.</p>

  </div>
</template>

<script>
export default {
  name: 'HomePage'
};
</script>

<style>
/* Add any styles here */
</style>
