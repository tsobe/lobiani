<template>
  <v-container fill-height fluid>
    <v-row justify="center">
      <v-btn x-large rounded color="primary"
             @click="this.login"
             :loading="this.$auth.loading" data-login>
        Login
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    methods: {
      login() {
        this.$auth.login({
          redirect_uri: window.location.origin
        })
      }
    }
  }
</script>
