
<template>
  <q-layout view="hHh lpR fFf">

    <q-header bordered class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          ODIN
        </q-toolbar-title>
        <q-space/> 
         <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round dense flat color="black" :icon="$q.fullscreen.isActive ? 'fullscreen_exit' : 'fullscreen'"
                 @click="$q.fullscreen.toggle()" v-if="$q.screen.gt.sm">
          </q-btn>
          <q-btn round flat>
            <q-avatar size="26px" @click="logout">
              <img src="https://cdn.quasar.dev/img/boy-avatar.png"/>
            </q-avatar>
          </q-btn>
        </div>

      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script setup>
import { useAuthStore } from 'stores/auth.store.js'


// const notify = useNotify()
const authStore = useAuthStore()

const logout = () => {
    authStore.logout()
}


</script>