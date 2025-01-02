<template>
  <div class="layout">
    <!-- Sidebar -->
    <div class="sidebar">
      <h2 class="sidebar-title"></h2>
      <div class="sidebar-links">
        <router-link to="/about" class="sidebar-link">About</router-link>
        <router-link to="/more" class="sidebar-link">More</router-link>
        <router-link to="/info" class="sidebar-link">Help</router-link>
      </div>
      <button class="logout-btn" @click="logout">Exit also from here </button>
    </div>

    <!-- Main Content Area -->
    <div class="main-content">
      <header class="header">
        <h1 class="title-3d">CRM | Job Portal</h1>
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
  overflow: hidden; /* Prevents page overflow */
  background-color: #d3d3d3;
}

/* Sidebar Styling */
.sidebar {
  width: 150px;
  min-width: 150px; /* Ensures sidebar doesn't shrink */
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
  flex-grow: 1; /* Takes available vertical space */
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

/* Position the logout button at the bottom */
.logout-btn {
  background-color: #0073b1;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: auto; /* Pushes the button to the bottom */
}

.logout-btn:hover {
  background-color: #005a8c;
}

/* Main Content Styling */
.main-content {

  margin-left: 150px; /* Sidebar width */
  flex: 1;
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #222;
  color: white;
  padding: 1rem;
  position: relative;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.4);
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








.title-3d {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
  text-transform: uppercase;
  text-align: center;
  margin: 0;
  position: relative;
  animation: fadeInTitle 2s ease-in-out;
  background: linear-gradient(135deg, #ffcc00, #ff66cc); /* Brighter gradient */
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.6),
  -1px -1px 5px rgba(255, 255, 255, 0.4);
}

.title-3d:hover {
  animation: bounce 1s ease infinite;
  cursor: pointer;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.7),
  -2px -2px 8px rgba(255, 255, 255, 0.5);
}

@keyframes fadeInTitle {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
</style>

