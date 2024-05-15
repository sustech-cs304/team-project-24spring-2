<script>
export default {
  name: 'BottomNav',
}
</script>

<template>
  <div class="container">
    <div class="logo">
      <img src="../assets/bottomLOGO.png" alt="University Logo"/>
    </div>
    <div class="footer-info">
      <p>Develop Team: Best-effort-team</p>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column; /* Changed to column to stack items vertically */
  align-items: center;
  text-align: center;
}

.logo img {

  height: 150px;
  width: auto;
  margin-bottom: 5px;
}

.footer-info p {
  font-size: 18px; /* Adjust font size */
}

</style>
