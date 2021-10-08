<template>
  <div class="q-pa-md">
    <q-table
      :rows="rows"
      :columns="columns"
      :filter="search"
      row-key="name"
      no-data-label="I didn't find anything for you. Consider creating a new global graph."
      no-results-label="The filter didn't uncover any results"
    >
      <template v-slot:top-left="">
        <div class="q-table__title">
          {{ title }}
        </div>
        <q-input dense debounce="400" color="primary" v-model="search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>

      </template>

      <template v-slot:top-right="props">
        <q-btn
          flat
          round
          dense
          :icon="props.inFullscreen ? 'fullscreen_exit' : 'fullscreen'"
          @click="props.toggleFullscreen"
        >
          <q-tooltip :disable="$q.platform.is.mobile" v-close-popup>
            {{ props.inFullscreen ? "Exit Fullscreen" : "Toggle Fullscreen" }}
          </q-tooltip>
        </q-btn>
      </template>

      <template v-slot:body-cell-OMQ="props">
        <q-td :props="props">
          <q-btn
            dense
            round
            flat
            color="grey"
            :to="'/omq/' + props.row.id +'/'+props.row.id"
            icon="edit"
          ></q-btn>
<!--          {{props.row.wrappers[0].iri}}-->
        </q-td>
      </template>

      <template v-slot:no-data="{ icon, message, filter }">
        <div class="full-width row flex-center text-accent q-gutter-sm">
          <q-icon size="2em" name="sentiment_dissatisfied" />
          <span> Well this is sad... {{ message }} </span>
          <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon" />
        </div>
      </template>
    </q-table>
  </div>
</template>



<script >
import { defineComponent } from "vue";
import { odinApi } from "boot/axios";

export default defineComponent({
  name: "TableOMQ",
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
      {
        name: "name",
        required: true,
        label: "Name",
        align: "center",
        field: "name",
        sortable: true,
      },
      {
        name: "named_graph",
        required: true,
        label: "NamedGraph",
        align: "center",
        field: "iri",
        sortable: true,
      },
      // {
      //   name: "status",
      //   label: "status",
      //   align: "center",
      //   field: "status",
      //   sortable: true,
      // },
      {
        name: "OMQ",
        label: "OMQ",
        align: "center",
        field: "OMQ",
      }
      // {
      //   name: "actions",
      //   label: "actions",
      //   align: "center",
      //   field: "actions",
      //   sortable: false,
      // },
    ];
    const rows = [];
    const title = "Manage POSE OMQ";
    const show_dialog = false;
    const show_edit_dialog = false;
    const newGlobalGraph = {
      id: "",
      name: "",
      namedGraph: "",
      namespace: "",
      graphicalGraph: "",
    };

    return {
      columns,
      rows,
      title,
      show_dialog,
      newGlobalGraph,
      search: "",
      show_edit_dialog,
    };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    // id:string;
    // name:string;
    // namedGraph:string;
    // graphicalGraph:string;
    // namespace:string;
    editRow(props) {
      this.show_edit_dialog = true;
      const row = props.row;
      this.newGlobalGraph.id = row.id;
      this.newGlobalGraph.name = row.name;
      this.newGlobalGraph.namedGraph = row.namedGraph;
      this.newGlobalGraph.graphicalGraph = row.graphicalGraph;
      this.newGlobalGraph.namespace = row.namespace;
    },

    onSubmit() {
      if (this.newGlobalGraph.namespace.substr(-1) !== '/') {
        this.newGlobalGraph.namespace += '/'
      }
      odinApi.post("/globalGraph", this.newGlobalGraph).then((response) => {
        if (response.status == 201) {
          this.$q.notify({
            color: "positive",
            textColor: "white",
            icon: "check_circle",
            message: `Global graph ${this.newGlobalGraph.name} sucessfully created`,
          });
          this.rows.push(response.data);
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
    onSubmitEdit() {
      this.show_edit_dialog = false;
      const data = {
        globalGraph: this.newGlobalGraph,
        isModified: true,
        ttl: "string",
        deleted: {
          classes: [],
          properties: [],
        },
      };

      odinApi
        .put(
          `/globalGraph/${this.newGlobalGraph.id}`,
          //this.newGlobalGraph
          data
        )
        .then((response) => {
          if (response.status == 200) {
            this.rows.map((e) => {
              if (e.id === this.newGlobalGraph.id) {
                e.id = this.newGlobalGraph.id;
                e.name = this.newGlobalGraph.name;
                e.namedGraph = this.newGlobalGraph.namedGraph;
                e.graphicalGraph = this.newGlobalGraph.graphicalGraph;
                e.namespace = this.newGlobalGraph.namespace;
              }
              return e;
            });
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: `Global graph ${this.newGlobalGraph.name} sucessfully edited`,
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

    onReset() {
      this.newGlobalGraph.name = "";
      this.newGlobalGraph.namespace = "";
      this.newGlobalGraph.graphicalGraph = "";
      this.show_dialog = false;
      this.show_edit_dialog = false;
    },

    retrieveData() {
      odinApi.get("/dataSource").then((response) => {
        if (response.status == 200) {
          this.rows = response.data;
          console.log(response.data)
        }
      });
    },

    deleteRow(props) {
      odinApi.delete(`/globalGraph/${props.row.id}`).then((response) => {
        if (response.status == 204 || response.status == 200) {
          console.log(response);

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
    },
  },
});
</script>

<style lang="css" scoped>
</style>
