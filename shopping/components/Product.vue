<template>
  <div :data-product="product.slug" class="font-sans bg-blue-50 shadow overflow-hidden rounded-lg border p-3">
    <div class="sm:flex items-center justify-between">
      <div class="text-3xl font-mono font-light uppercase" data-title>{{ product.title }}</div>
      <div class="font-bold uppercase" v-bind:class="stockSummary.class"
           data-stock-summary>{{ stockSummary.text }}
      </div>
    </div>
    <div class="sm:flex items-center justify-between mt-2 pt-2 border-t">
      <div data-description class="font-thin flex-auto sm:border-r m-3">{{ product.description }}</div>
      <div data-price class="m-3 border-t sm:border-0 sm:m-0 sm:flex-none">
        <span>{{ product.price.value }}</span>
        <span class="text-green-600 font-bold">{{ currencySign }}</span>
      </div>
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
            text: stockLevel === 1
              ? `only ${stockLevel} item left`
              : `only ${stockLevel} items left`,
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
