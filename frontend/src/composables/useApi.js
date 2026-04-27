import { ref, readonly } from 'vue'
import { DefaultApi, Configuration } from '@/api/generated'
import config from '@/config'

const apiInstance = ref(null)

export function useApi() {
  if (!apiInstance.value) {
    const configuration = new Configuration({
      basePath: config.apiUrl
    })
    apiInstance.value = new DefaultApi(configuration)
  }
  return apiInstance.value
}

export function useUsers() {
  const api = useApi()
  const owner = ref(null)
  const guest = ref(null)
  const loading = ref(false)
  const error = ref(null)

  async function fetchUsers() {
    loading.value = true
    error.value = null
    try {
      const [ownerRes, guestRes] = await Promise.all([
        api.getOwner(),
        api.getGuest()
      ])
      console.log('getOwner response:', ownerRes)
      console.log('getGuest response:', guestRes)
      owner.value = ownerRes.data
      guest.value = guestRes.data
    } catch (e) {
      error.value = e
      console.error('Failed to fetch users:', e)
    } finally {
      loading.value = false
    }
  }

  return {
    owner: readonly(owner),
    guest: readonly(guest),
    loading: readonly(loading),
    error: readonly(error),
    fetchUsers
  }
}
