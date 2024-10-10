<!-- Sign-up Page -->

<template>
  <div class="auth-form">
    <h2>Sign Up</h2>
    <form @submit.prevent="signUp">
      <label for="username">Username:</label>
      <input type="username" v-model="username" required />
      <label for="email">Email:</label>
      <input type="email" v-model="email" required />

      <label for="password">Password:</label>
      <input type="password" v-model="password" required />

      <button type="submit">Sign Up</button>
    </form>
    <p>Already have an account? <router-link to="/login">Login here</router-link></p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SignUpPage',
  data() {
    return {
      username: '',
      email: '',
      password: ''
    };
  },
  methods: {
    async signUp() {
      try {
        await axios.post('/api/auth/signup', {
          username: this.username,
          email: this.email,
          password: this.password
        });
        this.$router.push('/login');
      } catch (error) {
        console.error('Sign Up failed:', error);
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
