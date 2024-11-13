<!-- Admin Page for User Management (update in buttons)-->
<template>
  <div>
    <h1 class="text-3xl font-bold mb-6 custom-header"><center>Users</center></h1>

    <!-- Displaying users -->
    <div class="table-container">
    <table class="w-full border border-gray-300 mt-4">
      <thead>
      <tr class="bg-gray-200">
        <th class="border border-gray-300 p-4">ID</th>
        <th class="border border-gray-300 p-4">Username</th>
        <th class="border border-gray-300 p-4">Email</th>
        <th class="border border-gray-300 p-4">Delete Action</th>
        <th class="border border-gray-300 p-4">Roles</th>
        <th class="border border-gray-300 p-4">Assign Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id" class="hover:bg-gray-100">
        <td class="border border-gray-300 p-4">{{ user.id }}</td>
        <td class="border border-gray-300 p-4">{{ user.username }}</td>
        <td class="border border-gray-300 p-4">{{ user.email }}</td>
        <td class="border border-gray-300 p-4">
          <button @click="deleteUser(user.id)" class="btn btn-danger">
            Delete User
          </button>
        </td>
        <td class="border border-gray-300 p-4">
          <ul class="list-disc pl-5">
            <li v-for="role in user.roles" :key="role" class="role-item">{{ role }}</li>
          </ul>
        </td>
        <td class="border border-gray-300 p-4 space-y-2">
          <button @click="addRole(user.id, 'ROLE_MODERATOR')" class="btn btn-primary">
            Add Moderator
          </button>
          <button @click="deleteRole(user.id, 'ROLE_MODERATOR')" class="btn btn-warning">
            Delete Moderator
          </button>
          <button @click="addRole(user.id, 'ROLE_ADMIN')" class="btn btn-success">
            Add Admin
          </button>
          <button @click="deleteRole(user.id, 'ROLE_ADMIN')" class="btn btn-warning">
            Delete Admin
          </button>
          <button @click="addRole(user.id, 'ROLE_USER')" class="btn btn-info">
            Add User
          </button>
          <button @click="deleteRole(user.id, 'ROLE_USER')" class="btn btn-warning">
            Delete User
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    </div>
  </div>

  <!--router-link to="/">Go to Homepage</router-link-->
</template>

<script>
import axios from 'axios';

// Mapping role names to role IDs
const roleMapping = {
  ROLE_ADMIN: 1,
  ROLE_USER: 2,
  ROLE_MODERATOR: 3,
};

export default {
  data() {
    return {
      users: [], // Array of users
    };
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    // Get the auth token from localStorage
    getAuthToken() {
      return localStorage.getItem('authToken');
    },

    // Fetch all users
    fetchUsers() {
      const token = this.getAuthToken();
      axios
          .get('/api/user/user/roles', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then((response) => {
            console.log(response.data);
            this.users = response.data;
          })
          .catch((error) => {
            console.error('Error fetching users:', error);
          });
    },

    // Delete a user by ID
    deleteUser(userId) {
      const token = this.getAuthToken();
      axios
          .delete(`/api/user/admin/${userId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then(() => {
            this.fetchUsers(); // Refresh the user list after deletion
          })
          .catch((error) => {
            console.error('Error deleting user:', error);
          });
    },

    // Add a specific role to a user
    addRole(userId, roleName) {
      const roleId = roleMapping[roleName]; // Get role ID from mapping
      const token = this.getAuthToken();

      axios
          .post(`/api/user/admin/role/add/${userId}/${roleId}`, {}, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then(() => {
            this.fetchUsers(); // Refresh user data after role assignment
          })
          .catch((error) => {
            console.error(`Error adding role ${roleName} to user:`, error);
          });
    },

    // Remove a specific role from a user
    deleteRole(userId, roleName) {
      const roleId = roleMapping[roleName]; // Get role ID from mapping
      const token = this.getAuthToken();

      axios
          .post(`/api/user/admin/role/delete/${userId}/${roleId}`, {}, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then(() => {
            this.fetchUsers(); // Refresh user data after role removal
          })
          .catch((error) => {
            console.error(`Error deleting role ${roleName} from user:`, error);
          });
    },
  },
};
</script>

<style scoped>

.table-container {
  max-height: 400px; /* Adjust the height as needed */
  overflow-y: auto; /* Enables vertical scroll within the container */
  border: 1px solid #ddd; /* Optional border for container */
  border-radius: 8px; /* Optional rounded corners */
  margin-top: 10px; /* Optional margin */
}

/* Styling for the table */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th,
td {
  border: 1px solid #ddd;
  padding: 10px;
}

th {
  background-color: #f4f4f4;
  text-align: left;
}

/* Custom font for header */
.custom-header {
  font-family: 'Arial', sans-serif; /* Change this to your preferred font */
  color: #787878; /* A nice color for the header */
}

/* Button styles */
.btn {
  display: inline-block;
  padding: 10px 20px; /* Smaller padding for slightly smaller buttons */
  font-size: 16px; /* Adjusted font size */
  border: none;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.btn-danger {
  background-color: #e3342f; /* Red */
}

.btn-primary {
  background-color: #3490dc; /* Blue */
}

.btn-success {
  background-color: #38c172; /* Green */
}

.btn-warning {
  background-color: #ffed4a; /* Yellow */
}

.btn-info {
  background-color: #6cb2eb; /* Light Blue */
}

/* 3D effect on hover */
.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.btn:active {
  transform: translateY(0);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Custom font for roles */
.role-item {
  font-family: 'Georgia', serif; /* Change this to a nicer font for roles */
  font-weight: bold; /* Bold text for better visibility */
  color: #2c3e50; /* Dark color for role items */
}

div {
  margin: 20px;
}
</style>

