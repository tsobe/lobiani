<template>
  <v-container>
    <v-row justify="start" data-items>
      <inventory-item v-for="item in items"
                      v-bind:key="item.id" v-bind:item="item"/>
    </v-row>
  </v-container>
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
