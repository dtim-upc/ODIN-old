<template>
    <q-page class="flex flex-center">
      <home_pattern style="top:10;right:0;position:absolute;margin-right: 10px;width:500px;"></home_pattern>
      <q-card class="my-card q-pa-md" style="min-width: 500px" >
      <q-card-section>
        <div  >
          <h6 style="margin:10px">Welcome {{authStore.getUserName}} </h6>
          <q-list bordered separator>

            <q-item clickable style="padding:12px" v-ripple :active="active" @click="addDataSource = true">
              
              <q-item-section avatar>
                <!-- check_circle -->
                <q-icon :name="storeDS.getDatasourcesNumber >0? 'check_circle':'o_file_upload'" :color="storeDS.getDatasourcesNumber >0? 'green':null"  />
              </q-item-section>

              <q-item-section>
                <q-item-label>Upload a data source</q-item-label>
                <q-item-label caption>This will automatically define an schema</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable style="padding:12px" v-ripple :active="active" @click="addDataSource = true">
              
              <q-item-section avatar>
                <q-icon :name="storeDS.getDatasourcesNumber > 1 || (integrationStore.getDatasourcesNumber>0 && storeDS.getDatasourcesNumber >0) ? 'check_circle':'o_file_upload'" :color="storeDS.getDatasourcesNumber > 1 || (integrationStore.getDatasourcesNumber>0 && storeDS.getDatasourcesNumber >0)? 'green':null"   />
              </q-item-section>

              <q-item-section>
                <q-item-label>Upload a second data source</q-item-label>
                <q-item-label caption>This will automatically define an schema</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable style="padding:12px" v-ripple :active="active">
              
              <q-item-section avatar>
                <q-icon :name="storeDS.getDatasourcesNumber > 1? 'check_circle':'o_merge'" :color="storeDS.getDatasourcesNumber > 1? 'green':null"  />
              </q-item-section>

              <q-item-section>
                <q-item-label>Integrate second data source with the project</q-item-label>
                <!-- <q-item-label caption>This will automatically define an schema</q-item-label> -->
              </q-item-section>

              <q-item-section side>
                <q-icon name="o_navigate_next" color="green" />
              </q-item-section>

            </q-item>

            <q-item clickable style="padding:12px" v-ripple :active="active">
              
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
import { ref, onBeforeMount } from "vue";
import FormNewDataSource from "components/forms/FormNewDataSource.vue";
import { useDataSourceStore } from 'src/stores/datasources.store.js'
import { useIntegrationStore } from 'src/stores/integration.store.js'
import home_pattern from "components/icons/home_pattern.vue";

import { useAuthStore } from 'stores/auth.store.js'

const addDataSource = ref(false)
const active = ref(false)



const authStore = useAuthStore()

const storeDS = useDataSourceStore();
const integrationStore = useIntegrationStore()

onBeforeMount( () => {
    storeDS.setProject()
    integrationStore.setProject()
})


</script>
  
<style lang="scss">
</style>  