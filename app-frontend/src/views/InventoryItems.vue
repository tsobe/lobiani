<template>
  <div class="items">
    <inventory-item v-for="(item, idx) in items"
                    v-bind:item="item"
                    v-bind:key="item.id"
                    v-on:itemDeleted="deleteItem(item, idx)"/>
  </div>
</template>

<script>
  import axios from 'axios'
  import InventoryItem from '@/components/InventoryItem'

  export default {
    name: 'InventoryItems',
    components: {InventoryItem},
    data() {
      return {
        items: []
      }
    },
    async mounted() {
      const response = await axios.get('/inventory-items')
      this.items = response.data
    },
    methods: {
      deleteItem(item, idx) {
        this.items.splice(idx, 1)
      }
    }
  }
</script>
