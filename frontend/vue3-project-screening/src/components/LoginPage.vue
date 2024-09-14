<template>
  <div class="auth-form">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <label for="username">Username:</label>
      <input type="username" v-model="username" required />

      <label for="password">Password:</label>
      <input type="password" v-model="password" required />

      <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <router-link to="/signup">Sign up here</router-link></p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginPage',
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/api/auth/signin', {
          username: this.username,
          password: this.password
        });

        // Log the response to check if the accessToken is received
        console.log('Login response:', response.data);

        // Store auth token in localStorage
        if (response.data.accessToken) {
          localStorage.setItem('authToken', response.data.accessToken);
          console.log('Token stored:', response.data.accessToken);

          // Redirect to job application page after login
          this.$router.push('/job-application');
        } else {
          console.error('No token received in login response');
        }
      } catch (error) {
        console.error('Login failed:', error);
      }
    }
  }
};
</script>

<style scoped>
.auth-form {
  max-width: 300px;
  margin: auto;
  padding: 20px;
}
</style>
