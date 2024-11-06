<!-- HomePage.vue -->
<template>
  <div>
    <center><h1> Resume Analysis with AI Agent </h1></center>

    <br><br> <br><br> <br><br> <br><br> <br><br> <br><br>
    <center><b><p>Please <button class="button-Login"><router-link to="/login">Login</router-link></button> | <button class="button-signup"><router-link to="/signup">Sign Up</router-link></button> to continue.</p></b> </center>

  </div>
</template>

<script>
export default {
  name: 'HomePage'
};
</script>





<style>




.button-Login{

    display: inline-block;
    padding: 10px 20px; /* Smaller padding for slightly smaller buttons */
    font-size: 25px; /* Adjusted font size */
    border: none;
    border-radius: 8px;
    color: #333;
    cursor: pointer;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

}
.button-Login:hover {
  background-color: #395ce6;
}




.button-Login:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}




.button-signup{

  display: inline-block;
  padding: 10px 20px; /* Smaller padding for slightly smaller buttons */
  font-size: 25px; /* Adjusted font size */
  border: none;
  border-radius: 8px;
  color: #333;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

}
.button-signup:hover {
  background-color: #395ce6;
}




.button-signup:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}














</style>
