<template>
  <div class="q-pa-md">
    <q-table
      :rows="rows"
      :columns="columns"
      row-key="name"
      no-data-label="I didn't find anything for you. Consider creating a new data source."
      no-results-label="The filter didn't uncover any results"
    >
      <template v-slot:top-left="props">
        <div class="q-table__title">
          {{ title }}
          <q-btn
            padding="none"
            color="secondary"
            icon="add"
            @click="show_dialog = true"
          />
        </div>

        <q-dialog v-model="show_dialog" persistent>
          <q-card style="width: 700px; max-width: 80vw">
            <q-card-section>
              <div class="text-h6">New LAVMapping(owl:sameAs)</div>
            </q-card-section>

            <q-card-section>
              <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
                <div class="row">
                  <div class="col">      
                    <q-select :options="globalGraphsLabels" label="Wrapper" />
                    <h6>Attributes</h6>
                  </div>
                  <div class="col">
                    <q-select :options="dataSourcesLabels" label="Global Graph" />
                    <h6>Features</h6>
                  </div>
                </div>
                <div>
                  <q-btn label="Submit" type="submit" color="primary" />
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

      <template v-slot:body-cell-status="props">
        <q-td :props="props">
          <q-chip
            text-color="white"
            color="accent"
            v-if="props.row.graphicalGraph === ''"
          >
            Missing Graphical graph</q-chip
          >
          <q-chip text-color="white" color="blue" v-else> Completed</q-chip>
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



<script lang="ts">
import { defineComponent, ref } from "vue";
import { odinApi } from "boot/axios";
import { DataSources, GlobalGraph } from "components/models";
import { QTd } from "quasar";
export default defineComponent({
  name: "TableDataSources",
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
        name: "Wrapper",
        label: "Wrapper",
        align: "center",
        field: "wrapper",
        sortable: true,
      },
      {
        name: "Global Graph",
        label: "Global Graph",
        align: "center",
        field: "Global Graph",
        sortable: false,
      },
      {
        name: "owl:sameAs",
        label: "owl:sameAs",
        align: "center",
        field: "owl:sameAs",
        sortable: false,
      },      
      {
        name: "Covered Subgraph",
        label: "Covered Subgraph",
        align: "center",
        field: "Covered Subgraph",
        sortable: false,
      },
    ];
    const rows: DataSources[] = [];
    const globalGraphsContent: GlobalGraph[] = [];
    const dataSourcesContent: DataSources[] = [];
    const globalGraphsLabels: string[] = [];
    const dataSourcesLabels: string[] = [];
    const title = "LAVMappings";
    const show_dialog = false;

    return { columns, rows, title, show_dialog, globalGraphsContent, dataSourcesContent, globalGraphsLabels, dataSourcesLabels };
  },
  mounted() {
    this.retrieveData();
    this.globalGraphs();
    this.dataSources();
  },
  methods: {
    onSubmit() {
    },

    onReset() {
      this.show_dialog = false;
    },

    retrieveData() {
      /*odinApi.get("/lavmapping").then((response) => {
        if (response.status == 200) {
          this.rows = response.data;
        }
      });*/
    },
    globalGraphs() {
      odinApi.get("/globalGraph").then((response) => {
        if (response.status == 200) {
          console.log(response.data)
          this.globalGraphsContent = response.data;
          for (const elem of response.data) {
            this.globalGraphsLabels.push(elem.name);
          }
        }
      });
    },    
    dataSources() {
      odinApi.get("/dataSources").then((response) => {
        if (response.status == 200) {
          this.dataSourcesContent = response.data;
          for (const elem of response.data) {
            this.dataSourcesLabels.push(elem.name + '(' + elem.type + ')');
          }
        }
      });
    },
  },
});
</script>

<style lang="css" scoped>
</style>
