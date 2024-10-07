<template>
  <div>
    <h1>Manage Job Requirements</h1>
    <form @submit.prevent="submitJob">
      <label for="title">Title:</label>
      <input type="text" v-model="job.title" required />

      <label for="description">Description:</label>
      <textarea v-model="job.description" required></textarea>

      <label for="requiredSkills">Required Skills (comma-separated):</label>
      <input type="text" v-model="job.requiredSkills" required />

      <label for="minExperience">Minimum Experience (years):</label>
      <input type="number" v-model.number="job.minExperience" required min="0" />

      <label for="educationLevel">Education Level:</label>
      <div>
        <input type="checkbox" value="BSc" v-model="job.educationLevels"> BSc
        <input type="checkbox" value="Master" v-model="job.educationLevels"> Master
        <input type="checkbox" value="PhD" v-model="job.educationLevels"> PhD
        <input type="checkbox" value="High School" v-model="job.educationLevels"> High School
      </div>


      <label for="institutionType">Institution Type:</label>
      <div>
        <input type="checkbox" value="AEI" v-model="job.institutionTypes"> Ανώτατο Εκπαιδευτικό Ίδρυμα (AEI)
        <input type="checkbox" value="TEI" v-model="job.institutionTypes"> Τεχνολογικό Εκπαιδευτικό Ίδρυμα (TEI)
        <input type="checkbox" value="PrivateCollege" v-model="job.institutionTypes"> Ιδιωτικό Κολλέγιο
        <input type="checkbox" value="IEK" v-model="job.institutionTypes"> Ινστιτούτο Εκπαιδευτικής Κατάρτισης (IEK)
        <input type="checkbox" value="ForeignUniversity" v-model="job.institutionTypes"> Πανεπιστήμιο εξωτερικού
      </div>




      <label for="universityPreference">Preferred Universities:</label>
      <div>
        <select v-model="job.universityPreferences" multiple>
          <option value="National and Kapodistrian University of Athens">National and Kapodistrian University of Athens</option>
          <option value="Harokopio University of Athens">Harokopio University of Athens</option>

          <option value="Athens University of Economics and Business">Athens University of Economics and Business</option>
          <option value="Aristotle University of Thessaloniki">Aristotle University of Thessaloniki</option>
          <option value="National Technical University of Athens[">National Technical University of Athens[</option>
          <option value="University of Piraeus ">University of Piraeus</option>
          <option value="University of Patras">University of Patras</option>
          <option value="University of Crete">University of Crete</option>

          <option value="University of West Attica (formerly TEI of Athens and TEI of Piraeus) ">University of West Attica (formerly TEI of Athens and TEI of Piraeus) </option>
          <option value="Agricultural University of Athens">Agricultural University of Athens</option>
          <option value="Athens School of Fine Arts">Athens School of Fine Arts</option>
          <option value="Democritus University of Thrace">Democritus University of Thrace</option>

          <option value="Technical University of Crete ">Technical University of Crete </option>
          <option value="University of Peloponnese">University of Peloponnese</option>
          <option value="University of the Aegean ">University of the Aegean </option>
          <option value="University of Crete ">University of Crete </option>
          <option value="Panteion University ">Panteion University </option>
          <option value="Ionian University">Ionian University </option>
          <option value="International Hellenic University">International Hellenic University </option>
          <option value="University of Ioannina ">University of Ioannina </option>
          <option value="University of Macedonia ">University of Macedonia</option>
          <option value="University of Patras ">University of Patras</option>
          <option value="University of Western Macedonia "> University of Western Macedonia</option>
          <option value="University of Thessaly "> University of Thessaly</option>
          <option value="Hellenic Open University">Hellenic Open University</option>
          <option value="School of Pedagogical and Technological Education (ASPETE) ">  School of Pedagogical and Technological Education (ASPETE)</option>










          <!-- Add more Greek universities as needed -->
        </select>
      </div>






      <label for="experienceWeight">Weight for Experience (%):</label>
      <input type="number" v-model.number="criteriaWeights.minExperience" required min="0" max="100" />

      <label for="educationWeight">Weight for Education (%):</label>
      <input type="number" v-model.number="criteriaWeights.education" required min="0" max="100" />

      <label for="skillsWeight">Weight for Skills (%):</label>
      <input type="number" v-model.number="criteriaWeights.skills" required min="0" max="100" />

      <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
      <button type="submit">Save Job</button>
    </form>

    <h2>Job List</h2>
    <ul>
      <li v-for="job in jobs" :key="job.id">
        <strong>Title:</strong> {{ job.title }} -
        <strong>Brief Description:</strong> {{ job.description }} -
        <strong>Education Level:</strong> {{ job.educationLevel }} -
        <strong>Preferred Universities:</strong> {{ job.universityPreference }} -
        <strong>Institution:</strong> {{ job.institutionType}} -
        <strong>Required Skills:</strong> {{ job.requiredSkills }} -
        <strong>Experience (years):</strong> {{ job.minExperience }} -
        <strong>Weights:</strong> Experience: {{ job.minExperienceWeight }}%, Education: {{ job.educationWeight }}%, Skills: {{ job.skillsWeight }}% -
        <strong>Highest Score:</strong> {{ job.topMatchScore || 'N/A' }} -
        <strong>CV Files:</strong> {{ job.topCvFileNames || 'N/A' }}
      </li>
      <br><br>
      <button @click="exportToExcel">Export to Excel</button>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      job: {
        title: '',
        description: '',
        requiredSkills: '',
        minExperience: null,
        institutionTypes: [],
        educationLevels: [], // Array to store multiple selections
        universityPreferences: []
      },
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
    async submitJob() {
      // Calculate the total weight
      const totalWeight = this.criteriaWeights.minExperience + this.criteriaWeights.education + this.criteriaWeights.skills;

      // Validate that the total weight is 100%
      if (totalWeight !== 100) {
        this.errorMessage = 'The total of all weights must equal 100%.';
        return;
      } else {
        this.errorMessage = '';
      }

      try {
        const authToken = localStorage.getItem('authToken');
        console.log('Token:', authToken);



        const educationLevelString = this.job.educationLevels.join(', ');

        const institutionTypeString = this.job.institutionTypes.join(', ');

        const universityPreferenceString = this.job.universityPreferences.join(', ');

        // Construct the job object with weights
        const jobToSave = {
          title: this.job.title,
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
        // Reset the form fields
        this.resetForm();

        // Fetch the latest jobs after saving
        this.fetchJobs();
      } catch (error) {
        console.error('Error saving job:', error);
        if (error.response) {
          this.errorMessage = error.response.data;
        } else {
          this.errorMessage = 'An error occurred while saving the job.';
        }
      }
    },
    resetForm() {
      // Reset the form fields
      this.job = {
        title: '',
        description: '',
        requiredSkills: '',
        minExperience: null,
        institutionTypes: [] ,
        educationLevels: []
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
        const response = await axios.get('http://localhost:9090/api/jobs/topApplication', {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });

        // Process each job in the response
        this.jobs = response.data.map(job => {
          // Handle cases where topMatchScore might be null or undefined
          const score = job.topMatchScore != null ? job.topMatchScore.toString() : null;

          // Extract and format the score
          const regex = /(\d+(\.\d+)?)/; // Matches a number with optional decimals
          const match = score ? score.match(regex) : null; // Only match if score is not null

          const formattedScore = match ? parseFloat(match[0]).toFixed(2) : 'N/A'; // Round to 2 decimal places

          return {
            ...job,
            topCvFileNames: Array.from(new Set(job.topCvFileNames.split(', '))).join(', '),
            topMatchScore: formattedScore // Set the formatted score
          };
        });

      } catch (error) {
        console.error('Error fetching jobs:', error);
        this.errorMessage = 'An error occurred while fetching jobs.'; // Set a general error message
      }


    },

    exportToExcel() {
      const authToken = localStorage.getItem('authToken');
      const url = 'http://localhost:9090/api/jobs/export';

      // Trigger the file download
      window.location.href = `${url}?authToken=${authToken}`;
    }
  },
  mounted() {
    this.fetchJobs();
  }
};
</script>
