<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <dashboard></dashboard>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import Dashboard from './views/index.vue';

export default {
  name: 'App',
  components: {
    Dashboard,
  },
};
</script>

<style lang="stylus">
.collapse-item-header .el-collapse-item__header
  background #FEA680
  border-radius 3px

.collapse-item-header .el-icon-arrow-right
  margin 0 8px 0 4px

  &:before
    font-weight 900

.collapse-title
  margin-left 20px
  display inline-block
  overflow hidden
  text-overflow ellipsis
  white-space nowrap
</style>
