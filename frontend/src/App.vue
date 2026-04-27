<template>
  <v-app>
    <v-app-bar color="primary" dark>
      <v-app-bar-title>Календарь звонков</v-app-bar-title>
      <template v-slot:append>
        <v-btn 
          :color="activeTab === 'owner' ? 'white' : 'rgba(255,255,255,0.7)'"
          :variant="activeTab === 'owner' ? 'flat' : 'text'"
          class="mr-2"
          @click="setTab('owner')"
        >
          Владелец
        </v-btn>
        <v-btn 
          :color="activeTab === 'guest' ? 'white' : 'rgba(255,255,255,0.7)'"
          :variant="activeTab === 'guest' ? 'flat' : 'text'"
          @click="setTab('guest')"
        >
          Гость
        </v-btn>
      </template>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const activeTab = ref('owner')

function setTab(tab) {
  if (tab === 'owner') {
    router.push('/owner')
  } else {
    router.push('/guest')
  }
}

watch(() => route.path, (path) => {
  if (path?.startsWith('/owner')) {
    activeTab.value = 'owner'
  } else if (path === '/guest') {
    activeTab.value = 'guest'
  }
}, { immediate: true })
</script>
