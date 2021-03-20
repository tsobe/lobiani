<template>
  <div class="font-sans bg-blue-50 shadow overflow-hidden rounded-lg border p-3">
    <div class="flex items-center justify-between">
      <span class="text-3xl font-mono font-light uppercase" data-title>{{ product.title }}</span>
      <span class="font-bold uppercase" v-bind:class="stockSummary.class"
            data-stock-summary>{{ stockSummary.text }}</span>
    </div>
    <div class="flex items-center justify-between mt-2 pt-2 border-t border-gray">
      <span data-description class="font-thin flex-auto border-r m-3">{{ product.description }}</span>
      <p data-price class="flex-none">
        <span>{{ product.price.value }}</span>
        <span class="text-green-600 font-bold">{{ currencySign }}</span>
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
  computed: {
    stockSummary() {
      const stockLevel = this.product.stockLevel
      if (stockLevel === 0) {
        return {
          text: 'out of stock',
          class: 'text-red-600'
        }
      }
      if (stockLevel <= 10) {
        return {
          text: `only ${stockLevel} items left`,
          class: 'text-yellow-600'
        }
      }
      return {
        text: 'in stock',
        class: 'text-green-600'
      }
    },
    currencySign() {
      return '€'
    }
  }
}
</script>
