<template>
  <div>
    <v-row class="mb-4" align="center">
      <v-col>
        <h2 class="text-h5">Типы событий</h2>
      </v-col>
      <v-col class="text-right">
        <v-btn color="primary" prepend-icon="mdi-plus" @click="openCreateDialog">
          Создать тип
        </v-btn>
      </v-col>
    </v-row>

    <v-data-table
      :headers="headers"
      :items="eventTypes"
      :loading="loading"
      :items-per-page="pageSize"
      class="elevation-1"
    >
      <template v-slot:item.actions="{ item }">
        <v-icon size="small" class="mr-2" @click="openEditDialog(item)">
          mdi-pencil
        </v-icon>
        <v-icon size="small" color="error" @click="confirmDelete(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>

    <v-pagination
      v-model="currentPage"
      :length="totalPages"
      :total-visible="6"
      class="mt-4"
      @update:model-value="loadEventTypes"
    />

    <v-dialog v-model="showDialog" max-width="500">
      <v-card>
        <v-card-title>
          {{ isEdit ? 'Редактировать тип события' : 'Создать тип события' }}
        </v-card-title>
        <v-card-text>
          <v-form ref="formRef" @submit.prevent="saveEventType">
            <v-text-field
              v-model="form.title"
              label="Название *"
              :rules="[rules.required, rules.minLength, rules.maxLength]"
              outlined
            />
            <v-textarea
              v-model="form.description"
              label="Описание"
              :rules="[rules.maxDescLength]"
              outlined
              rows="3"
            />
            <v-text-field
              v-model.number="form.duration"
              label="Длительность (минуты) *"
              type="number"
              :rules="[rules.required, rules.positive]"
              outlined
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="grey" variant="text" @click="closeDialog">Отмена</v-btn>
          <v-btn color="primary" :loading="saving" @click="saveEventType">
            {{ isEdit ? 'Сохранить' : 'Создать' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useApi } from '@/composables/useApi'
import { useUsers } from '@/composables/useApi'

const api = useApi()
const { owner, fetchUsers } = useUsers()

const eventTypes = ref([])
const loading = ref(false)
const saving = ref(false)
const showDialog = ref(false)
const isEdit = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const formRef = ref(null)

watch(owner, (newOwner) => {
  console.log('owner changed:', newOwner)
  if (newOwner?.id) {
    loadEventTypes()
  }
}, { immediate: true })

const totalPages = computed(() => Math.max(1, Math.ceil(totalCount.value / pageSize.value)))

const defaultForm = {
  id: null,
  title: '',
  description: '',
  duration: 30
}

const form = ref({ ...defaultForm })

const rules = {
  required: v => !!v || 'Обязательное поле',
  minLength: v => (v && v.length >= 3) || 'Минимум 3 символа',
  maxLength: v => (v && v.length <= 100) || 'Максимум 100 символов',
  maxDescLength: v => (!v || v.length <= 500) || 'Максимум 500 символов',
  positive: v => v > 0 || 'Должно быть больше 0'
}

const headers = [
  { title: 'ID', key: 'id', width: '60px', align: 'center' },
  { title: 'Название', key: 'title', align: 'left' },
  { title: 'Описание', key: 'description', align: 'left' },
  { title: 'Длительность (мин)', key: 'duration', align: 'center', width: '150px' },
  { title: 'Действия', key: 'actions', sortable: false, width: '100px', align: 'center' }
]

async function loadEventTypes() {
  console.log('loadEventTypes called, owner:', owner.value)
  if (!owner.value?.id) {
    console.warn('Owner not loaded yet, skipping loadEventTypes')
    return
  }
  
  loading.value = true
  try {
    const offset = (currentPage.value - 1) * pageSize.value
    const response = await api.list(owner.value.id, pageSize.value, offset)
    console.log('event-types response:', response)
    eventTypes.value = response.data
    totalCount.value = response.headers['x-total-count'] 
      ? parseInt(response.headers['x-total-count']) 
      : eventTypes.value.length
  } catch (error) {
    console.error('Failed to load event types:', error)
  } finally {
    loading.value = false
  }
}

function openCreateDialog() {
  isEdit.value = false
  form.value = { ...defaultForm }
  showDialog.value = true
}

function openEditDialog(eventType) {
  isEdit.value = true
  form.value = { ...eventType }
  showDialog.value = true
}

async function saveEventType() {
  if (!owner.value?.id) {
    console.error('Owner not loaded')
    return
  }
  
  const { valid } = await formRef.value.validate()
  if (!valid) return
  
  saving.value = true
  try {
    if (isEdit.value) {
      await api.update(form.value.id, {
        id: form.value.id,
        user_id: owner.value.id,
        title: form.value.title,
        description: form.value.description || '',
        duration: form.value.duration
      })
    } else {
      await api.create({
        id: 0,
        user_id: owner.value.id,
        title: form.value.title,
        description: form.value.description || '',
        duration: form.value.duration
      })
    }
    showDialog.value = false
    await loadEventTypes()
  } catch (error) {
    console.error(error)
  } finally {
    saving.value = false
  }
}

function closeDialog() {
  showDialog.value = false
  formRef.value?.resetValidation()
}

function confirmDelete(eventType) {
  if (confirm(`Удалить тип "${eventType.title}"?`)) {
    deleteEventType(eventType)
  }
}

async function deleteEventType(eventType) {
  if (!owner.value?.id) {
    console.error('Owner not loaded')
    return
  }
  
  try {
    await api._delete(eventType.id)
    await loadEventTypes()
  } catch (error) {
    console.error(error)
  }
}

function refresh() {
  loadEventTypes()
}

defineExpose({ refresh })

onMounted(async () => {
  console.log('EventTypesPage mounted')
  await fetchUsers()
  console.log('After fetchUsers, owner:', owner.value)
  await loadEventTypes()
})
</script>
