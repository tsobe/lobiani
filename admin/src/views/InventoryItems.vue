<template>
  <v-container>
    <v-row justify="start" data-items>
      <inventory-item v-for="item in items"
                      v-bind:key="item.id" v-bind:item="item"/>
    </v-row>
    <v-btn
      class="mx-2"
      fab
      dark
      fixed
      right
      bottom
      color="primary"
      data-new-item
      to="/new">
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>
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
