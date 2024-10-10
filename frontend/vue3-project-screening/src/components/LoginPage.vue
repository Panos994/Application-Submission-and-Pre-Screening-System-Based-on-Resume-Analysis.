<!-- LoginPage.vue -->

<template>
  <div class="auth-form">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <label for="username">Username:</label>
      <input type="text" v-model="username" required />

      <label for="password">Password:</label>
      <input type="password" v-model="password" required />

      <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <router-link to="/signup">Sign up here</router-link></p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginPage',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/api/auth/signin', {
          username: this.username,
          password: this.password
        });

        // Log the response to check the token and roles
        console.log('Login response:', response.data);

        const { accessToken, roles } = response.data;
        const userRole = roles[0]; // Assuming user has one primary role

        // Store auth token and role in localStorage
        localStorage.setItem('authToken', accessToken);
        localStorage.setItem('userRole', userRole);
        console.log('Token stored:', accessToken);
        console.log('User Role:', userRole);

        // Redirect based on the user's role
        if (userRole === 'ROLE_ADMIN') {
          this.$router.push({ name: 'AdminUserManagement' });
        } else if (userRole === 'ROLE_USER') {
          this.$router.push({ name: 'JobApplication' });
        } else if (userRole === 'ROLE_MODERATOR'){
          this.$router.push({name:'AdminPage'});
        } else {
          this.$router.push({ name: 'HomePage' });
        }
      } catch (error) {
        this.errorMessage = 'Invalid login credentials';
        console.error('Login error:', error);
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

.error {
  color: red;
  margin-top: 10px;
}
</style>
