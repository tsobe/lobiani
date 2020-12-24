<template>
    <div v-show="!this.$auth.loading">
      <v-btn v-if="!this.$auth.authenticated" @click="login" color="primary" data-login>Login</v-btn>
      <div v-if="this.$auth.authenticated" data-profile>
        <v-btn @click="logout" color="error" data-logout>Logout</v-btn>
      </div>
    </div>
</template>

<script>
  export default {
    methods: {
      async login() {
        await this.$auth.login()
      },
      async logout() {
        await this.$auth.logout({returnTo: window.location.origin})
      }
    }
  }
</script>
