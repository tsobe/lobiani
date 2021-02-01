<template>
  <v-card>
    <v-card-title data-title>{{ product.title }}</v-card-title>
    <v-card-subtitle data-slug>{{ product.slug }}</v-card-subtitle>
    <v-card-text data-description>{{ product.description }}</v-card-text>
  </v-card>
</template>

<script>
  export default {
    props: {
      product: {
        type: Object,
        required: true
      }
    }
  }
</script>
