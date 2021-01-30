<template>
  <v-form v-model="valid">
    <v-container>
      <v-row dense>
        <v-card class="ma-auto pa-5">
          <v-card-title>
            <p class="display-1 text--primary">Define new inventory item</p>
          </v-card-title>

          <v-text-field label="slug"
                        autofocus
                        data-slug
                        v-model="slug"
                        :rules="slugRules"
                        :error-messages="validationMessage"
          ></v-text-field>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn data-cancel @click="cancel">Cancel</v-btn>
            <v-btn class="primary" data-save justify="end"
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
  import axios from 'axios'
  import _ from 'lodash'

  export default {
    name: 'NewInventoryItem',
    data() {
      return {
        valid: false,
        slug: null,
        validationMessage: '',
        checkingSlugAvailability: false,
        slugRules: [
          v => !!v || 'Slug is required',
          v => this.hasValidSlugFormat(v) || 'Slug must consist of lowercase alpha-numeric and dash(\'-\') characters'
        ]
      }
    },
    created() {
      this.debounceCheckSlugAvailability = _.debounce(this.checkSlugAvailability, 500)
    },
    watch: {
      slug(newSlug) {
        if (newSlug) {
          if (!this.hasValidSlugFormat(newSlug)) {
            this.debounceCheckSlugAvailability.cancel()
            this.checkingSlugAvailability = false
          } else {
            this.checkingSlugAvailability = true
            this.debounceCheckSlugAvailability()
          }
        }
      }
    },
    methods: {
      hasValidSlugFormat(slug) {
        return /^[a-z0-9-]+$/g.test(slug)
      },
      async defineItem() {
        const item = await this.$store.dispatch('inventory/defineItem', this.slug)
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
