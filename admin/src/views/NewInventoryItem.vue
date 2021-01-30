<template>
  <v-form v-model="valid">
    <v-container>
      <v-row dense>
        <v-card class="ma-auto pa-5">
          <v-card-title>
            <p class="display-1 text--primary">Define new inventory item</p>
          </v-card-title>

          <slug v-model="slug"
                @checkingAvailability="checkingSlugAvailability = $event"
                resource="inventory-items">
          </slug>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn data-cancel @click="cancel">Cancel</v-btn>
            <v-btn class="primary"
                   data-save
                   justify="end"
                   :disabled="!valid"
                   :loading="checkingSlugAvailability"
                   @click="defineItem">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
  import Slug from '@/components/Slug'

  export default {
    name: 'NewInventoryItem',
    components: {Slug},
    data() {
      return {
        valid: false,
        slug: null,
        checkingSlugAvailability: false
      }
    },
    methods: {
      async defineItem() {
        const item = await this.$store.dispatch('inventory/defineItem', this.slug)
        this.slug = null
        this.$emit('itemDefined', item)
      },
      cancel() {
        this.$emit('itemDefinitionCancelled')
      }
    }
  }
</script>
