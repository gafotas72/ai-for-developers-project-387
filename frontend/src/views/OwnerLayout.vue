<template>
  <v-container fluid class="pa-4">
    <v-tabs v-model="activeTab" color="primary" align-tabs="justify" class="mb-4">
      <v-tab value="event-types">Типы событий</v-tab>
      <v-tab value="slots">Забронированные слоты</v-tab>
    </v-tabs>

    <v-tabs-window v-model="activeTab">
      <v-tabs-window-item value="event-types">
        <EventTypesPage ref="eventTypesRef" />
      </v-tabs-window-item>
      <v-tabs-window-item value="slots">
        <BookedSlotsPage ref="slotsRef" />
      </v-tabs-window-item>
    </v-tabs-window>
  </v-container>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import EventTypesPage from './owner/EventTypesPage.vue'
import BookedSlotsPage from './owner/BookedSlotsPage.vue'

const route = useRoute()
const router = useRouter()
const activeTab = ref('event-types')
const eventTypesRef = ref(null)
const slotsRef = ref(null)

watch(activeTab, (newTab) => {
  if (newTab === 'event-types' && eventTypesRef.value) {
    eventTypesRef.value.refresh()
  } else if (newTab === 'slots' && slotsRef.value) {
    slotsRef.value.refresh()
  }
  router.replace({ path: '/owner', query: { tab: newTab } })
})

watch(() => route.query.tab, (newTab) => {
  if (newTab === 'slots') {
    activeTab.value = 'slots'
  } else {
    activeTab.value = 'event-types'
  }
}, { immediate: true })
</script>
