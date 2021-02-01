<template>
  <v-container>
    <v-row justify="start">
      <product v-for="product in products" :key="product.id" :product="product"></product>
    </v-row>
  </v-container>
</template>
<script>
  import Product from '@/components/Product'

  export default {
    components: {Product},
    async mounted() {
      await this.$store.dispatch('product/fetchProducts')
    },
    computed: {
      products() {
        return this.$store.state.product.products
      }
    }
  }
</script>
