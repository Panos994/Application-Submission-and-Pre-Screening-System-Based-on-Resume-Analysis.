<template>
  <div class="info">
    <h1>Info</h1>
    <br><br>
    <p>
      <b>This distributed system has 3 roles. The roles are the ADMIN, the USER and the EMPLOYEE. </b>
<br><br>
      USER: A regular user can be a candidate interested in checking in which job positions his CV scores better.
      1) He should sign up and then login with his credentials.
      2) Then, he can press the Available job positions to check his preferences
      3)Moreover, after his search he can choose manually the job in the main page of the USER and browse his CV and check his score.
      4) At last, before that he can also check his CV with AI agent to get suggestions of the top 3 job that he can apply to get a good score.

      <br><br><br>
     EMPLOYEE: An employee user is assigned as an employee from an ADMIN.
      1) He can create new Job postings and hit save.
      2) He can check the available highest scores of the current jobs from a candidate that has sent his CV.
      3) He can also search for a candidate if he wants to contact him because of his good score.
      4) This can be done by checking the name of the CV in the Job List and then open the page of the CV repositories.
      He searches the CVs in the CV repositories page from the search input and he can download them.

      <br><br><br>
      ADMIN: An admin can delete a user. Moreover, an admin can delete or add (assignment of roles) a role/roles.


    </p>
  </div>
</template>

<script>
export default {
  name: 'InfoPage',
};
</script>

<style scoped>
.more-page {
  padding: 2rem;
}
</style>