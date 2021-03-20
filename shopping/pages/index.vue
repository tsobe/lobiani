<template>
  <div class="container mx-auto p-10">
    <product class="mb-3" v-for="product in products" :key="product.slug" :product="product"/>
  </div>
</template>

<script>
export default {
  data() {
    return {
      products: []
    }
  },
  async fetch() {
    const response = await this.$axios.get('http://localhost:9090/shopping/api/products')
    this.products = response.data
  }
}
</script>
