<template>
  <div>
    <input type="text" class="slug" v-model="slug"/>
    <button class="add" v-on:click="defineItem">Define item</button>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'DefineInventoryItem',
    data() {
      return {
        slug: null
      }
    },
    methods: {
      async defineItem() {
        const response = await axios.post('/api/inventory-items', {slug: this.slug})
        this.$emit('itemDefined', {
          id: response.data.id,
          slug: this.slug
        })
        this.slug = null
      }
    }
  }
</script>
