<template>
  <div class="q-pa-md">
    <q-table
      :rows="rows"
      :columns="columns"
      :filter="search"
      row-key="name"
      no-data-label="I didn't find anything for you. Consider creating a new data source."
      no-results-label="The filter didn't uncover any results"
    >
      <template v-slot:top-left="">
        <div class="q-table__title">
          {{ title }}
          <q-btn
            padding="none"
            color="secondary"
            icon="add"
            @click="show_dialog = true"
          />
        </div>
        <q-input dense debounce="400" color="primary" v-model="search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>

        <q-dialog v-model="show_dialog" persistent>
          <q-card style="width: 700px; max-width: 80vw">
            <q-card-section>
              <div class="text-h6">Create new data source</div>
            </q-card-section>

            <q-card-section>
              <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
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
                
                <!-- <q-file
                  outlined
                  v-model="uploadedFile"
                  multiple
                  auto-expand
                  :headers="{ 'content-type': 'multipart/form-data' }"
                >
                  <template v-slot:prepend>
                    <q-icon name="attach_file" />
                  </template>
                </q-file> -->
                <q-toggle
                  v-model="mustUploadFile"
                  label="Upload file to remote database"
                />

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
        <q-dialog v-model="show_edit_dialog" persistent>
          <q-card style="width: 700px; max-width: 80vw">
            <q-card-section>
              <div class="text-h6">Edit data source</div>
            </q-card-section>

            <q-card-section>
              <q-form
                @submit="onSubmitEdit"
                @reset="onReset"
                class="q-gutter-md"
              >
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
                    <q-icon name="attach_file" />
                  </template>
                </q-file>
                <q-toggle
                  v-model="mustUploadFile"
                  label="Upload file to remote database"
                />

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
            Missing Data Sources</q-chip
          >
          <q-chip text-color="white" color="blue" v-else> Completed</q-chip>
        </q-td>
      </template>

      <template v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn
            dense
            round
            flat
            color="grey"
            :to="'/dataSources/view/' + props.row.id"
            icon="remove_red_eye"
          ></q-btn>
          <q-btn
            dense
            round
            flat
            color="grey"
            @click="editRow(props)"
            icon="edit"
          ></q-btn>
          <q-btn
            dense
            round
            flat
            color="grey"
            @click="deleteRow(props)"
            icon="delete"
          ></q-btn>
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
import { defineComponent } from "vue";
import { odinApi } from "boot/axios";
import { DataSources, Wrapper } from "components/models";
export default defineComponent({
  name: "TableDataSources",
  data() {
    const columns = [
      {
        name: "Name",
        required: true,
        label: "Name",
        align: "center",
        field: "name",
        sortable: true,
      },
      {
        name: "Type",
        required: true,
        label: "Type",
        align: "center",
        field: "type",
        sortable: true,
      },
      {
        name: "#Wrappers",
        label: "#Wrappers",
        align: "center",
        field: "wrappers",
        sortable: true,
      },
      {
        name: "View Metadata",
        label: "View Metadata",
        align: "center",
        field: "View Metadata",
        sortable: false,
      },
      {
        name: "View Source Graph",
        label: "View Source Graph",
        align: "center",
        field: "View Source Graph",
        sortable: false,
      },
      {
        name: "actions",
        label: "actions",
        align: "center",
        field: "actions",
        sortable: false,
      },
    ];
    const rows: {
      id: string;
      name: string;
      type: string;
      wrappers: number;
    }[] = [];
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
    const show_dialog = false;
    const show_edit_dialog: boolean = false;
    const uploadedFile: File = new File([], "");
    const mustUploadFile: boolean = false;
    const newDataSources = {
      id: "",
      name: "",
      type: "",
    };
    return {
      columns,
      rows,
      options,
      title,
      show_dialog,
      newDataSources,
      uploadedFile,
      search: "",
      mustUploadFile,
      show_edit_dialog,
    };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    editRow(props: any) {
      this.show_edit_dialog = true;
      const row = props.row;
      this.newDataSources.id = row.id;
      this.newDataSources.name = row.name;
      this.newDataSources.type = row.type;
    },
    deleteRow(props: any) {
      odinApi.delete(`/dataSources/${props.row.id}`).then((response) => {
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
    },
    onSubmit() {
      odinApi.post("/dataSources", this.newDataSources).then((response) => {
        if (response.status == 201) {
          this.$q.notify({
            color: "positive",
            textColor: "white",
            icon: "check_circle",
            message: `Data Source ${this.newDataSources.name} sucessfully created`,
          });
          response.data.wrappers = 0;
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
      odinApi
        .post(
          `/dataSources/edit/${this.newDataSources.id}`,
          this.newDataSources
        )
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

    onReset() {
      this.newDataSources.name = "";
      this.newDataSources.type = "";
      this.show_dialog = false;
      this.show_edit_dialog = false;
    },

    retrieveData() {
      odinApi
        .get("/dataSources")
        .then((response) => {
          if (response.status == 200) {
            this.rows = response.data;
          }
        })
        .then(() => {
          odinApi.get("/wrapper").then((wrappers_res) => {
            const arrayOfWrappers: Wrapper[] = wrappers_res.data;
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
