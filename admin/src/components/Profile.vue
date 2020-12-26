<template>
  <v-list-item two-line>
    <v-list-item-avatar>
      <img alt="Avatar" :src="this.$auth.user.picture">
    </v-list-item-avatar>

    <v-list-item-content>
      <v-list-item-title data-name>{{ this.$auth.user.name }}</v-list-item-title>
      <v-list-item-subtitle data-email>{{ this.$auth.user.email }}</v-list-item-subtitle>
    </v-list-item-content>
  </v-list-item>
</template>
