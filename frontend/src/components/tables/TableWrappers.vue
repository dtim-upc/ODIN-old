<template>
  <div class="q-pa-md">
    <q-table
      :rows="rows"
      :columns="columns"
      :filter="search"
      row-key="name"
      no-data-label="I didn't find anything for you. Consider creating a new wrapper."
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
              <div class="text-h6">Create new wrapper</div>
            </q-card-section>

            <q-card-section>
              <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
                <q-input
                  filled
                  v-model="newWrapper.name"
                  label="Introduce a wrapper name"
                  lazy-rules
                  :rules="[
                    (val) => (val && val.length > 0) || 'Please type a name',
                  ]"
                />
                <q-select
                  v-model="newWrapper.dataSourcesId"
                  :options="dataSources"
                  label="Data Sources"
                  :rules="[(val) => !!val || 'Field is required']"
                  emit-value
                  map-options
                />
                <q-input bottom-slots v-model="attrib" label="Attributes">
                  <template v-slot:append>
                    <q-btn round dense flat icon="add" @click="addAttrib()" />
                  </template>
                </q-input>

                <div class="q-pa-md">
                  <div class="q-gutter-xs">
                    <q-chip
                      size="lg"
                      v-for="x in newWrapper.attributes"
                      :key="x"
                      removable
                      color="primary"
                      text-color="white"
                      @remove="removeAttrib(x)"
                    >
                      {{ x.name }}
                    </q-chip>
                  </div>
                </div>
                <q-toggle v-model="inferSchema" label="Infer Schema" />

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
              <div class="text-h6">Edit wrapper</div>
            </q-card-section>

            <q-card-section>
              <q-form
                @submit="onSubmitEdit"
                @reset="onReset"
                class="q-gutter-md"
              >
                <q-input
                  filled
                  v-model="newWrapper.name"
                  label="Introduce a wrapper name"
                  lazy-rules
                  :rules="[
                    (val) => (val && val.length > 0) || 'Please type a name',
                  ]"
                />
                <q-select
                  v-model="newWrapper.dataSourcesId"
                  :options="dataSources"
                  label="Data Sources"
                  :rules="[(val) => !!val || 'Field is required']"
                  emit-value
                  map-options
                />
                <q-input bottom-slots v-model="attrib" label="Attributes">
                  <template v-slot:append>
                    <q-btn round dense flat icon="add" @click="addAttrib()" />
                  </template>
                </q-input>

                <div class="q-pa-md">
                  <div class="q-gutter-xs">
                    <q-chip
                      size="lg"
                      v-for="x in newWrapper.attributes"
                      :key="x"
                      removable
                      color="primary"
                      text-color="white"
                      @remove="removeAttrib(x)"
                    >
                      {{ x.name }}
                    </q-chip>
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
            v-if="props.row.Wrapper === ''"
          >
            Missing Wrapper</q-chip
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
            :to="'/wrappers/view/' + props.row.id"
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
import { Wrapper, Attribute } from "components/models";
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
        name: "Name",
        required: true,
        label: "Name",
        align: "center",
        field: "name",
        sortable: true,
      },
      {
        name: "Data Source",
        label: "Data Source",
        align: "center",
        field: "dataSourcesLabel",
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
        name: "actions",
        label: "actions",
        align: "center",
        field: "actions",
        sortable: false,
      },
    ];
    const rows: Wrapper[] = [];
    const title = "Wrappers";
    const show_dialog = false;
    const show_edit_dialog = false;
    const newWrapper = {
      id: "",
      name: "",
      attributes: Array<Attribute>(),
      dataSourcesId: "",
      dataSourcesLabel: "",
    };
    const dataSources: { label: string; value: string }[] = [];
    const attrib: string = "";
    const inferSchema: boolean = false;

    return {
      columns,
      rows,
      title,
      show_dialog,
      show_edit_dialog,
      newWrapper,
      dataSources,
      attrib,
      search: "",
      inferSchema,
    };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    editRow(props: any) {
      this.show_edit_dialog = true;
      const row = props.row;
      this.newWrapper.id = row.id;
      this.newWrapper.name = row.name;
      this.newWrapper.attributes = row.attributes;
      this.newWrapper.dataSourcesId = row.dataSourcesId;
      this.newWrapper.dataSourcesLabel = row.dataSourcesLabel;
    },
    deleteRow(props: any) {
      odinApi.delete(`/wrapper/${props.row.id}`).then((response) => {
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
      if (this.newWrapper.attributes.length === 0) {
        this.$q.notify({
          color: "negative",
          textColor: "white",
          message: "Please add at least 1 attribute",
        });
      } else {
        console.log(this.newWrapper);
        odinApi.post("/wrapper", this.newWrapper).then((response) => {
          if (response.status == 201) {
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: `Wrapper ${this.newWrapper.name} sucessfully created`,
            });
            const index = this.dataSources
              .map((e) => e.value)
              .indexOf(response.data.dataSourcesId);
            if (index !== -1) {
              response.data["dataSourcesLabel"] = this.dataSources[index].label;
            }
            console.log(response.data)
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
      }
      this.onReset();
    },
    onSubmitEdit() {
      this.show_edit_dialog = false;
      odinApi
        .put(`/wrapper/${this.newWrapper.id}`, this.newWrapper)
        .then((response) => {
          if (response.status == 204) {
            this.rows.map((e) => {
              if (e.id === this.newWrapper.id) {
                e.id = this.newWrapper.id;
                e.name = this.newWrapper.name;
                const aux: [Attribute] = [{isID:false, name:""}];
                aux.pop();
                for (const a of this.newWrapper.attributes) {
                  aux.push(a);
                }
                e.attributes = aux;
                e.dataSourcesId = this.newWrapper.dataSourcesId;
                console.log(this.dataSources)
                const index = this.dataSources
                  .map((e) => e.value)
                  .indexOf(e.dataSourcesId);
                console.log(index)
                if (index !== -1) {
                  e.dataSourcesLabel = this.dataSources[index].label;
                } else {
                  this.$q.notify({
                    message: "Something went wrong in the server.",
                    color: "negative",
                    icon: "cancel",
                    textColor: "white",
                  });
                  e.dataSourcesLabel = "ERROR DATA SOURCE NOT FOUND"
                }
              }
              return e;
            });
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: `Wrapper ${this.newWrapper.name} sucessfully edited`,
            });
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
      this.newWrapper.name = "";
      this.newWrapper.attributes = [];
      this.newWrapper.dataSourcesId = "";
      this.show_dialog = false;
      this.show_edit_dialog = false;
    },
    retrieveData() {
      odinApi.get("/wrapper").then((response) => {
        console.log(response.status);
        if (response.status == 200 || response.status == 204) {
          this.getDataSources(response.data);
        }
      });
    },
    getDataSources(data: any) {
      odinApi.get("/dataSources").then((response) => {
        if (response.status == 200) {
          for (const elem of response.data) {
            const obj = {
              label: elem.name + "(" + elem.type + ")",
              value: elem.id,
            };
            this.dataSources.push(obj);
          }
          if (data) this.getDataSourcesLabels(this.dataSources, data);
        }
      });
    },
    getDataSourcesLabels(ds: { label: string; value: string }[], data: any) {
      this.rows = data.map(
        (elem: { [x: string]: string; dataSourcesId: string }) => {
          if (elem.dataSourcesId) {
            const index = ds.map((e) => e.value).indexOf(elem.dataSourcesId);
            if (index !== -1) {
              elem["dataSourcesLabel"] = ds[index].label;
            } else {
              elem["dataSourcesLabel"] = "ERROR DATA SOURCE NOT FOUND";
            }
          }
          return elem;
        }
      );
    },
    addAttrib() {
      if (
        this.attrib !== "" &&
        this.newWrapper.attributes.indexOf({isID:false, name: this.attrib}) === -1
      )
        this.newWrapper.attributes.push({isID:false, name: this.attrib});
      this.attrib = "";
    },
    removeAttrib(att: Attribute) {
      this.newWrapper.attributes = this.newWrapper.attributes
        .map((e) => e)
        .filter((e) => e !== att);
    },
  },
});
</script>

<style lang="css" scoped>
</style>
