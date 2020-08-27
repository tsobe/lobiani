<template>
  <div id="app">
    <div id="nav">
      <router-link to="/items">Inventory Items</router-link>
      |
      <router-link id="new-item" to="/new">Define New Item</router-link>
    </div>
    <router-view v-on:itemDefined="navigateToItems"/>
  </div>
</template>

<script>
  export default {
    name: 'App',
    methods: {
      navigateToItems() {
        this.$router.push('/items')
      }
    }
  }
</script>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  #nav {
    padding: 30px;
  }

  #nav a {
    font-weight: bold;
    color: #2c3e50;
  }

  #nav a.router-link-exact-active {
    color: #42b983;
  }
</style>
