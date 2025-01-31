<!-- Employee page for posting jobs -->
<template>
  <div class="container">

    <div class="form-container">



      <h1>Welcome, {{ username }}. Start adding your Job Postings</h1>
      <form @submit.prevent="submitJob">





        <div class="form-group">
          <label for="title">Title:</label>
          <input type="text" v-model="job.title" required class="input-field" />

        </div>



<!-- --here -->
        <v-form>
          <v-row>
            <v-col cols="12" md="6">
              <v-autocomplete
                  v-model="job.location"
                  :items="locations"
                  label="Location"
                  required
              ></v-autocomplete>
            </v-col>

            <v-col cols="12" md="6">
              <v-autocomplete
                  v-model="job.sector"
                  :items="sectors"
                  label="Segment"
                  required
              ></v-autocomplete>
            </v-col>
          </v-row>
        </v-form>





        <div class="form-group">
          <label for="workType">Type of Employment:</label>
          <input type="text" v-model="job.workType" list="workTypeList" required class="input-field" />
          <datalist id="workTypeList">
            <option value="Full Time - On site"></option>
            <option value="Part Time - On site"></option>
            <option value="Full Time - Hybrid"></option>
            <option value="Part Time - Hybrid"></option>
            <option value="Full Time - Remote"></option>
            <option value="Part Time - Remote"></option>
          </datalist>
        </div>

        <div class="form-group">
          <label for="jobLevel">Level of position:</label>
          <input type="text" v-model="job.jobLevel" list="jobLevelList" required class="input-field" />

          <datalist id="jobLevelList">
            <option value="Junior Level"></option>
            <option value="Mid Level"></option>
            <option value="Senior Level"></option>
            <option value="Mid-Senior Level"></option>
            <option value="Associate"></option>
            <option value="Specialist"></option>
            <option value="Manager"></option>
            <option value="Associate Manager"></option>
            <option value="Senior Manager"></option>
            <option value="Lead"></option>
          </datalist>
        </div>






        <div class="form-group">
          <label for="description">Description:</label>
          <textarea v-model="job.description" required class="textarea-field"></textarea>
        </div>



        <div class="form-group">
          <label for="requiredSkills">Required Skills (comma-separated):</label>
          <input type="text" v-model="job.requiredSkills" required class="input-field" />
        </div>



        <div class="form-group">
          <label for="minExperience">Minimum Experience (years):</label>
          <input type="number" v-model.number="job.minExperience" required min="0" class="input-field" />
        </div>



        <div class="form-group">
          <label for="educationLevel">Education Level:</label>
          <div class="checkbox-group">
            <input type="checkbox" value="BSc" v-model="job.educationLevels"> BSc
            <input type="checkbox" value="Master" v-model="job.educationLevels"> Master
            <input type="checkbox" value="PhD" v-model="job.educationLevels"> PhD
            <input type="checkbox" value="High School" v-model="job.educationLevels"> High School
          </div>
        </div>



        <div class="form-group">
          <label for="institutionType">Institution Type:</label>
          <div class="checkbox-group">
            <input type="checkbox" value="AEI" v-model="job.institutionTypes"> Ανώτατο Εκπαιδευτικό Ίδρυμα (AEI)
            <input type="checkbox" value="TEI" v-model="job.institutionTypes"> Τεχνολογικό Εκπαιδευτικό Ίδρυμα (TEI)
            <input type="checkbox" value="PrivateCollege" v-model="job.institutionTypes"> Ιδιωτικό Κολλέγιο
            <input type="checkbox" value="IEK" v-model="job.institutionTypes"> Ινστιτούτο Εκπαιδευτικής Κατάρτισης (IEK)
            <input type="checkbox" value="ForeignUniversity" v-model="job.institutionTypes"> Πανεπιστήμιο εξωτερικού
          </div>
        </div>

        <br><br><br><br><br><br><br>

        <div class="form-group">
          <label for="universityPreference">Preferred Universities:</label>
          <div>
            <select v-model="job.universityPreferences" multiple class="select-field">
              <option value="National and Kapodistrian University of Athens">National and Kapodistrian University of Athens</option>
              <option value="Harokopio University of Athens">Harokopio University of Athens</option>
              <option value="Athens University of Economics and Business">Athens University of Economics and Business</option>
              <option value="Aristotle University of Thessaloniki">Aristotle University of Thessaloniki</option>
              <option value="National Technical University of Athens">National Technical University of Athens</option>
              <option value="University of Piraeus">University of Piraeus</option>
              <option value="University of Patras">University of Patras</option>
              <option value="University of Crete">University of Crete</option>
              <option value="University of West Attica">University of West Attica</option>
              <option value="Agricultural University of Athens">Agricultural University of Athens</option>
              <option value="Athens School of Fine Arts">Athens School of Fine Arts</option>
              <option value="Democritus University of Thrace">Democritus University of Thrace</option>
              <option value="Technical University of Crete">Technical University of Crete</option>
              <option value="University of Peloponnese">University of Peloponnese</option>
              <option value="University of the Aegean">University of the Aegean</option>
              <option value="Panteion University">Panteion University</option>
              <option value="Ionian University">Ionian University</option>
              <option value="International Hellenic University">International Hellenic University</option>
              <option value="University of Ioannina">University of Ioannina</option>
              <option value="University of Macedonia">University of Macedonia</option>
              <option value="University of Patras">University of Patras</option>
              <option value="University of Western Macedonia">University of Western Macedonia</option>
              <option value="University of Thessaly">University of Thessaly</option>
              <option value="Hellenic Open University">Hellenic Open University</option>
              <option value="School of Pedagogical and Technological Education (ASPETE)">School of Pedagogical and Technological Education (ASPETE)</option>
            </select>
          </div>
        </div>


        <br><br><br><br><br>

        <div class="form-group">
          <label for="experienceWeight">Weight for Experience (%):</label>
          <input type="number" v-model.number="criteriaWeights.minExperience" required min="0" max="100" />
        </div>

        <br><br><br>

        <div class="form-group">
          <label for="educationWeight">Weight for Education (%):</label>
          <input type="number" v-model.number="criteriaWeights.education" required min="0" max="100" />
        </div>

        <br><br><br>

        <div class="form-group">
          <label for="skillsWeight">Weight for Skills (%):</label>
          <input type="number" v-model.number="criteriaWeights.skills" required min="0" max="100" />
        </div>


        <br><br><br>
        <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
        <button type="submit" class="submit-button">Save Job</button>
      </form>
    </div>



    <div class="job-list-container">

      <h2>Job List</h2>

      <router-link to="/applications">Find and Download CVs through Candidates CV Files</router-link>
      <table class="job-table">
        <thead>
        <tr>

          <th>Title</th>
          <th>Location</th>
          <th>Department</th>
          <th>Type of Employment</th>
          <th>Job Level</th>




          <th>Description</th>
          <th>Education Level</th>
          <th>Institution Type</th> <!-- Added Institution Type -->
          <th>Preferred Universities</th> <!-- Added Preferred Universities -->






          <th>Required Skills</th>
          <th>Experience</th>
          <th>Score Weights</th>
          <th>Highest Candidates' Scores</th>
          <th>Candidates CV Files</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="job in jobs" :key="job.id">

          <td>{{ job.title }}</td>
          <td>{{ job.location }}</td>
          <td>{{ job.sector }}</td>
          <td>{{ job.workType }}</td>
          <td>{{ job.jobLevel }}</td>





          <td>{{ job.description }}</td>
          <td>{{ job.educationLevel }}</td>
          <td>{{ job.institutionType }}</td> <!-- Added Institution Type -->
          <td>{{ job.universityPreference }}</td>


          <td>{{ job.requiredSkills }}</td>
          <td>{{ job.minExperience }} years</td>
          <td>Experience: {{ job.minExperienceWeight }}%, Education: {{ job.educationWeight }}%, Skills: {{ job.skillsWeight }}%</td>
          <td>Highest Score: {{ job.topMatchScore || 'N/A' }} </td>
          <td>CV Files: {{ job.topCvFileNames || 'N/A' }} </td>


        </tr>
        </tbody>
      </table>
      <button @click="exportToExcel" class="export-button">Export to Excel</button>
    </div>




  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: localStorage.getItem("username") || "User",
      job: {

        title: '',
        location: '',
        sector: '',
        workType: '',
        jobLevel: '',
        description: '',
        requiredSkills: '',
        minExperience: null,
        institutionTypes: [],
        educationLevels: [], // Array to store multiple selections
        universityPreferences: []
      },
      locations: ['Athens', 'Thessaloniki', 'Heraklion', 'Patras'], // Adjust or extend this list as needed
      sectors: [
        'Information Technology', 'Engineering', 'Software Engineering', 'Marketing',
        'Operations', 'DevOps', 'Finance', 'HR', 'Talent Acquisition', 'Sales',
        'Consulting', 'Accounting'
      ],
      criteriaWeights: {
        minExperience: 0,
        education: 0,
        skills: 0
      },
      jobs: [],
      errorMessage: ''
    };
  },
  methods: {

    checkLocation() {
      console.log('Current location input:', this.job.location);
    },
    checkSector() {
      console.log('Current sector input:', this.job.sector);
    },




    async submitJob() {
      const totalWeight = this.criteriaWeights.minExperience + this.criteriaWeights.education + this.criteriaWeights.skills;

      if (totalWeight !== 100) {
        this.errorMessage = 'The total of all weights must equal 100%.';
        return;
      } else {
        this.errorMessage = '';
      }

      try {
        const authToken = localStorage.getItem('authToken');

        const educationLevelString = this.job.educationLevels.join(', ');
        const institutionTypeString = this.job.institutionTypes.join(', ');
        const universityPreferenceString = this.job.universityPreferences.join(', ');

        const jobToSave = {

          title: this.job.title,
          location: this.job.location,
          sector: this.job.sector,
          workType: this.job.workType,
          jobLevel: this.job.jobLevel,
          description: this.job.description,
          requiredSkills: this.job.requiredSkills,
          minExperience: this.job.minExperience,
          institutionType: institutionTypeString,
          educationLevel: educationLevelString,
          universityPreference: universityPreferenceString,
          minExperienceWeight: this.criteriaWeights.minExperience,
          educationWeight: this.criteriaWeights.education,
          skillsWeight: this.criteriaWeights.skills
        };

        await axios.post('http://localhost:9090/api/jobs/create', jobToSave, {
          headers: {
            'Authorization': `Bearer ${authToken}`,
            'Content-Type': 'application/json'
          }
        });

        alert('Job saved successfully!');
        this.resetForm();
        await this.fetchJobs(); //this.fetchJobs();
      } catch (error) {
        console.error('Error saving job:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || 'Error occurred while saving the job.';
        } else {
          this.errorMessage = 'An error occurred while saving the job.';
        }
      }
    },
    resetForm() {
      this.job = {

        title: '',
        location: '',
        sector: '',
        workType: '',
        jobLevel: '',
        description: '',
        requiredSkills: '',
        minExperience: null,
        institutionTypes: [],
        educationLevels: [],
        universityPreferences: []
      };
      this.criteriaWeights = {
        minExperience: 0,
        education: 0,
        skills: 0
      };
    },
    async fetchJobs() {
      try {
        const authToken = localStorage.getItem('authToken');
        const username = localStorage.getItem('username');


        console.log('Auth Token:', authToken); // Log the auth token
        console.log('Username:', username); // Log the username

        if (!username) {
          this.errorMessage = 'Username is not available.';
          return;
        }

        const response = await axios.get(`http://localhost:9090/api/jobs/user/${username}`, {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });

        // Check if response.data is an array before mapping
        if (Array.isArray(response.data)) {
          this.jobs = response.data.map(job => {
            return {
              ...job,
              topMatchScore: job.topMatchScore || 'N/A', // Fallback to 'N/A' if undefined
              topCvFileNames: job.topCvFileNames || 'N/A' // Fallback to 'N/A' if undefined
            };
          });
        } else {
          this.errorMessage = 'No jobs found for this user or invalid response format.';
        }
      } catch (error) {
        console.error('Error fetching jobs:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || 'Error occurred while fetching jobs.';
        } else {
          this.errorMessage = 'An error occurred while fetching jobs.';
        }
      }
    },

    exportToExcel() {
      const authToken = localStorage.getItem('authToken');
      const url = 'http://localhost:9090/api/jobs/export';

      // Trigger the file download
      window.location.href = `${url}?authToken=${authToken}`;
    }
  },


  created() {
    this.fetchJobs(); // Fetch jobs when the component is created
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-height: 100vh;
  overflow: hidden;
}

.form-container {
  padding: 20px;
  max-height: 45vh; /* Adjust height to fit content and page layout */
  overflow-y: auto;
  border: 1px solid #ccc; /* Optional, for visual separation */
  border-radius: 8px;
  background-color: #f9f9f9;
}

.job-list-container {
  padding: 20px;
  max-height: 45vh; /* Adjust height to fit content and page layout */
  overflow-y: auto;
  border: 1px solid #ccc; /* Optional, for visual separation */
  border-radius: 8px;
  background-color: #f9f9f9;
}

h1, h2 {
  color: #333;
  font-size: 1.2em; /* Smaller heading font size */
}

.form-group {
  margin-bottom: 8px; /* Reduce space between form fields */
}

label {
  display: block;
  margin-bottom: 4px;
  font-weight: bold;
  font-size: 0.85em; /* Smaller font for labels */
}

.input-field,
.textarea-field,
.select-field {
  width: 100%;
  padding: 6px; /* Smaller padding to shrink field size */
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 0.9em; /* Slightly smaller font size for inputs */
}

.textarea-field {
  height: 70px;
}

.submit-button,
.export-button {
  background-color: #28a745;
  color: white;
  padding: 6px 10px; /* Smaller padding for buttons */
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85em; /* Smaller button font size */
}

.submit-button:hover,
.export-button:hover {
  background-color: #218838;
}

.job-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
  font-size: 0.8em; /* Smaller font for table content */
}

.job-table th,
.job-table td {
  border: 1px solid #ccc;
  padding: 5px; /* Smaller padding to reduce cell size */
  text-align: left;
}

.job-table th {
  background-color: #f8f9fa;
}
</style>
