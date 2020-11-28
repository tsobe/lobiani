<template>
  <v-app>
    <v-navigation-drawer app clipped v-model="drawerVisible">
      <v-list>
        <v-list-item to="/items">
          <v-list-item-content>
            <v-list-item-title>Inventory items</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app clipped-left>
      <v-app-bar-nav-icon v-on:click="toggleDrawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Inventory</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn data-new-item to="/new">
        Define new
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view v-on:itemDefined="navigateToItems" v-on:itemDefinitionCancelled="navigateToItems"></router-view>
      </v-container>
    </v-main>

  </v-app>
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {drawerVisible: false}
    },
    methods: {
      navigateToItems() {
        this.$router.push('/items')
      },
      toggleDrawer() {
        this.drawerVisible = !this.drawerVisible
      }
    }
  }
</script>
