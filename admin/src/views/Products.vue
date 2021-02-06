<template>
  <v-container>
    <v-row justify="start" data-products>
      <product v-for="product in products" :key="product.id" :product="product"></product>
    </v-row>
    <v-btn
      class="mx-2"
      fab
      dark
      fixed
      right
      bottom
      color="primary"
      data-new-product
      to="/products/new">
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>
  </v-container>
</template>

<script>
  import Product from '@/components/Product'

  export default {
    components: {Product},
    async mounted() {
      if (!this.$store.getters['product/hasProducts']) {
        await this.$store.dispatch('product/fetch')
      }
    },
    computed: {
      products() {
        return this.$store.state.product.products
      }
    }
  }
</script>
