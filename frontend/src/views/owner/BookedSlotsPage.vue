<template>
  <div>
    <v-row class="mb-4" align="center">
      <v-col>
        <h2 class="text-h5">Забронированные слоты</h2>
      </v-col>
      <v-col class="text-right">
        <v-btn color="primary" prepend-icon="mdi-refresh" variant="outlined" @click="refresh">
          Обновить
        </v-btn>
      </v-col>
    </v-row>

    <v-data-table
      :headers="headers"
      :items="sortedSlots"
      :loading="loading"
      :items-per-page="pageSize"
      class="elevation-1"
    >
      <template v-slot:item.start_time="{ item }">
        {{ formatDateTime(item.start_time) }}
      </template>
      <template v-slot:item.end_time="{ item }">
        {{ formatDateTime(item.end_time) }}
      </template>
    </v-data-table>

    <v-pagination
      v-model="currentPage"
      :length="totalPages"
      :total-visible="6"
      class="mt-4"
      @update:model-value="loadSlots"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useApi } from '@/composables/useApi'
import { useUsers } from '@/composables/useApi'

const api = useApi()
const { owner, fetchUsers } = useUsers()

const slots = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

const headers = [
  { title: 'ID', key: 'id', width: '60px', align: 'center', sortable: true },
  { title: 'Тип события', key: 'event_title', align: 'left', sortable: true },
  { title: 'Гость', key: 'guest_name', align: 'left', sortable: true },
  { title: 'Название', key: 'slot_title', align: 'left', sortable: true },
  { title: 'Начало', key: 'start_time', align: 'left', sortable: true },
  { title: 'Конец', key: 'end_time', align: 'left', sortable: true }
]

const sortedSlots = computed(() => {
  return [...slots.value].sort((a, b) => new Date(a.start_time) - new Date(b.start_time))
})

const totalPages = computed(() => Math.max(1, Math.ceil(totalCount.value / pageSize.value)))

function formatDateTime(isoString) {
  if (!isoString) return '-'
  const date = new Date(isoString)
  return date.toLocaleString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function getLocalDateTimeString(date = new Date()) {
  return date.toISOString()
}

async function loadSlots() {
  if (!owner.value?.id) return
  loading.value = true
  try {
    const now = getLocalDateTimeString(new Date())
    const offset = (currentPage.value - 1) * pageSize.value
    const response = await api.listByOwner(owner.value.id, now, pageSize.value, offset)
    slots.value = response.data
    totalCount.value = response.headers['x-total-count']
      ? parseInt(response.headers['x-total-count'])
      : slots.value.length
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function refresh() {
  currentPage.value = 1
  await loadSlots()
}

defineExpose({ refresh })

onMounted(async () => {
  await fetchUsers()
  if (owner.value?.id) {
    await loadSlots()
  }
})
</script>
