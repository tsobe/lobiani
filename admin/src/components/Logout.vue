<template>
  <div class="pa-2">
    <v-btn block dark :loading="this.$auth.loading" @click="this.$auth.logout" data-logout>Logout</v-btn>
  </div>
</template>
