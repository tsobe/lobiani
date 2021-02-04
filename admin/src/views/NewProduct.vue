<template>
  <v-form v-model="valid">
    <v-container>
      <v-row>
        <v-card class="ma-auto pa-5">
          <v-card-title>
            <p class="display-1 text--primary">Define new product</p>
          </v-card-title>

          <slug v-model="slug"
                @checkingAvailability="checkingSlugAvailability = $event"
                resource="products">
          </slug>
          <v-text-field data-title
                        label="title"
                        v-model="title"
                        :rules="titleRules">
          </v-text-field>
          <v-textarea outlined
                      data-description
                      label="description"
                      v-model="description"
                      :rules="descriptionRules">
          </v-textarea>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn data-cancel @click="cancel">Cancel</v-btn>
            <v-btn class="primary"
                   data-save
                   :disabled="!valid"
                   :loading="checkingSlugAvailability"
                   @click="defineProduct">
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
    components: {Slug},
    data() {
      return {
        valid: false,
        slug: '',
        title: '',
        titleRules: [
          v => !!v.trim() || 'Title is required'
        ],
        description: '',
        descriptionRules: [
          v => !!v.trim() || 'Description is required'
        ],
        validationErrorMessage: '',
        checkingSlugAvailability: false
      }
    },
    methods: {
      async defineProduct() {
        await this.$store.dispatch('product/define', {
          slug: this.slug,
          title: this.title,
          description: this.description
        })
        await this.$router.push('/products')
      },
      cancel() {
        this.$router.go(-1)
      }
    }
  }
</script>
