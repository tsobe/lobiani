<template>
  <v-container>
    <v-row dense>
      <v-card class="ma-auto pa-5">
        <v-card-title>
          <p class="display-1 text--primary">Define new inventory item</p>
        </v-card-title>

        <v-text-field label="slug" autofocus data-slug v-model="slug"></v-text-field>

        <v-alert data-validation-message type="error" v-show="validationFailed">{{ validationMessage }}</v-alert>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn data-cancel @click="cancel">Cancel</v-btn>
          <v-btn class="primary" data-save justify="end"
                 :disabled="!validSlugProvided"
                 :loading="checkingSlugAvailability"
                 @click="defineItem">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
  import axios from 'axios'
  import _ from 'lodash'

  export default {
    name: 'NewInventoryItem',
    data() {
      return {
        slug: null,
        validationMessage: '',
        checkingSlugAvailability: false
      }
    },
    created() {
      this.debounceCheckSlugAvailability = _.debounce(this.checkSlugAvailability, 500)
    },
    watch: {
      slug(newSlug) {
        if (newSlug) {
          this.checkingSlugAvailability = true
          this.debounceCheckSlugAvailability()
        }
      }
    },
    computed: {
      validSlugProvided() {
        return !!this.slug && !this.validationFailed && !this.checkingSlugAvailability
      },
      validationFailed() {
        return this.validationMessage.length > 0
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
      },
      async checkSlugAvailability() {
        try {
          const response = await axios.get(`/inventory-items?slug=${this.slug}`)
          const isGivenSlugAvailable = response.data.length === 0
          this.validationMessage = isGivenSlugAvailable
            ? ''
            : 'An item with this slug is already defined'
        } catch (e) {
          this.validationMessage = 'Failed to check slug availability'
        } finally {
          this.checkingSlugAvailability = false
        }
      }
    }
  }
</script>
