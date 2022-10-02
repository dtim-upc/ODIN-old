<template>
    <q-page class="flex flex-center">
      
      <q-card class="my-card">
      <q-card-section>
        <div  style="max-width: 500px">
          <p>Welcome {{authStore.getUserName}} </p>
          <q-list bordered separator>

            <q-item clickable v-ripple :active="active" @click="addDataSource = true">
              
              <q-item-section avatar>
                <q-icon name="o_file_upload" />
              </q-item-section>

              <q-item-section>
                <q-item-label>Upload a data source</q-item-label>
                <q-item-label caption>This will automatically define an schema</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable v-ripple :active="active" @click="addDataSource = true">
              
              <q-item-section avatar>
                <q-icon name="o_file_upload" />
              </q-item-section>

              <q-item-section>
                <q-item-label>Upload a second data source</q-item-label>
                <q-item-label caption>This will automatically define an schema</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable v-ripple :active="active">
              
              <q-item-section avatar>
                <q-icon name="o_merge" />
              </q-item-section>

              <q-item-section>
                <q-item-label>Integrate second data source with the project</q-item-label>
                <!-- <q-item-label caption>This will automatically define an schema</q-item-label> -->
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable v-ripple :active="active">
              
              <q-item-section avatar>
                <q-icon name="mdi-selection-search" />
              </q-item-section>

              <q-item-section>
                <q-item-label>Explore the integrated data</q-item-label>
                <q-item-label caption>Not available for this survey</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

           
          </q-list>
        </div>
      </q-card-section>
    </q-card>
   
    <FormNewDataSource v-model:show="addDataSource"></FormNewDataSource>

    </q-page>
  </template>
  
<script setup>
import { ref } from "vue";
import FormNewDataSource from "components/forms/FormNewDataSource.vue";

import { useAuthStore } from 'stores/auth.store.js'

const addDataSource = ref(false)
const active = ref(false)



const authStore = useAuthStore()

</script>
  
<style lang="scss">
</style>  