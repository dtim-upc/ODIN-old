<template>

  <div class="q-pa-md">
    <q-table
      :rows="rows"
      :columns="columns"
      row-key="name"
      no-data-label="I didn't find anything for you. Consider creating a new global graph."
      no-results-label="The filter didn't uncover any results"
    >

    <template v-slot:top-left="props">
        <div class="q-table__title">
          {{title}} <q-btn padding="none" color="secondary" icon="add" @click="show_dialog = true"/>
        </div>

        <q-dialog v-model="show_dialog" persistent >
          <q-card  style="width: 700px; max-width: 80vw;">

            <q-card-section>
              <div class="text-h6">Create new global graph</div>
            </q-card-section>

            <q-card-section>
              <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
                    <q-input filled
                      v-model="newGlobalGraph.name"
                      label="Introduce a global graph name"
                      lazy-rules
                      :rules="[ val => val && val.length > 0 || 'Please type a name']"
                    />

                    <q-input
                      filled
                      type="url"
                      v-model="newGlobalGraph.namespace"
                      label="Introduce a namespace"
                      lazy-rules
                      :rules="[
                        val => val !== null && val !== '' || 'Please type a namespace'
                      ]"
                    />

                    <div>
                      <q-btn label="Submit" type="submit" color="primary"/>
                      <q-btn label="Cancel" type="reset" color="primary" flat class="q-ml-sm" />
                    </div>
                  </q-form>

            </q-card-section>
          </q-card>
        </q-dialog>

    </template>

    <template v-slot:top-right="props">
        <q-btn
          flat
          round
          dense
          :icon="props.inFullscreen ? 'fullscreen_exit' : 'fullscreen'"
          @click="props.toggleFullscreen"
        >
           <q-tooltip
            :disable="$q.platform.is.mobile"
            v-close-popup
            >
              {{props.inFullscreen ? 'Exit Fullscreen' : 'Toggle Fullscreen'}}
            </q-tooltip>
        </q-btn>
    </template>

    <template v-slot:body-cell-status="props">
      <q-td :props="props">
        <q-chip text-color="white"  color="accent" v-if="props.row.graphicalGraph === ''" > Missing Graphical graph</q-chip>
        <q-chip text-color="white"  color="blue" v-else> Completed</q-chip>
      </q-td>
    </template>


    <template v-slot:body-cell-actions="props">
      <q-td :props="props">
        <q-btn dense round flat color="grey" @click="editRow(props)" icon="edit"></q-btn>
        <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>
      </q-td>
    </template>


    <template v-slot:no-data="{ icon, message, filter }">
            <div class="full-width row flex-center text-accent q-gutter-sm">
              <q-icon size="2em" name="sentiment_dissatisfied" />
              <span>
                Well this is sad... {{ message }}
              </span>
              <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon" />
            </div>
   </template>

  </q-table>
  </div>


</template>



<script lang="ts">
import { defineComponent, ref } from 'vue';
import { odinApi } from 'boot/axios';
import { GlobalGraph } from 'components/models';
import {QTd} from 'quasar'
export default defineComponent({


  name: 'TableGlobalGraph',
  // props:{
  //   rows: {
  //     type: Array,
  //     default() {
  //       return []
  //     }
  //   }
  // },
  data() {

    const columns = [
      { name: 'name', required: true, label: 'Name', align: 'center', field: 'name', sortable: true},
      { name: 'named_graph', required: true, label: 'NamedGraph', align: 'center', field: 'namedGraph', sortable: true},
      { name: 'status', label: 'status', align: 'center', field: 'status', sortable: true},
      { name: 'actions', label: 'actions', align: 'center', field: 'actions', sortable: false}
    ]
    const rows: GlobalGraph[] = []
    const title = 'Global Graphs'
    const show_dialog = false
    const newGlobalGraph = {
      name: '',
      namespace:'',
      graphicalGraph: ''
    }

    return { columns, rows, title, show_dialog, newGlobalGraph  };
  },
  mounted() {
    this.retrieveData()
  },
  methods: {
    // editRow(props) {
    //   // do something
    //   console.log(props.row)
    // },

    onSubmit () {

      odinApi.post('/globalGraph', this.newGlobalGraph)
              .then(response => {
                if(response.status == 201) {
                  this.$q.notify({
                    color: 'positive',
                    textColor: 'white',
                    icon: 'check_circle',
                    message: `Global graph ${this.newGlobalGraph.name} sucessfully created`
                  })
                  this.rows.push(response.data)
                  this.show_dialog = false
                } else {
                  this.$q.notify({
                    message: 'Something went wrong in the server.',
                    color: 'negative',
                    icon: 'cancel',
                    textColor: 'white'
                  })
                }
      });
    },

    onReset () {
        this.newGlobalGraph.name = ''
        this.newGlobalGraph.namespace = ''
        this.newGlobalGraph.graphicalGraph = ''
        this.show_dialog = false
    },

    retrieveData(){
      odinApi.get('/globalGraph')
              .then(response => {
                if(response.status == 200) {
                  this.rows = response.data;
                }
               });
    },

    deleteRow(props){
      console.log(props)
      console.log(props.row.id)
      odinApi.delete(`/globalGraph/${props.row.id}` )
              .then(response => {

                if(response.status == 204){
                  console.log('response')
                  console.log(response)


                  this.$q.notify({
                    color: 'positive',
                    textColor: 'white',
                    icon: 'check_circle',
                    message: 'Successfully deleted'
                  })

                  this.rows.splice(props.rowIndex, 1);
                } else {
                  // 500
                  this.$q.notify({
                    message: 'Something went wrong in the server.',
                    color: 'negative',
                    icon: 'cancel',
                    textColor: 'white'
                  })
                }

               });
    }
  }

});
</script>

<style lang="css" scoped>
</style>
