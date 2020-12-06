<template>
  <v-snackbar v-model="visible" :color="color" data-notification>{{ text }}</v-snackbar>
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
    created() {
      this.unsubscribe = this.$store.subscribe((mutation, state) => {
        if (mutation.type === 'notifier/setMessage') {
          this.visible = true
          this.text = state.notifier.text
          this.type = state.notifier.type
        }
      })
    },
    destroyed() {
      this.unsubscribe()
    },
    computed: {
      color() {
        return this.type === 'info' ? 'dark' : 'red'
      }
    }
  }
</script>
