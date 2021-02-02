<template>
  <v-app>
    <v-navigation-drawer v-if="this.$auth.authenticated" app v-model="drawerVisible">
      <template v-slot:prepend>
        <profile></profile>
      </template>

      <template v-slot:append>
        <v-divider></v-divider>
        <logout></logout>
      </template>

      <v-divider></v-divider>

      <v-list>
        <v-list-item to="/inventory">
          <v-list-item-content>
            <v-list-item-title>Inventory</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/products">
          <v-list-item-content>
            <v-list-item-title>Products</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar v-if="this.$auth.authenticated" app>
      <v-app-bar-nav-icon v-on:click="toggleDrawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Admin</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
      <notification-holder></notification-holder>
    </v-main>

  </v-app>
</template>

<script>
  import NotificationHolder from '@/notifier/NotificationHolder'
  import Profile from '@/components/Profile'
  import Logout from '@/components/Logout'

  export default {
    name: 'App',
    components: {
      NotificationHolder,
      Profile,
      Logout
    },
    data() {
      return {drawerVisible: null}
    },
    methods: {
      toggleDrawer() {
        this.drawerVisible = !this.drawerVisible
      }
    }
  }
</script>
