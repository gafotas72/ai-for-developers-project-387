import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import './assets/main.css'

import App from './App.vue'
import router from './router'

const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#1976D2',
          secondary: '#26A69A',
          accent: '#9C27B0',
          error: '#C10015',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FFC107'
        }
      }
    }
  },
  locale: {
    locale: 'ru',
    fallback: 'ru',
    messages: {
      ru: {
        noDataText: 'Нет данных',
        loading: 'Загрузка...',
        dataIterator: {
          loadingText: 'Загрузка',
          noResultsText: 'Не найдено',
          noDataText: 'Нет данных'
        },
        dataFooter: {
          itemsPerPageText: 'Строк на странице',
          itemsPerPageAll: 'Все',
          pageText: 'Страница',
          nextPage: 'Следующая страница',
          prevPage: 'Предыдущая страница',
          firstPage: 'Первая страница',
          lastPage: 'Последняя страница'
        },
        pagination: {
          ariaLabel: {
            root: 'Переход по страницам',
            previous: 'Предыдущая',
            next: 'Следующая',
            first: 'Первая страница',
            last: 'Последняя страница',
            page: 'Страница {0}',
            currentPage: 'Страница {0}'
          }
        },
        input: {
          clear: 'Очистить {0}'
        },
        datePicker: {
          itemsSelected: 'Выбрано: {0}',
          range: {
            title: 'Выберите даты',
            header: 'Введите даты'
          },
          title: 'Выберите дату',
          header: 'Введите дату',
          input: {
            placeholder: 'Введите дату'
          },
          ariaLabel: {
            previousMonth: 'Предыдущий месяц',
            nextMonth: 'Следующий месяц',
            selectYear: 'Выберите год',
            previousYear: 'Предыдущий год',
            nextYear: 'Следующий год',
            selectMonth: 'Выберите месяц',
            selectDate: '{0}',
            currentDate: 'Сегодня, {0}'
          }
        }
      }
    }
  }
})

const app = createApp(App)
app.use(vuetify)
app.use(router)
app.mount('#app')
