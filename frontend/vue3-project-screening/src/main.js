import { createApp } from 'vue';
import App from './App.vue';
import { createRouter, createWebHistory } from 'vue-router';
import axios from 'axios';

// Import Vuetify components and styles
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

// Import components and views
import HomePage from './views/HomePage.vue';
import JobApplication from './components/JobApplication.vue';
import LoginPage from './components/LoginPage.vue';
import SignUpPage from './components/SignUpPage.vue';
import AdminPage from './components/AdminPage.vue';
import AdminUserManagement from "@/components/AdminUserManagement.vue";
import LayoutPage from "@/components/LayoutPage.vue";
import CandidateJobList from "@/components/CandidateJobList.vue";
import ApplicationsPage from '@/components/ViewApplicationsCV.vue';
import ModeratorJobList from '@/components/ModeratorJobList.vue';


import AboutPage from '@/components/AboutPage.vue';
import MorePage from '@/components/MorePage.vue';

import InfoPage from '@/components/InfoPage.vue';


import YourApplicationsPage from '@/components/YourApplicationsPage.vue';



// Set base URL for Axios
axios.defaults.baseURL = 'http://localhost:9090';
//axios.defaults.baseURL = 'http://13.60.60.8:30090';
//axios.defaults.baseURL = 'http://cvanalysishuait.ddns.net:30090';

// Define routes
const routes = [
    {
        path: '/',
        component: LayoutPage,
        children: [
            { path: '', name: 'HomePage', component: HomePage },
            { path: '/job-application', name: 'JobApplication', component: JobApplication },
            { path: '/login', name: 'LoginPage', component: LoginPage },
            { path: '/signup', name: 'SignUpPage', component: SignUpPage },
            { path: '/admin', name: 'AdminPage', component: AdminPage },
            { path: '/adminUserManagement', name: 'AdminUserManagement', component: AdminUserManagement },
            { path: '/CandidateJobList', name: 'CandidateJobList', component: CandidateJobList },
            { path: '/applications', component: ApplicationsPage },

            { path: '/about', component: AboutPage },
            { path: '/more', component: MorePage },
            { path: '/info', component: InfoPage },


            {path: '/yourapplications', component: YourApplicationsPage},
            {path: '/jobs', component: ModeratorJobList}
        ]
    }
];

// Create router instance
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// Create Vuetify instance
const vuetify = createVuetify({
    components,
    directives,
});

// Create Vue app instance
const app = createApp(App);
app.use(router);
app.use(vuetify);
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
