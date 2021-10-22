<template>
  <div class="q-pa-md">

    <q-table ref="tableRef" :rows="datasources" :columns="columns" :filter="search" selection="multiple"
             :class="{ 'no-shadow': no_shadow }" :selected="selectedDS" row-key="id" @selection="validateSelection2"
             no-data-label="I didn't find anything for you. Consider creating a new data source."
             no-results-label="The filter didn't uncover any results" :visible-columns="visibleColumns">

      <template v-slot:top-left="">
        <div class="q-table__title">
          {{ title }}
          <q-btn v-if="view === 'datasources'" padding="none" color="secondary" icon="add" @click="newDS = true"/>
        </div>

      </template>

      <template v-slot:top-right="props">
        <q-btn v-if="view === 'datasources'" :disable='selectedDS.filter(v => (v.graphicalGraph || v.type == "INTEGRATED") ).length != 2'
               outline color="primary" label="Integrate" class="q-mr-xs"  :to="{name: 'integration', params:{setStep:2} }" />

        <q-input outlined dense debounce="400" color="primary" v-model="search">
          <template v-slot:append>
            <q-icon name="search"/>
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

      <template v-slot:body-cell-View_Source_Graph="props">
        <q-td :props="props">
          <q-btn dense round flat color="grey" :to="'/dataSources/webvowl/' + props.row.id" icon="remove_red_eye"
                 :disable="!hasSourceGraph(props)"></q-btn>

          <q-btn v-if="props.row.type == 'INTEGRATED'" dense round flat color="grey" :to="{name: 'webvowl', params: {id:props.row.id, minimalI: true}}" icon="mdi-vector-circle-variant"
                 ></q-btn>

          <q-btn v-if="props.row.type == 'INTEGRATED'" dense round flat color="grey" :to="{name: 'webvowl', params: {id:props.row.id, integrated: true}}" icon="mdi-shape-circle-plus"
          ></q-btn>

          <!--          :disable="props.row.graphicalGraph"-->
        </q-td>
      </template>

      <template v-if="view === 'datasources'" v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn dense round flat color="grey" :to="'/dataSources/view/' + props.row.id" icon="remove_red_eye"></q-btn>
          <q-btn dense round flat color="grey" @click="editRow(props)" icon="edit"></q-btn>
          <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>
        </q-td>
      </template>

      <template v-slot:no-data="{ icon, message, filter }">
        <div class="full-width row flex-center text-accent q-gutter-sm">
          <q-icon size="2em" name="sentiment_dissatisfied"/>
          <span> Well this is sad... {{ message }} </span>
          <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon"/>
        </div>
      </template>
    </q-table>

    <q-dialog v-model="newDS" >
      <NewDatasourceWrapperStepper style="max-width: calc(100vh - 48px)" @finished="newDS = false"/>
    </q-dialog>




  </div>
</template>


<script>
import {computed, defineComponent, onMounted, ref} from "vue";
import {useQuasar} from 'quasar'
import {useStore} from "vuex";
import notify from 'components/hooks/notify';
// import NewDataSourceForm from "components/forms/NewDataSourceForm.vue";
import NewDatasourceWrapperStepper from "components/stepper/NewDatasourceWrapperStepper";
import {odinApi} from "boot/axios";

export default defineComponent({
  name: "TableDataSourcesAndIntegrations",
  components: {NewDatasourceWrapperStepper},
  props: {
    no_shadow: {type: Boolean, default: false},
    view: {type: String, default: "datasources"},
  },
  setup(props) {
    const store = useStore()
    const $q = useQuasar()

    const columns = [
      {name: "id", label: "Id", align: "center", field: "id", sortable: true,},
      {name: "Name", label: "Name", align: "center", field: "name", sortable: true,},
      {name: "Type", label: "Type", align: "center", field: "type", sortable: true,},
      // {name: "#Wrappers", label: "#Wrappers", align: "center", field: "wrappers", sortable: true,},
      {name: "View Metadata", label: "View Metadata", align: "center", field: "View Metadata", sortable: false,},
      { name: "View_Source_Graph", label: "View Source Graph",align: "center", field: "View Source Graph",
        sortable: false,},
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];

    const views = {
      "integration": ['Name', 'Type'],
      "datasources": ['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions']
    }

    const visibleColumns = views[props.view]

    const datasources = computed(() => store.state.datasource.datasources)
    // const show =  ref(false)
    const newDS = ref(false)

    onMounted(() => {
      // if (datasources.value.length == 0) {
        store.dispatch("getDatasources")
      // }

    })


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


    const title = "Data Sources";

    const selectedDS = computed(() => store.state.datasource.selectedDatasources)


    const validateSelection2 = ({rows, added, evt}) => {

      if (rows.length === 0) {
        console.log("not added")
        return
      } else if (selectedDS.value.length >= 2 && added) {
        // console.log("can only select 2")
        // show error, we can only select 2
        notify.negative("You can only select 2 datasources for integration")
      } else if (rows.length === 1) {

        const row = rows[0]
        console.log("selected is ")
        console.log(row)
        if (added) {
          store.dispatch('updateSelectedDatasourcesI', row)
        } else {

          store.dispatch('deleteSelectedDatasource', row)
        }

      } else {
        //  do nothing
      }

    }

    const deleteRow = (props2) => {
      if (props2.row.wrappers > 0) {
        // this.show_warning_dialog = true;
        // this.todeleteid = props2.row.id;
        // this.rowIndex = props2.rowIndex;
      } else {
        odinApi.delete(`/dataSource/${props2.row.id}`).then((response) => {
          if (response.status == 204) {
            notify.positive("Successfully deleted")
            store.dispatch('deleteDatasource', props2.row)
          } else {
            // 500
            notify.negative("Something went wrong in the server.")
          }
        });
      }
    }

    return {
      visibleColumns,
      datasources, selectedDS, validateSelection2, columns, title,
       search: "", hasSourceGraph,deleteRow, newDS

    }

  },

});
</script>

<style lang="css" scoped>
</style>
