<template>
  <div class="q-pa-md">
    <q-table :rows="rows" :columns="columns" :filter="search" selection="multiple" v-model:selected="selected"
             row-key="name" no-data-label="I didn't find anything for you. Consider creating a new data source."
             no-results-label="The filter didn't uncover any results" :class="{ 'no-shadow': no_shadow }"
             :visible-columns="visibleColumns">
      <template v-slot:top-left="">
        <div class="q-table__title">
          {{ title }}
          <q-btn padding="none" color="secondary" icon="add" @click="show_dialog = true"/>
        </div>

        <new-data-source-form :show_dialog="show_dialog" @close-dialog="closeDialog"/>
        <q-dialog v-model="show_edit_dialog" persistent>
          <q-card style="width: 700px; max-width: 80vw">
            <q-card-section>
              <div class="text-h6">Edit data source</div>
            </q-card-section>

            <q-card-section>
              <q-form @submit="onSubmitEdit" @reset="onReset" class="q-gutter-md">
                <q-input
                  filled
                  v-model="newDataSources.name"
                  label="Introduce a data source name"
                  lazy-rules
                  :rules="[
                    (val) => (val && val.length > 0) || 'Please type a name',
                  ]"
                />

                <q-select
                  v-model="newDataSources.type"
                  :options="options"
                  label="Type"
                />

                <q-file
                  outlined
                  v-model="uploadedFile"
                  multiple
                  auto-expand
                  :headers="{ 'content-type': 'multipart/form-data' }"
                >
                  <template v-slot:prepend>
                    <q-icon name="attach_file"/>
                  </template>
                </q-file>

                <div>
                  <q-btn label="Submit" type="submit" color="primary"/>
                  <q-btn
                    label="Cancel"
                    type="reset"
                    color="primary"
                    flat
                    class="q-ml-sm"
                  />
                </div>
              </q-form>
            </q-card-section>
          </q-card>
        </q-dialog>
        <q-dialog v-model="show_warning_dialog" persistent>
          <q-card style="width: 700px; max-width: 80vw">
            <q-card-section>
              <div class="text-h4">Warning</div>
              <div class="text-h6">Some wrappers are using this data source and will be also deleted. Proceed?</div>
            </q-card-section>

            <q-card-section>
              <q-form
                @submit="onWarningSubmit"
                @reset="onReset"
                class="q-gutter-md"
              >
                <div>
                  <q-btn label="Proceed" type="submit" color="primary"/>
                  <q-btn
                    label="Cancel"
                    type="reset"
                    color="primary"
                    flat
                    class="q-ml-sm"
                  />
                </div>
              </q-form>
            </q-card-section>
          </q-card>
        </q-dialog>
      </template>

      <template v-slot:top-right="props">

        <q-btn :disable='selected.filter(v => (v.graphicalGraph ) ).length != 2'
               outline color="primary" label="Integrate" class="q-mr-xs"/>

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
import {defineComponent, ref, onMounted, computed} from "vue";
import {odinApi} from "boot/axios";
import NewDataSourceForm from "components/forms/NewDataSourceForm.vue";
// import {ref} from 'vue'
import {mapGetters} from "vuex";
import {useStore} from 'vuex'

export default defineComponent({
  name: "TableDataSources",
  components: {NewDataSourceForm},
  props: {
    no_shadow: {type: Boolean, default: false},
    view: {type: String, default: "datasources"}
  },

  setup() {

    const store = useStore()

    const datasources = computed( () => {
      return store.state.datasource.datasources
    } )

    onMounted(() => {
      if(datasources.value.length == 0){
        store.dispatch("getDatasources")
      }
    })

    return {datasources}


  },

  data() {
    const columns = [
      {name: "Name", required: true, label: "Name", align: "center", field: "name", sortable: true,},
      {name: "Type", required: true, label: "Type", align: "center", field: "type", sortable: true,},
      {name: "#Wrappers", label: "#Wrappers", align: "center", field: "wrappers", sortable: true,},
      {name: "View Metadata", label: "View Metadata", align: "center", field: "View Metadata", sortable: false,},
      {
        name: "View_Source_Graph", label: "View Source Graph", align: "center",
        field: "View Source Graph", sortable: false,
      },
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];
    const rows = [];
    const options = [
      "Avro",
      "JSONFile",
      "MongoDB",
      "Neo4j",
      "Parquet",
      "RESTAPI",
      "SQLDatabase",
    ];
    const title = "Data Sources";
    // const show_dialog = false;
    const show_edit_dialog = false;
    // const uploadedFile = new File([], "");
    const show_warning_dialog = false;
    const todeleteid = "";
    const rowIndex = 0;
    const newDataSources = {
      id: "",
      name: "",
      type: ""
    };
    const selected = ref([])
    return {
      columns,
      visibleColumns: ref(['Name', 'Type', '#Wrappers', 'View Metadata', 'View_Source_Graph', 'actions']),
      rows,
      selected,
      options,
      title,
      show_dialog: ref(false),
      newDataSources,
      // uploadedFile,
      search: "",
      show_edit_dialog,
      show_warning_dialog,
      todeleteid,
      rowIndex,
    };
  },
  mounted() {
    this.setView()
    this.retrieveData();
  },
  methods: {

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

    editRow(props) {
      this.show_edit_dialog = true;
      const row = props.row;
      this.newDataSources.id = row.id;
      this.newDataSources.name = row.name;
      this.newDataSources.type = row.type;
    },
    deleteRow(props) {
      if (props.row.wrappers > 0) {
        this.show_warning_dialog = true;
        this.todeleteid = props.row.id;
        this.rowIndex = props.rowIndex;
      } else {
        odinApi.delete(`/dataSource/${props.row.id}`).then((response) => {
          if (response.status == 204) {
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: "Successfully deleted",
            });

            this.rows.splice(props.rowIndex, 1);
          } else {
            // 500
            this.$q.notify({
              message: "Something went wrong in the server.",
              color: "negative",
              icon: "cancel",
              textColor: "white",
            });
          }
        });
      }
    },
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
    onSubmitEdit() {
      this.show_edit_dialog = false;
      odinApi
        .put(`/dataSource/${this.newDataSources.id}`, this.newDataSources)
        .then((response) => {
          if (response.status == 204) {
            this.rows.map((e) => {
              if (e.id === this.newDataSources.id) {
                e.id = this.newDataSources.id;
                e.name = this.newDataSources.name;
                e.type = this.newDataSources.type;
              }
              return e;
            });
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: `Data Source ${this.newDataSources.name} sucessfully edited`,
            });
            this.show_dialog = false;
          } else {
            this.$q.notify({
              message: "Something went wrong in the server.",
              color: "negative",
              icon: "cancel",
              textColor: "white",
            });
          }
        });
    },
    onWarningSubmit() {
      console.log(this.todeleteid);
      odinApi.delete(`/dataSource/${this.todeleteid}`).then((response) => {
        if (response.status == 204) {
          this.$q.notify({
            color: "positive",
            textColor: "white",
            icon: "check_circle",
            message: "Successfully deleted",
          });

          this.rows.splice(this.rowIndex, 1);
          this.show_warning_dialog = false;
        } else {
          // 500
          this.$q.notify({
            message: "Something went wrong in the server.",
            color: "negative",
            icon: "cancel",
            textColor: "white",
          });
        }
      });
    },

    onReset() {
      this.newDataSources.name = "";
      this.newDataSources.type = "";
      this.show_dialog = false;
      this.show_edit_dialog = false;
      this.show_warning_dialog = false;
    },

    retrieveData() {
      odinApi
        .get("/dataSource")
        .then((response) => {
          if (response.status == 200) {
            this.rows = response.data;
            console.log(response)
          }
        })
        .then(() => {
          odinApi.get("/wrapper").then((wrappers_res) => {
            const arrayOfWrappers = wrappers_res.data;
            // Calculates the number of wrappers per DataSource
            this.rows.map((r) => {
              let size = 0;
              for (const w of arrayOfWrappers)
                if (w.dataSourcesId === r.id) ++size;
              r.wrappers = size;
              return r;
            });
          });
        });
    },
  },
});
</script>

<style lang="css" scoped>
</style>
