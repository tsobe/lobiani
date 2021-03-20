<template>
  <div class="font-sans bg-blue-50 shadow overflow-hidden rounded-lg border p-3">
    <div class="flex items-center justify-between">
      <span class="text-3xl font-mono font-light uppercase" data-title>{{ product.title }}</span>
      <div>
        <span class="font-light uppercase">Left in stock:</span>
        <span data-stock-level>{{ product.stockLevel }}</span>
      </div>
    </div>
    <div class="flex items-center justify-between mt-2 pt-2 border-t border-gray">
      <span data-description class="font-thin flex-auto border-r m-3">{{ product.description }}</span>
      <p data-price class="flex-none">
        <span>{{ product.price.value }}</span>
        <span class="text-green-600 font-bold">{{ product.price.currency }}</span>
      </p>
    </div>
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
      price: {},
      stockLevel: 0
    }
  }
}
</script>
