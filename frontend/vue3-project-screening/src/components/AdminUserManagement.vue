<!-- Admin Page for User Management-->
<template>
  <div>
    <h1> Users</h1>

    <!-- Displaying users -->
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Actions</th>
        <th>Roles</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.id }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>
          <button @click="deleteUser(user.id)">Delete User</button>
        </td>






        <td>
          <button @click="addRole(user.id, 'ROLE_MODERATOR')">Add Moderator</button>
          <button @click="deleteRole(user.id, 'ROLE_MODERATOR')">Delete Moderator</button>
          <button @click="addRole(user.id, 'ROLE_ADMIN')">Add Admin</button>
          <button @click="deleteRole(user.id, 'ROLE_ADMIN')">Delete Admin</button>
          <button @click="addRole(user.id, 'ROLE_USER')">Add User</button>
          <button @click="deleteRole(user.id, 'ROLE_USER')">Delete User</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <!--router-link to="/">Go to Homepage</router-link-->
</template>

<script>
import axios from 'axios';
// Mapping role names to role IDs
const roleMapping = {
  "ROLE_ADMIN": 1,
  "ROLE_USER": 2,
  "ROLE_MODERATOR": 3
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
      axios.get('/api/user', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
          .then(response => {
            this.users = response.data;
          })
          .catch(error => {
            console.error('Error fetching users:', error);
          });
    },

    // Delete a user by ID
    deleteUser(userId) {
      const token = this.getAuthToken();
      axios.delete(`/api/user/admin/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
          .then(() => {
            this.fetchUsers(); // Refresh the user list after deletion
          })
          .catch(error => {
            console.error('Error deleting user:', error);
          });
    },

    // Add a specific role to a user
    addRole(userId, roleName) {
      const roleId = roleMapping[roleName]; // Get role ID from mapping
      const token = this.getAuthToken();

      axios.post(`/api/user/admin/role/add/${userId}/${roleId}`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
          .then(() => {
            this.fetchUsers(); // Refresh user data after role assignment
          })
          .catch(error => {
            console.error(`Error adding role ${roleName} to user:`, error);
          });
    },

    // Remove a specific role from a user
    deleteRole(userId, roleName) {
      const roleId = roleMapping[roleName]; // Get role ID from mapping
      const token = this.getAuthToken();

      axios.post(`/api/user/admin/role/delete/${userId}/${roleId}`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
          .then(() => {
            this.fetchUsers(); // Refresh user data after role removal
          })
          .catch(error => {
            console.error(`Error deleting role ${roleName} from user:`, error);
          });
    }
  }
};
</script>
<style scoped>
/*  some styling */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f4f4f4;
  text-align: left;
}

button {
  margin-right: 5px;
}

div {
  margin: 20px;
}
</style>
