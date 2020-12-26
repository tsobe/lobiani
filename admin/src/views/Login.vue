<template>
  <v-container fill-height fluid>
    <v-row justify="center">
      <v-btn x-large rounded color="primary"
             @click="this.$auth.login"
             :loading="this.$auth.loading" data-login>
        Login
      </v-btn>
    </v-row>
  </v-container>
</template>
