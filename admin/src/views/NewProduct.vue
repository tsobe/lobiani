<template>
  <v-container>
    <v-row>
      <v-card class="ma-auto pa-5">
        <v-card-title>
          <p class="display-1 text--primary">Define new product</p>
        </v-card-title>

        <v-text-field data-slug label="slug" autofocus v-model="slug"></v-text-field>
        <v-text-field data-title label="title" v-model="title"></v-text-field>
        <v-textarea outlined data-description label="description" v-model="description"></v-textarea>

        <v-alert data-validation-message type="error" v-show="validationFailed">{{ validationErrorMessage }}</v-alert>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn data-cancel @click="cancel">Cancel</v-btn>
          <v-btn class="primary"
                 data-save
                 :disabled="!validInfoProvided"
                 @click="defineProduct">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
  import axios from 'axios'

  export default {
    data() {
      return {
        slug: '',
        title: '',
        description: '',
        validationErrorMessage: ''
      }
    },
    watch: {
      slug() {
        this.validationErrorMessage = this.hasValidSlugFormat()
          ? ''
          : 'Slug must consist of lowercase alpha-numeric and dash(\'-\') characters'
      }
    },
    computed: {
      validationFailed() {
        return this.validationErrorMessage.length > 0
      },
      infoProvided() {
        return this.slug && this.title.trim() && this.description.trim()
      },
      validInfoProvided() {
        return this.infoProvided && this.hasValidSlugFormat()
      }
    },
    methods: {
      hasValidSlugFormat() {
        return /^[a-z0-9-]+$/g.test(this.slug)
      },
      async defineProduct() {
        await axios.post('/products', {
          slug: this.slug,
          title: this.title,
          description: this.description
        })
        await this.$router.push('/products')
      },
      cancel() {
        this.$router.push('/products')
      }
    }
  }
</script>
