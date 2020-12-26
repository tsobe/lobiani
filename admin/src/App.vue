<template>
  <v-app>
    <v-navigation-drawer v-if="this.$auth.authenticated" app clipped v-model="drawerVisible">
      <v-list>
        <v-list-item to="/items">
          <v-list-item-content>
            <v-list-item-title>Inventory items</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar v-if="this.$auth.authenticated" app clipped-left>
      <v-app-bar-nav-icon v-on:click="toggleDrawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Inventory</v-toolbar-title>
      <v-spacer></v-spacer>
      <profile></profile>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view v-on:itemDefined="navigateToItems" v-on:itemDefinitionCancelled="navigateToItems"></router-view>
      </v-container>
    </v-main>

    <notification-holder></notification-holder>
  </v-app>
</template>

<script>
  import NotificationHolder from '@/components/NotificationHolder'
  import Profile from '@/components/Profile'

  export default {
    name: 'App',
    components: {
      Profile,
      NotificationHolder
    },
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
