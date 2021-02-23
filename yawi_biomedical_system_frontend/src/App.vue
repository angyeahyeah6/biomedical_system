<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <dashboard></dashboard>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import Dashboard from "./views/index.vue";

export default {
  name: "App",
  components: {
    Dashboard,
  },
};
</script>

<style lang="stylus"></style>
