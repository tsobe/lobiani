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
        if (this.$route.query.next) {
          opts.appState = {
            next: this.$route.query.next
          }
        }
        this.$auth.login(opts)
      }
    }
  }
</script>
