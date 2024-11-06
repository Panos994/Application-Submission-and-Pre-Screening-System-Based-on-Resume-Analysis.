<template>
  <div class="layout">
    <!-- Sidebar -->
    <div class="sidebar">
      <h2 class="sidebar-title"></h2>
      <div class="sidebar-links">
        <router-link to="/about" class="sidebar-link">About</router-link>
        <router-link to="/more" class="sidebar-link">More</router-link>
        <br><br><br><br><br><br><br> <br><br><br><br><br><br><br>
        <button class="logout-btn" @click="logout">Log Out</button>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="main-content">
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
  </div>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('authToken');
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
/* Layout Styling */
.layout {
  display: flex;
  height: 100vh;
}

/* Sidebar Styling */
.sidebar {
  width: 150px;
  background-color: #333;
  color: white;
  padding: 2rem 1rem;
  position: fixed;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-title {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.sidebar-links {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.sidebar-link {
  color: white;
  text-decoration: none;
  font-size: 1.2rem;
  padding: 0.5rem 0;
}

.sidebar-link:hover {
  text-decoration: underline;
}

/* Main Content Styling */
.main-content {
  margin-left: 250px; /* Aligns content to the right of the sidebar */
  flex: 1;
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  color: white;
  padding: 1rem;
}

.navbar {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.home-link, .navbar a {
  font-size: 1.5rem;
  color: white;
  text-decoration: none;
}

.home-link:hover, .navbar a:hover {
  text-decoration: underline;
}

.logout-btn {
  background-color: #0073b1;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-btn:hover {
  background-color: #005a8c;
}
</style>
