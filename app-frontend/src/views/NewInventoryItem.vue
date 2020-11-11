<template>
  <div>
    <input type="text" class="slug" v-model="slug"/>
    <button class="add" v-on:click="defineItem">Define item</button>
  </div>
</template>

<script>

  export default {
    name: 'NewInventoryItem',
    data() {
      return {
        slug: null
      }
    },
    methods: {
      async defineItem() {
        const item = await this.$store.dispatch('defineItem', this.slug)
        this.slug = null
        this.$emit('itemDefined', item)
      }
    }
  }
</script>
