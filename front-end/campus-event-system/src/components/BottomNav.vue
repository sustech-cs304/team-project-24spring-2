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
      <p><a-tag color="#ED6C00" :style="{ scale: 1.2 }">{{ "Best-effort-team" }}</a-tag></p>
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

  height: 100px;
  width: auto;
  margin-bottom: 5px;
}

.footer-info p {
  font-size: 18px; /* Adjust font size */
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

</style>
