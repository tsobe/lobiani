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
        const opts = {}
        if (this.$route.query.targetUrl) {
          opts.appState = {
            targetUrl: this.$route.query.targetUrl
          }
        }
        this.$auth.login(opts)
      }
    }
  }
</script>
