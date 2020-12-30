<template>
  <v-card class="ma-2" v-bind:data-item="item.slug">
    <v-card-title>
      <p class="display-1 text--primary" data-slug>{{ item.slug }}</p>
    </v-card-title>
    <v-card-text>
      <p>Stock level <span data-stock-level>{{ item.stockLevel }}</span></p>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-list>
        <v-list-item>
          <v-list-item-content>
            <v-text-field label="amount" data-amount type="number" v-model.number="amount"/>
            <v-btn class="ma-1" color="primary" data-add-to-stock :disabled="!hasValidAmount" v-on:click="addToStock">
              Add to stock
            </v-btn>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list-item>
          <v-list-item-content>
            <v-btn color="red" block data-delete v-on:click="deleteItem">
              Delete
            </v-btn>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-actions>
  </v-card>
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
    computed: {
      hasValidAmount() {
        return this.amount > 0
      }
    },
    methods: {
      async addToStock() {
        await this.$store.dispatch('inventory/addToStock', {
          item: this.item,
          amount: this.amount
        })
        this.amount = null
      },
      async deleteItem() {
        await this.$store.dispatch('inventory/deleteItem', this.item)
      }
    }
  }
</script>
