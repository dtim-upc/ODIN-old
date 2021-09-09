<template>
  <div class="q-pa-md">

    <q-table ref="tableRef" :rows="datasources" :columns="columns" :filter="search" selection="multiple"
             :selected="selectedDS"
             row-key="id" no-data-label="I didn't find anything for you. Consider creating a new data source."
             no-results-label="The filter didn't uncover any results" :class="{ 'no-shadow': no_shadow }"
             :visible-columns="visibleColumns" @selection="validateSelection2"
    >

      <template v-slot:top-left="">
        <div class="q-table__title">
          {{ title }}
        </div>

      </template>

      <template v-slot:top-right="props">

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
          <!--          :disable="props.row.graphicalGraph"-->
        </q-td>
      </template>

      <template v-slot:body-cell-actions="props">
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
  </div>
</template>


<script>
import {computed, defineComponent, onMounted} from "vue";
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {useStore} from "vuex";
import notify from 'components/hooks/notify';

export default defineComponent({
  name: "TableDataSources2",
  props: {
    no_shadow: {type: Boolean, default: false},
    view: {type: String, default: "datasources"},
  },
  setup(props) {
    const store = useStore()
    const $q = useQuasar()

    const columns = [
      {name: "id",  label: "Id", align: "center", field: "id", sortable: true,},
      {name: "Name",  label: "Name", align: "center", field: "name", sortable: true,},
      {name: "Type",  label: "Type", align: "center", field: "type", sortable: true,},
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];

    const views = {
      "integration": ['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions'],
      "datasources": [ 'Name', 'Type', 'actions']
      // "datasources": ['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions']
    }
    // console.log(props.view)
    // console.log(views[props.view])
    // const visibleColumns = ref( [ 'Name', 'Type', 'actions'])

    const datasources = computed(() => store.state.datasource.datasources)

    onMounted(() => {
      // if(datasources.value.length == 0){
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
        if (added) {
          store.dispatch('updateSelectedDatasourcesI', row)
        } else {

          store.dispatch('deleteSelectedDatasource', row)
        }

      } else {
        //  do nothing
      }

    }

    return {
      visibleColumns: ref( [ 'Name', 'Type', 'actions']),
      datasources, selectedDS, validateSelection2, columns, title,
      show_dialog: ref(false), search: "", hasSourceGraph,


    }

  },

});
</script>

<style lang="css" scoped>
</style>
