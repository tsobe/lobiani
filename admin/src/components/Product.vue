<template>
  <v-card :data-product="product.slug">
    <v-card-title data-title>{{ product.title }}</v-card-title>
    <v-card-subtitle data-slug>{{ product.slug }}</v-card-subtitle>
    <v-card-text data-description>{{ product.description }}</v-card-text>
    <v-card-actions>
      <v-btn color="red" block data-delete @click="deleteProduct">
        Delete
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
  export default {
    props: {
      product: {
        type: Object,
        required: true
      }
    },
    methods: {
      async deleteProduct() {
        await this.$store.dispatch('product/delete', this.product.id)
      }
    }
  }
</script>
