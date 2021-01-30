<template>
  <v-text-field label="slug"
                autofocus
                data-slug
                :value="value"
                :rules="slugRules"
                :error-messages="validationMessage"
                @input="$emit('input', $event)">
  </v-text-field>
</template>

<script>
  import _ from 'lodash'
  import axios from 'axios'

  export default {
    props: ['value', 'resource'],
    data() {
      return {
        checkingAvailability: false,
        validationMessage: '',
        slugRules: [
          v => !!v || 'Slug is required',
          v => this.isValidFormat(v) || 'Slug must consist of lowercase alpha-numeric and dash(\'-\') characters'
        ]
      }
    },
    created() {
      this.debounceCheckAvailability = _.debounce(this.checkAvailability, 500)
    },
    watch: {
      value(newValue) {
        if (newValue) {
          if (!this.isValidFormat(newValue)) {
            this.debounceCheckAvailability.cancel()
            this.validationMessage = ''
            this.emitCheckingAvailability(false)
          } else {
            this.emitCheckingAvailability(true)
            this.debounceCheckAvailability()
          }
        }
      }
    },
    methods: {
      emitCheckingAvailability(status) {
        this.$emit('checkingAvailability', status)
      },
      isValidFormat(value) {
        return /^[a-z0-9-]+$/g.test(value)
      },
      async checkAvailability() {
        try {
          const response = await axios.get(`/${this.resource}?slug=${this.value}`)
          const isAvailable = response.data.length === 0
          this.validationMessage = isAvailable
            ? ''
            : 'An item with this slug is already defined'
        } catch (e) {
          this.validationMessage = 'Failed to check slug availability'
        } finally {
          this.emitCheckingAvailability(false)
        }
      }
    }
  }
</script>
