<!-- Layout.vue -->
<template>
  <div>
    <header class="header">
      <h1>CRM | Job Portal</h1>

      <nav class="navbar">
        <router-link to="/" class="home-link">Home</router-link>
        <button class="logout-btn" @click="logout">Log Out</button>
      </nav>
    </header>

    <main>
      <router-view></router-view>
    </main>


  </div>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('authToken');
      this.$router.push('/');
    }
  }
};
</script>

<style>
.header {
  display: flex;
  justify-content: space-between;
  background-color: #333;
  padding: 1rem;
  color: white;
}

.navbar {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.home-link {
  font-size: 1.5rem; /* Larger font for Home link */
  color: white;
  text-decoration: none;
}

.home-link:hover {
  text-decoration: underline;
}

.logout-btn {
  background-color: #ff4d4d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #e63939;
}
</style>

