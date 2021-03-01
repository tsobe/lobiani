<template>
  <v-card class="ma-2" :data-product="product.slug">
    <v-card-title data-title>{{ product.title }}</v-card-title>
    <v-card-subtitle data-slug>{{ product.slug }}</v-card-subtitle>
    <v-card-text data-description>{{ product.description }}</v-card-text>

    <v-divider></v-divider>

    <v-card-text>
      <p>Price <span data-current-price>{{ currentPrice }}</span></p>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-list>
        <v-list-item>
          <v-list-item-content>
            <v-text-field label="price" data-price-to-assign type="number" v-model.number="priceToAssign"></v-text-field>
            <v-btn class="ma-1" color="primary"
                   data-assign-price
                   :disabled="!hasValidPrice"
                   @click="assignPrice">
              Assign Price
            </v-btn>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list-item>
          <v-list-item-content>
            <v-btn color="red" block data-delete @click="deleteProduct">
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
    props: {
      product: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        priceToAssign: null
      }
    },
    computed: {
      hasValidPrice() {
        return this.priceToAssign > 0 && this.priceToAssign !== this.product.price?.value
      },
      currentPrice() {
        const price = this.product.price
        return price ? `${price.value} ${price.currency}` : 'N/A'
      }
    },
    methods: {
      async assignPrice() {
        await this.$store.dispatch('product/assignPrice', {
          id: this.product.id,
          price: {
            value: this.priceToAssign,
            currency: 'EUR'
          }
        })
        this.priceToAssign = null
      },
      async deleteProduct() {
        await this.$store.dispatch('product/delete', this.product.id)
      }
    }
  }
</script>
