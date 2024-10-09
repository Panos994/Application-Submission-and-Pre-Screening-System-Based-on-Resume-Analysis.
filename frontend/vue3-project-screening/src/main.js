// main.js
import { createApp } from 'vue';
import App from './App.vue';
import { createRouter, createWebHistory } from 'vue-router';
import axios from 'axios';

// Import components and views
import HomePage from './views/HomePage.vue';
import JobApplication from './components/JobApplication.vue';
import LoginPage from './components/LoginPage.vue';
import SignUpPage from './components/SignUpPage.vue';
import AdminPage from './components/AdminPage.vue';
import AdminUserManagement from "@/components/AdminUserManagement.vue"; // Import AdminPage

// Set base URL for Axios
axios.defaults.baseURL = 'http://localhost:9090';

// Define routes
const routes = [
    { path: '/', name: 'HomePage', component: HomePage },
    { path: '/job-application', name: 'JobApplication', component: JobApplication },
    { path: '/login', name: 'LoginPage', component: LoginPage },
    { path: '/signup', name: 'SignUpPage', component: SignUpPage },
    { path: '/admin', name: 'AdminPage', component: AdminPage }, // Add route for AdminPage

    { path: '/adminUserManagement', name: 'AdminUserManagement', component: AdminUserManagement }


];

// Create router instance
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// Create Vue app instance
const app = createApp(App);
app.use(router);
app.config.globalProperties.$axios = axios;
app.mount('#app');

// Add navigation guard
router.beforeEach((to, from, next) => {
    const isAuthenticated = !!localStorage.getItem('authToken'); // Check if user is authenticated

    if (to.name === 'JobApplication' && !isAuthenticated) {
        next({ name: 'LoginPage' }); // Redirect to login if not authenticated
    } else {
        next(); // Proceed if authenticated
    }
});
