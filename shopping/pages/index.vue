<template>
  <div class="container">
    <div v-for="product in products">
      <product :product="product"/>
    </div>
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
