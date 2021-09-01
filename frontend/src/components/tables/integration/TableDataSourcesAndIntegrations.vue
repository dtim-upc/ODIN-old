<template>
  <div class="q-pa-md">

    <q-table ref="tableRef" :rows="rows" :columns="columns" :filter="search" selection="multiple"
             :selected="selected"
             row-key="name" no-data-label="I didn't find anything for you. Consider creating a new data source."
             no-results-label="The filter didn't uncover any results" :class="{ 'no-shadow': no_shadow }"
             :visible-columns="visibleColumns" @selection="validateSelection"
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
          <q-chip
            text-color="white"
            color="accent"
            v-if="props.row.graphicalGraph === ''"
          >
            Missing Data Sources
          </q-chip
          >
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


<script >
import {defineComponent} from "vue";
import {odinApi} from "boot/axios";
import {ref} from 'vue'
import {QTable} from "quasar";
import {useQuasar} from 'quasar'

export default defineComponent({
  name: "TableDataSourcesAndIntegrations",
  props: {
    no_shadow: {type: Boolean, default: false},
    view: {type: String, default: "datasources"},
    // dsA: {type: DataSourceModel, default: {id:"", name:"", type:"", graphicalGraph:""}},
    // dsB: {type: DataSourceModel, default: {id:"", name:"", type:"", graphicalGraph:""}},
    // selected: {type: Array, default:[]}

    //
  },
  emits: {
    "selected": null
  },
  data() {
    const columns = [
      {name: "Name", required: true, label: "Name", align: "center", field: "name", sortable: true,},
      {name: "Type", required: true, label: "Type", align: "center", field: "type", sortable: true,},
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];
    const rows = [];
    const selected = ref([])
    const title = "Data Sources";
    // const show_dialog = false;
    // const show_edit_dialog: boolean = false;
    // const uploadedFile = new File([], "");
    // const show_warning_dialog: boolean = false;
    // const todeleteid: string = "";
    const rowIndex = 0;
    const newDataSources = {
      id: "",
      name: "",
      type: ""
    };
    // const tableRef = ref(null);
    const $q = useQuasar()
    return {
      columns,
      visibleColumns: ref(['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions']),
      rows,
      $q,
      selected,
      // options,
      title,
      // tableRef,
      show_dialog: ref(false),
      newDataSources,
      // uploadedFile,
      search: "",
      // show_edit_dialog,
      // show_warning_dialog,
      // todeleteid,
      rowIndex,

    };
  },
  mounted() {
    // this.setView()
    this.retrieveData();
  },
  methods: {
    validateSelection({rows, added, evt}) {

      if (rows.length === 0) {
        console.log("not added")
        return
      }
      const row = rows[0]
        if(this.selected){
          let fl = this.selected.length <2 && added
          if(this.selected.value){
            let flag =this.selected.value.length <2
          }
          if (this.selected.length < 2 && added) {
            console.log("added")
            this.selected = this.selected.concat(row)
          } else if (!added) {
            console.log("remove")
            this.selected = this.selected.filter(x => x.name != row.name)
          }
        }
      this.$emit('selected', {data: this.selected} )
        return
      //
    },
    setView() {
      switch (this.view) {
        case "integrationTask":
          this.visibleColumns = ['Name', 'Type'];
          break;
        case "integration":
          this.visibleColumns = ['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions'];
          break;
        default:
          this.visibleColumns = ['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions'];
          break;
      }
    },

    hasSourceGraph(props) {

      if (props) {
        if (props.graphicalGraph) {
          return true;
        } else if (props.row.graphicalGraph) {
          return true;
        }
      }


      return false;
    },
    closeDialog(props) {
      console.log("closed")
      console.log(props)
      this.show_dialog = !this.show_dialog
      if (props.data) {
        this.rows.push(props.data);
      }
    },


    // editRow(props: any) {
    //   this.show_edit_dialog = true;
    //   const row = props.row;
    //   this.newDataSources.id = row.id;
    //   this.newDataSources.name = row.name;
    //   this.newDataSources.type = row.type;
    // },
    // deleteRow(props: any) {
    //   if (props.row.wrappers > 0) {
    //     this.show_warning_dialog = true;
    //     this.todeleteid = props.row.id;
    //     this.rowIndex = props.rowIndex;
    //   } else {
    //     odinApi.delete(`/dataSource/${props.row.id}`).then((response) => {
    //       if (response.status == 204) {
    //         this.$q.notify({
    //           color: "positive",
    //           textColor: "white",
    //           icon: "check_circle",
    //           message: "Successfully deleted",
    //         });
    //
    //         this.rows.splice(props.rowIndex, 1);
    //       } else {
    //         // 500
    //         this.$q.notify({
    //           message: "Something went wrong in the server.",
    //           color: "negative",
    //           icon: "cancel",
    //           textColor: "white",
    //         });
    //       }
    //     });
    //   }
    // },
    // onSubmit() {
    //   odinApi
    //     .post("/dataSource", this.newDataSources)
    //     .then((response) => {
    //       if (response.status == 201) {
    //         this.$q.notify({
    //           color: "positive",
    //           textColor: "white",
    //           icon: "check_circle",
    //           message: `Data Source ${this.newDataSources.name} sucessfully created`,
    //         });
    //         response.data.wrappers = 0;
    //         this.rows.push(response.data);
    //         this.show_dialog = false;
    //       } else {
    //         this.$q.notify({
    //           message: "Something went wrong in the server.",
    //           color: "negative",
    //           icon: "cancel",
    //           textColor: "white",
    //         });
    //       }
    //     })
    //     .then(() => {
    //       var data = new FormData();
    //       data.append("file", this.uploadedFile[0]);
    //       odinApi
    //         .post("/dataSource/uploadFile", data, {
    //           headers: {
    //             "Content-Type": "multipart/form-data",
    //           },
    //         })
    //         .then((response) => console.log(response));
    //     });
    // },
    // onSubmitEdit() {
    //   this.show_edit_dialog = false;
    //   odinApi
    //     .put(`/dataSource/${this.newDataSources.id}`, this.newDataSources)
    //     .then((response) => {
    //       if (response.status == 204) {
    //         this.rows.map((e) => {
    //           if (e.id === this.newDataSources.id) {
    //             e.id = this.newDataSources.id;
    //             e.name = this.newDataSources.name;
    //             e.type = this.newDataSources.type;
    //           }
    //           return e;
    //         });
    //         this.$q.notify({
    //           color: "positive",
    //           textColor: "white",
    //           icon: "check_circle",
    //           message: `Data Source ${this.newDataSources.name} sucessfully edited`,
    //         });
    //         this.show_dialog = false;
    //       } else {
    //         this.$q.notify({
    //           message: "Something went wrong in the server.",
    //           color: "negative",
    //           icon: "cancel",
    //           textColor: "white",
    //         });
    //       }
    //     });
    // },
    // onWarningSubmit() {
    //   console.log(this.todeleteid);
    //   odinApi.delete(`/dataSource/${this.todeleteid}`).then((response) => {
    //     if (response.status == 204) {
    //       this.$q.notify({
    //         color: "positive",
    //         textColor: "white",
    //         icon: "check_circle",
    //         message: "Successfully deleted",
    //       });
    //
    //       this.rows.splice(this.rowIndex, 1);
    //       this.show_warning_dialog = false;
    //     } else {
    //       // 500
    //       this.$q.notify({
    //         message: "Something went wrong in the server.",
    //         color: "negative",
    //         icon: "cancel",
    //         textColor: "white",
    //       });
    //     }
    //   });
    // },

    // onReset() {
    //   this.newDataSources.name = "";
    //   this.newDataSources.type = "";
    //   this.show_dialog = false;
    //   this.show_edit_dialog = false;
    //   this.show_warning_dialog = false;
    // },

    retrieveData() {
      odinApi
        .get("/dataSource")
        .then((response) => {
          if (response.status == 200) {
            this.rows = response.data.filter(v => (v.graphicalGraph ) );
            console.log(response)
          }
        })

    },
  },
});
</script>

<style lang="css" scoped>
</style>
