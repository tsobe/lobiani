<template>
  <div class="item" v-bind:data-slug="item.slug">
    <span class="slug">{{ item.slug }}</span>
    <span class="stock-level">{{ item.stockLevel }}</span>
    <input name="amount" type="number" v-model.number="amount"/>
    <button class="add-to-stock" v-on:click="addToStock">Add to stock</button>
    <button class="delete" v-on:click="deleteItem">Delete</button>
  </div>
</template>

<script>
  export default {
    name: 'InventoryItem',
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        amount: null
      }
    },
    methods: {
      async addToStock() {
        await this.$store.dispatch('addToStock', {item: this.item, amount: this.amount})
        this.amount = null
      },
      async deleteItem() {
        await this.$store.dispatch('deleteItem', this.item)
      }
    }
  }
</script>
