<template>
    <div class="q-pa-md">
        <!-- style="min-height: 70vh;margin-top:15px" -->
<!-- @selection="validateSelection2" -->
        <q-table :grid="gridEnable" ref="tableRef" :rows="storeDS.datasources" :columns="columns" :filter="search" 
            :class="{ 'no-shadow': no_shadow }"  row-key="id" 
            no-data-label="I didn't find anything for you. Consider creating a new data source."
            no-results-label="The filter didn't uncover any results" :visible-columns="visibleColumns" >

            <template v-slot:top-left="">
                <div class="q-table__title">
                    {{ title }}
                    <q-btn unelevated v-if="view === 'datasources'" padding="none" color="primary700" icon="add"
                        @click="addDataSource = true" />
                </div>

            </template>
<!-- storeDS.selected.filter(v => (v.graphicalGraph || v.type == "INTEGRATED")).length != 2 -->
            <template v-slot:top-right="props">
                <q-btn v-if="!integrationStore.isDSEmpty" outline
                    color="primary" label="Finish pending sources" class="q-mr-xs"
                    :to="{ name: 'dsIntegration' }">
                    <q-badge color="orange" floating>{{integrationStore.datasources.length}}</q-badge>    
                </q-btn>

                <q-btn label="Integrated schema" dense color="primary" icon="download" @click="storeDS.downloadProjectS" style="margin-right:10px"></q-btn>

                <q-input outlined dense debounce="400" color="primary" v-model="search">
                    <template v-slot:append>
                        <q-icon name="search" />
                    </template>
                </q-input>


                <q-btn flat round dense :icon="props.inFullscreen ? 'fullscreen_exit' : 'fullscreen'"
                    @click="props.toggleFullscreen">
                    <q-tooltip :disable="$q.platform.is.mobile" v-close-popup>
                        {{ props.inFullscreen ? "Exit Fullscreen" : "Toggle Fullscreen" }}
                    </q-tooltip>
                </q-btn>
            </template>


            <template v-slot:body-cell-status="props">
                <q-td :props="props">
                    <q-chip text-color="white" color="accent" v-if="props.row.graphicalGraph === ''">
                        Missing Data Sources
                    </q-chip>
                    <q-chip text-color="white" color="blue" v-else> Completed</q-chip>
                </q-td>
            </template>

            <template v-slot:body-cell-View_triples="props">
                <q-td :props="props">
                    <q-btn dense round flat color="grey" 
                        icon="mdi-graphql" :to="{name: 'viewTriples', params: {datasourceID: props.row.id}}"></q-btn>
                </q-td>
            </template>

            <template v-slot:body-cell-View_Source_Graph="props">
                <q-td :props="props">
                    <q-btn dense round flat color="grey" 
                        icon="download" @click="storeDS.downloadSource(props.row.id)"></q-btn>

                    <!-- <q-btn v-if="props.row.type == 'INTEGRATED'" dense round flat color="grey"
                        :to="{ name: 'webvowl', params: { id: props.row.id, minimalI: true } }"
                        icon="mdi-vector-circle-variant"></q-btn>
                    <q-btn v-if="props.row.type == 'INTEGRATED'" dense round flat color="grey"
                        :to="{ name: 'webvowl', params: { id: props.row.id, integrated: true } }"
                        icon="mdi-shape-circle-plus"></q-btn> -->

                    <!--          :disable="props.row.graphicalGraph"-->
                </q-td>
            </template>

            <template v-if="view === 'datasources'" v-slot:body-cell-actions="props">
                <q-td :props="props">
                    <!-- <q-btn dense round flat color="grey" :to="'/dataSources/view/' + props.row.id" -->
                        <!-- icon="remove_red_eye"></q-btn> -->
                    <!-- <q-btn dense round flat color="grey" @click="editRow(props)" icon="edit"></q-btn> -->
                    <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>
                </q-td>
            </template>

            <template v-slot:no-data="{ icon, message, filter }">
                <div class="full-width row flex-center text-accent q-gutter-sm q-pa-xl" style="flex-direction: column">
                    <svg width="160px" height="88px" viewBox="0 0 216 120" fill="none" xmlns="http://www.w3.org/2000/svg" class="sc-jIkXHa sc-ZOtfp fXAzWm jPTZgW"><g opacity="0.84" clip-path="url(#EmptyDocuments_svg__clip0_1142_57509)"><path fill-rule="evenodd" clip-rule="evenodd" d="M189.25 19.646a7.583 7.583 0 010 15.166h-43.333a7.583 7.583 0 010 15.167h23.833a7.583 7.583 0 010 15.167h-11.022c-5.28 0-9.561 3.395-9.561 7.583 0 1.956 1.063 3.782 3.19 5.48 2.017 1.608 4.824 1.817 7.064 3.096a7.583 7.583 0 01-3.754 14.174H65.75a7.583 7.583 0 010-15.166H23.5a7.583 7.583 0 110-15.167h43.333a7.583 7.583 0 100-15.167H39.75a7.583 7.583 0 110-15.166h43.333a7.583 7.583 0 010-15.167H189.25zm0 30.333a7.583 7.583 0 110 15.166 7.583 7.583 0 010-15.166z" fill="#D9D8FF" fill-opacity="0.8"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M132.561 19.646l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162zM73.162 26.33l4.97-.557-4.97.557z" fill="#fff"></path><path d="M73.162 26.33l4.97-.557m54.429-6.127l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162z" stroke="#7B79FF" stroke-width="2.5"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M129.818 24.27l9.122 66.608.82 6.682c.264 2.153-1.246 4.11-3.373 4.371l-56.812 6.976c-2.127.261-4.066-1.272-4.33-3.425l-8.83-71.908a2.167 2.167 0 011.887-2.415l7.028-.863" fill="#F0F0FF"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M135.331 5.833H85.978a2.97 2.97 0 00-2.107.873A2.97 2.97 0 0083 8.813v82.333c0 .823.333 1.567.872 2.106a2.97 2.97 0 002.107.873h63.917a2.97 2.97 0 002.106-.873 2.97 2.97 0 00.873-2.106V23.367a2.98 2.98 0 00-.873-2.107L137.437 6.705a2.98 2.98 0 00-2.106-.872z" fill="#fff" stroke="#7B79FF" stroke-width="2.5"></path><path d="M135.811 7.082v12.564a3.25 3.25 0 003.25 3.25h8.595M94.644 78.146h28.167m-28.167-55.25h28.167-28.167zm0 13h46.584-46.584zm0 14.083h46.584-46.584zm0 14.084h46.584-46.584z" stroke="#7B79FF" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"></path></g><defs><clipPath id="EmptyDocuments_svg__clip0_1142_57509"><path fill="#fff" d="M0 0h216v120H0z"></path></clipPath></defs></svg>
                    <span style="color: rgb(102, 102, 135);font-weight: 500;font-size: 1rem;line-height: 1.25;">Data sources not found.</span>
                    <span style="color: rgb(102, 102, 135);font-weight: 500;font-size: 1rem;line-height: 1.25;">To integrate data sources with the project, please add at least two sources.</span>
                </div>
                </template>
        </q-table>

    <!-- <q-dialog v-model="addDataSource" >
      <StepNewDataSource style="max-width: calc(100vh - 48px)" @finished="addDataSource = false"/>
    </q-dialog>  -->

    <FormNewDataSource v-model:show="addDataSource"></FormNewDataSource>


    </div>
</template>


<script setup>
import { computed, defineComponent,onBeforeMount, onMounted, defineProps, ref } from "vue";
import { useDataSourceStore } from 'src/stores/datasources.store.js'
import { useIntegrationStore } from 'src/stores/integration.store.js'
import { useQuasar } from 'quasar'
import {useNotify} from 'src/use/useNotify.js'
// import NewDataSourceForm from "components/forms/NewDataSourceForm.vue";
// import NewDatasourceWrapperStepper from "components/stepper/NewDatasourceWrapperStepper";
// import StepNewDataSource from "components/stepper/StepNewDataSource.vue";
import FormNewDataSource from "components/forms/FormNewDataSource.vue";
// import { odinApi } from "boot/axios";
import api from "src/api/dataSourcesAPI.js";
/*
  props
*/
const props = defineProps({
    no_shadow: { type: Boolean, default: false },
    view: { type: String, default: "datasources" },
});
const gridEnable = ref(false)
/*
  store
*/
 const notify = useNotify()
const storeDS = useDataSourceStore();
const integrationStore = useIntegrationStore();
onBeforeMount( () => {
    storeDS.setProject()
    integrationStore.init()
})
// select, name, tag, size, type -> owner, members -> delete, view local schema
const columns = [
    { name: "id", label: "Id", align: "center", field: "id", sortable: true, },
    { name: "Name", label: "Name", align: "center", field: "name", sortable: true, },
    { name: "Type", label: "Type", align: "center", field: "type", sortable: true, },
    // {name: "#Wrappers", label: "#Wrappers", align: "center", field: "wrappers", sortable: true,},
    { name: "View_triples", label: "View triples", align: "center", field: "View_triples", sortable: false, },
    {
        name: "View_Source_Graph", label: "Source Graph", align: "center", field: "View Source Graph",
        sortable: false,
    },
    { name: "actions", label: "actions", align: "center", field: "actions", sortable: false, },
];
const views = {
    "integration": ['Name', 'Type'],
    "datasources": ['Name', 'Type', '#Wrappers', 'View_triples', 'View_Source_Graph', 'actions']
}
const title = "Data Sources";
const search = ref("")
const visibleColumns = views[props.view]
const addDataSource = ref(false)
const hasSourceGraph = (props) => {
    if (props) {
        if (props.graphicalGraph) {
            return true;
        } else if (props.row.graphicalGraph) {
            return true;
        }
    }
    return false;
}
    const deleteRow = (props2) => {
        storeDS.deleteDataSource(props2.row)
        // odinApi.delete(`/dataSource/${props2.row.id}`)
        // api.deleteDS(props2.row.id)
        // .then((response) => {
        //   if (response.status == 204) {
        //     notify.positive("Successfully deleted")
        //     storeDS.deleteDataSource(props2.row)
        //     // store.dispatch('deleteDatasource', props2.row)
        //   } else {
        //     // 500
        //     notify.negative("Something went wrong in the server.")
        //   }
        // });
      
    }
</script>

<style lang="css" scoped>
</style>