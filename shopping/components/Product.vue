<template>
  <div>
    <h1 data-title>{{ product.title }}</h1>
    <p data-description>{{ product.description }}</p>
    <p data-price>{{ product.price.value + ' ' + product.price.currency }}</p>
    <p data-stock-level>{{ product.stockLevel }}</p>
  </div>
</template>

<script>
  export default {
    props: {
      product: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        title: '',
        description: '',
        price: {

        },
        stockLevel: 0
      }
    }
  }
</script>
