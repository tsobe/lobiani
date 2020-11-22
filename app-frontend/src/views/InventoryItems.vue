<template>
  <div class="items">
    <inventory-item v-for="item in items"
                    v-bind:item="item"
                    v-bind:key="item.id"/>
  </div>
</template>

<script>
  import InventoryItem from '@/components/InventoryItem'

  export default {
    name: 'InventoryItems',
    components: {InventoryItem},
    async mounted() {
      if (!this.$store.getters.hasItems) {
        await this.$store.dispatch('fetchItems')
      }
    },
    computed: {
      items() {
        return this.$store.state.items
      }
    }
  }
</script>
