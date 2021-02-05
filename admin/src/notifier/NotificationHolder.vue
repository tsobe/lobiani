<template>
  <v-snackbar v-model="visible" :color="color" data-notification>
    {{ text }}
    <template v-slot:action="{ attrs }">
      <v-btn
        text
        v-bind="attrs"
        data-dismiss
        @click="dismiss">
        Dismiss
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
  export default {
    name: 'NotificationHolder',
    data() {
      return {
        text: '',
        visible: false
      }
    },
    methods: {
      show(message) {
        this.visible = true
        this.text = message.text
        this.type = message.type
      },
      dismiss() {
        this.visible = false
      }
    },
    computed: {
      color() {
        return this.type === 'info' ? 'dark' : 'red'
      }
    },
    created() {
      this.unsubscribe = this.$store.subscribe((mutation, state) => {
        if (mutation.type === 'notifier/setMessage') {
          this.show(state.notifier)
        }
      })
    },
    destroyed() {
      this.unsubscribe()
    }
  }
</script>
