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
  import axios from 'axios'

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
        await axios.post(`/inventory-items/${this.item.id}/stock`, {amount: this.amount})
        this.item.stockLevel += this.amount
        this.amount = null
      },
      async deleteItem() {
        await axios.delete(`/inventory-items/${this.item.id}`)
        this.$emit('itemDeleted', this.item)
      }
    }
  }
</script>
