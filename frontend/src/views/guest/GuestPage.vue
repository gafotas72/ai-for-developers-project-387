<template>
  <div class="guest-page">
    <v-app-bar color="primary" dark density="compact">
      <v-app-bar-title>
        {{ selectedEventType ? selectedEventType.title : 'Выберите тип события' }}
      </v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid class="pa-4">
        <v-row no-gutters>
          <v-col cols="12" md="3" class="pr-md-4">
            <h2 class="text-h5 mb-4">Типы событий</h2>
            <v-list lines="two" class="border" style="max-height: 350px; overflow-y: auto;">
              <v-list-item
                v-for="eventType in eventTypes"
                :key="eventType.id"
                :active="selectedEventType?.id === eventType.id"
                @click="selectEventType(eventType)"
              >
                <v-list-item-title>{{ eventType.title }}</v-list-item-title>
                <v-list-item-subtitle>{{ eventType.duration }} мин</v-list-item-subtitle>
              </v-list-item>
            </v-list>
            <v-row align="center" class="mt-2">
              <v-col cols="6">
                <v-select
                  v-model="eventTypesPerPage"
                  :items="[5, 10, 15, 20]"
                  label="Строк"
                  density="compact"
                  variant="outlined"
                  hide-details
                  @update:model-value="loadEventTypes"
                />
              </v-col>
              <v-col cols="6">
                <v-pagination
                  v-model="eventTypesPage"
                  :length="eventTypesTotalPages"
                  :total-visible="5"
                  density="compact"
                  @update:model-value="loadEventTypes"
                />
              </v-col>
            </v-row>
          </v-col>

          <v-col cols="12" md="3" class="px-md-4">
            <h2 class="text-h5 mb-4">Календарь</h2>
            <v-date-picker
              v-model="selectedDate"
              :min="minDateFormatted"
              :max="maxDateFormatted"
              show-adjacent-months
              color="primary"
              hide-header
              @update:model-value="fetchSlots(true, true)"
            />
          </v-col>

          <v-col cols="12" md="6" class="pl-md-4">
            <h2 class="text-h5 mb-4">Слоты</h2>
            <v-alert v-if="!selectedEventType || !selectedDate" type="info" variant="tonal">
              Выберите тип события и дату
            </v-alert>
            <template v-else>
              <v-list lines="two" class="border" style="max-height: 350px; overflow-y: auto;">
                <v-list-item
                  v-for="slot in paginatedSlots"
                  :key="slot.start_time"
                >
                  <template v-slot:prepend>
                    <v-chip
                      :color="slot.state === 'Filled' ? 'error' : 'success'"
                      size="small"
                      class="mr-3"
                    >
                      {{ slot.state === 'Filled' ? 'Занят' : 'Свободен' }}
                    </v-chip>
                  </template>
                  <v-list-item-title>
                    {{ formatTime(slot.start_time) }} - {{ formatTime(slot.end_time) }}
                    <span v-if="slot.title" class="text-caption text-grey ml-2">({{ slot.title }})</span>
                  </v-list-item-title>
                  <template v-slot:append>
                    <v-btn
                      v-if="slot.state === 'Free'"
                      color="primary"
                      size="small"
                      variant="tonal"
                      @click="openBookDialog(slot)"
                    >
                      Забронировать
                    </v-btn>
                    <v-btn
                      v-if="slot.state === 'Filled' && slot.id > 0 && slot.guest_id === guest?.id"
                      color="error"
                      size="small"
                      variant="tonal"
                      @click="confirmCancel(slot)"
                    >
                      Отменить
                    </v-btn>
                  </template>
                </v-list-item>
              </v-list>
              <v-row align="center" class="mt-2">
                <v-col cols="6">
                  <v-select
                    v-model="slotsPerPage"
                    :items="[5, 10, 15, 20]"
                    label="Строк"
                    density="compact"
                    variant="outlined"
                    hide-details
                  />
                </v-col>
                <v-col cols="6">
                  <v-pagination
                    v-model="slotsPage"
                    :length="slotsTotalPages"
                    :total-visible="5"
                    density="compact"
                  />
                </v-col>
              </v-row>
            </template>
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <v-dialog v-model="showBookDialog" max-width="400">
      <v-card>
        <v-card-title>Забронировать слот</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="bookingTitle"
            label="Заголовок"
            :rules="[v => !!v || 'Введите заголовок']"
            outlined
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="grey" variant="text" @click="showBookDialog = false">Отмена</v-btn>
          <v-btn color="primary" @click="bookSlot">Забронировать</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, shallowRef, computed, watch, onMounted } from 'vue'
import { useApi } from '@/composables/useApi'
import { useUsers } from '@/composables/useApi'

const api = useApi()
const { guest, fetchUsers } = useUsers()

const eventTypes = ref([])
const slots = shallowRef([])
const selectedEventType = ref(null)
const selectedDate = ref(null)
const loading = ref(false)
const showBookDialog = ref(false)
const bookingTitle = ref('')
const currentSlot = ref(null)

const eventTypesPage = ref(1)
const eventTypesPerPage = ref(10)
const eventTypesTotal = ref(0)

const slotsPage = ref(1)
const slotsPerPage = ref(10)

const eventTypesTotalPages = computed(() => Math.ceil(eventTypesTotal.value / eventTypesPerPage.value))
const slotsTotalPages = computed(() => Math.ceil(slots.value.length / slotsPerPage.value))

const paginatedSlots = computed(() => {
  const start = (slotsPage.value - 1) * slotsPerPage.value
  const end = start + slotsPerPage.value
  return slots.value.slice(start, end).slice()
})

function getLocalDateString(date = new Date()) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const today = new Date()
const minDateFormatted = getLocalDateString(today)
const maxDateFormatted = computed(() => {
  const max = new Date(today)
  max.setDate(today.getDate() + 14)
  return getLocalDateString(max)
})

function selectEventType(eventType) {
  selectedEventType.value = eventType
  selectedDate.value = getLocalDateString(today)
  fetchSlots(true)
}

function formatTime(isoString) {
  if (!isoString) return ''
  const date = new Date(isoString)
  return date.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })
}

async function loadEventTypes() {
  loading.value = true
  try {
    const offset = (eventTypesPage.value - 1) * eventTypesPerPage.value
    const response = await api.list(null, eventTypesPerPage.value, offset)
    eventTypes.value = response.data
    eventTypesTotal.value = response.headers['x-total-count']
      ? parseInt(response.headers['x-total-count'])
      : eventTypes.value.length
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function fetchSlots(shouldResetPage = false, shouldClear = false) {
  if (!selectedEventType.value || !selectedDate.value) return
  const currentPage = slotsPage.value
  if (shouldClear) {
    slots.value = []
  }
  loading.value = true
  try {
    const dateStr = typeof selectedDate.value === 'string'
      ? selectedDate.value
      : getLocalDateString(selectedDate.value)
    const response = await api.listByEventType(selectedEventType.value.id, dateStr)
    const newSlots = response.data.map(slot => ({ ...slot }))
    slots.value = newSlots
    if (shouldResetPage) {
      slotsPage.value = 1
    } else {
      const maxPage = Math.ceil(newSlots.length / slotsPerPage.value)
      if (currentPage > maxPage && maxPage > 0) {
        slotsPage.value = maxPage
      }
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

function openBookDialog(slot) {
  currentSlot.value = slot
  bookingTitle.value = ''
  showBookDialog.value = true
}

async function bookSlot() {
  if (!bookingTitle.value.trim()) {
    alert('Введите заголовок')
    return
  }
  try {
    const updatedSlot = {
      id: currentSlot.value.id,
      event_type_id: currentSlot.value.event_type_id,
      guest_id: guest.value?.id || 1,
      title: bookingTitle.value,
      start_time: currentSlot.value.start_time,
      end_time: currentSlot.value.end_time,
      state: 'Filled'
    }
    await api.update_2(currentSlot.value.id, updatedSlot)
    showBookDialog.value = false
    await fetchSlots(false, true)
  } catch (error) {
    console.error(error)
  }
}

async function confirmCancel(slot) {
  if (confirm('Отменить бронирование?')) {
    await cancelSlot(slot)
  }
}

async function cancelSlot(slot) {
  try {
    const updatedSlot = {
      id: slot.id,
      event_type_id: slot.event_type_id,
      guest_id: 0,
      title: slot.title,
      start_time: slot.start_time,
      end_time: slot.end_time,
      state: 'Free'
    }
    await api.update_2(slot.id, updatedSlot)
    await fetchSlots(false, true)
  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  await fetchUsers()
  loadEventTypes()
})
</script>

<style scoped>
.guest-page {
  margin: -16px;
  min-height: calc(100vh - 64px);
}
</style>
