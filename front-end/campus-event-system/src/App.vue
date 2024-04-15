<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
<!--  <header>-->
<!--    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />-->

<!--    <div class="wrapper">-->
<!--      <HelloWorld msg="You did it!" />-->

<!--      <nav>-->
<!--        <RouterLink to="/">Home</RouterLink>-->
<!--        <RouterLink to="/about">About</RouterLink>-->
<!--      </nav>-->
<!--    </div>-->
<!--  </header>-->

<!--  <div id="app">-->
<!--    <router-view/>-->
<!--  </div>-->
<router-view/>
</template>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  //color: #2c3e50;
  //margin-top: 10px;
  width: 100%;
  height:100%;
}


.boundary {
  border-style: dashed;
  //border-style: solid;
  border-color: #d3dce6;
  border-width: 0.5px;
  border-radius: 100px;
}
</style>
