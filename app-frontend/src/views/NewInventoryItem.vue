<template>
  <v-container>
    <v-row dense>
      <v-card class="ma-auto pa-5">
        <v-card-title>
          <p class="display-1 text--primary">Define new inventory item</p>
        </v-card-title>

        <v-text-field label="slug" data-slug v-model="slug"></v-text-field>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn data-cancel v-on:click="cancel">Cancel</v-btn>
          <v-btn class="primary" data-save justify="end" :disabled="!hasValidSlug" v-on:click="defineItem">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'NewInventoryItem',
    data() {
      return {
        slug: null
      }
    },
    computed: {
      hasValidSlug() {
        return !!this.slug
      }
    },
    methods: {
      async defineItem() {
        const item = await this.$store.dispatch('defineItem', this.slug)
        this.slug = null
        this.$emit('itemDefined', item)
      },
      cancel() {
        this.$emit('itemDefinitionCancelled')
      }
    }
  }
</script>
