<template>
  <div class="pa-2">
    <v-btn block dark :loading="this.$auth.loading" @click="this.logout" data-logout>Logout</v-btn>
  </div>
</template>

<script>
  export default {
    methods: {
      logout() {
        this.$auth.logout({
          returnTo: window.location.origin
        })
      }
    }
  }
</script>
