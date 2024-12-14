<!-- HomePage.vue -->
<template>
  <div>
    <center>  <h1 class="animated-title">Resume Analysis with AI Agent</h1></center>

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

.animated-title {
  font-size: 3rem; /* Larger font size for emphasis */
  font-weight: bold;
  color: #ccc; /* Slightly darker text color */
  text-shadow:
      1px 1px 2px rgba(0, 0, 0, 0.5),
      0 0 8px #1c6b8e,
      0 0 16px #145f78,
      0 0 32px #0e4f5d,
      0 0 64px #0a3f47; /* Darker glow colors */
  animation: pulse 2s infinite, moveText 6s infinite alternate;
  transform: perspective(1000px) rotateX(20deg);
}

@keyframes pulse {
  0%, 100% {
    text-shadow:
        1px 1px 2px rgba(0, 0, 0, 0.5),
        0 0 8px #1c6b8e,
        0 0 16px #145f78,
        0 0 32px #0e4f5d,
        0 0 64px #0a3f47;
  }
  50% {
    text-shadow:
        1px 1px 2px rgba(0, 0, 0, 0.5),
        0 0 16px #0e4f5d,
        0 0 32px #0a3f47,
        0 0 64px #073539;
  }
}

@keyframes moveText {
  0% {
    transform: perspective(1000px) rotateX(20deg) translateY(0);
  }
  100% {
    transform: perspective(1000px) rotateX(-20deg) translateY(-10px);
  }
}















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
