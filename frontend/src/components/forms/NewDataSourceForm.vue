<template>
  <q-dialog v-model="show_dialog" persistent>
    <q-card style="width: 700px; max-width: 80vw">
      <q-card-section>
        <div class="text-h6">Create new data source</div>
      </q-card-section>

      <q-card-section>

        <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
          <q-input filled v-model="newDataSources.name" label="Introduce a data source name" lazy-rules
                   :rules="[                      (val) => (val && val.length > 0) || 'Please type a name',                    ]"/>
          <q-select v-model="DataSourceType" :options="options" label="Type" class="q-mt-none"/>

          <q-file outlined v-model="uploadedFile" auto-expand label="Select the file you would like to import."
                  :headers="{ 'content-type': 'multipart/form-data' }" accept="text/csv, application/json">
            <template v-slot:prepend>
              <q-icon name="attach_file"/>
            </template>


          </q-file>

          <p>Select a bootstrapping type:</p>
          <q-option-group name="bootstrapping_type" v-model="bootstrappingType"
                          :options="[{label: 'Automatically', value: 'auto'},{label: 'Manual', value: 'manual'}]"
                          color="primary" inline/>
          <div>
            <q-btn label="Submit" type="submit" color="primary"/>
            <q-btn label="Cancel" type="reset" color="primary" flat class="q-ml-sm"/>
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>


  <!--  <q-dialog v-model="show_dialog" persistent>-->
  <!--    <q-stepper v-model="step" header-nav ref="stepper" color="primary" animated style="width: 700px; max-width: 80vw;">-->
  <!--      <q-step :name="1" title="Create" icon="settings" :done="step > 1" :header-nav="step > 1">-->


  <!--        <q-form ref="DSForm" @submit="onSubmit" class="q-gutter-md">-->
  <!--          <q-input filled v-model="newDataSources.name" label="Introduce a name for the data source" lazy-rules-->
  <!--                   :rules="[(val) => (val && val.length > 0) || 'Please type a name',]"/>-->

  <!--          <q-select v-model="newDataSources.type" :options="options" label="Select a datasource type" class="q-mt-none"/>-->

  <!--          <q-file outlined v-model="uploadedFile" multiple label="Select the file you would like to import." auto-expand-->
  <!--                  :headers="{ 'content-type': 'multipart/form-data' }" accept="text/csv, application/json">-->
  <!--            <template v-slot:prepend>-->
  <!--              <q-icon name="attach_file"/>-->
  <!--            </template>-->
  <!--          </q-file>-->

  <!--        </q-form>-->
  <!--        <q-stepper-navigation>-->

  <!--          &lt;!&ndash;          () => { done1 = true; step = 2 }&ndash;&gt;-->
  <!--          <q-btn @click="validate()" color="primary" label="Create"/>-->
  <!--          <q-btn @click="onReset" label="Cancel" type="reset" color="primary" flat class="q-ml-sm"/>-->
  <!--        </q-stepper-navigation>-->
  <!--      </q-step>-->

  <!--      <q-step :name="2" title="Preview" caption="Optional" icon="create_new_folder" :done="step > 2"-->
  <!--              :header-nav="step > 2">-->

  <!--        <div class="row q-col-gutter-md">-->
  <!--          <div class="col-3">-->
  <!--            <p>hola</p>-->

  <!--          </div>-->
  <!--          <div class="col-9">-->

  <!--            <p>adios</p>-->

  <!--          </div>-->

  <!--        </div>-->

  <!--        <q-stepper-navigation>-->
  <!--          <q-btn @click="() => { done2 = true; step = 3 }" color="primary" label="Continue"/>-->
  <!--          <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm"/>-->
  <!--        </q-stepper-navigation>-->
  <!--      </q-step>-->

  <!--      <q-step-->
  <!--        :name="3"-->
  <!--        title="Finish"-->
  <!--        icon="add_comment"-->
  <!--        :header-nav="step > 3"-->
  <!--      >-->
  <!--        Try out different ad text to see what brings in the most customers, and learn how to-->
  <!--        enhance your ads using features like ad extensions. If you run into any problems with-->
  <!--        your ads, find out how to tell if they're running and how to resolve approval issues.-->

  <!--        <q-stepper-navigation>-->
  <!--          <q-btn color="primary" @click="done3 = true" label="Finish"/>-->
  <!--          <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm"/>-->
  <!--        </q-stepper-navigation>-->
  <!--      </q-step>-->
  <!--    </q-stepper>-->
  <!--  </q-dialog>-->
</template>

<script >
import {defineComponent} from "vue";
import {odinApi} from "boot/axios";
import {ref} from 'vue'
import {QForm} from "quasar";
// import * as ODIN_Alerts from "components/ODIN_alerts";

export default defineComponent({
  name: "NewDataSourceForm",
  props: {
    show_dialog: {
      type: Boolean,
      default: false
    }
  },
  emits: {
    "close-dialog": null
  },
  setup() {
    // const DSForm = ref<QForm>(null)

    return {
      step: ref(1),
      bootstrappingType: ref('auto'),
      // DSForm
    }
  },
  data() {

    const options = [
      // "Avro",
      // "JSONFile",
      // "MongoDB",
      // "Neo4j",
      // "Parquet",
      // "RESTAPI",
      "SQLDatabase",
      "Upload file"
    ];

    const uploadedFile = new File([], "");
    const DataSourceType = "";

    const newDataSources = {
      id: "",
      name: "",
      type: ""
    };

    return {
      DataSourceType,
      options,
      newDataSources,
      uploadedFile,
    };
  },
  mounted() {
    this.DataSourceType =  "Upload file";
    // this.newDataSources.type = "Upload file";
  },
  methods: {

    // validate() {
    //
    //   (this.$refs.DSForm as QForm).validate().then(success => {
    //     if (success) {
    //       // yay, models are correct
    //       // this.onSubmit()
    //       (this.$refs.DSForm as any).resetValidation()
    //       console.log("completed")
    //
    //     } else {
    //       // oh no, user has filled in at least one invalid value
    //       console.log("validation failed")
    //     }
    //   })
    //
    // },

    onSubmit() {

      var data = new FormData();
      data.append("file", this.uploadedFile);


      data.append("dataSource", new Blob([JSON.stringify(this.newDataSources)], {
        type: "application/json"
      }));
      var btype = this.bootstrappingType == "auto"? true: false;
      data.append("bootstrappingType", new Blob([JSON.stringify(btype)], {
        type: "application/json"
      }));

      odinApi
        .post("/dataSource", data, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status == 201) {
            // console.log("ytesjkjk")
            // ODIN_Alerts.positiveAlert(`Data Source ${this.newDataSources.name} sucessfully created`)
            this.$q.notify({
              color: "positive",
              textColor: "white",
              icon: "check_circle",
              message: `Data Source ${this.newDataSources.name} sucessfully created`,
            });
            response.data.wrappers = 0;
            // this.rows.push(response.data);
            // this.show_dialog = false;


            this.step = 2;

            this.newDataSources.name = "";
            this.bootstrappingType = "auto";
            this.uploadedFile = new File([], "");

            this.$emit("close-dialog",{
              status: "created",
              data: response.data
            })
          } else {
            this.$q.notify({
              message: "Something went wrong in the server.",
              color: "negative",
              icon: "cancel",
              textColor: "white",
            });
          }
        })
    },

    onReset() {
      this.newDataSources.name = "";
      this.newDataSources.type = "";
      this.uploadedFile = new File([], "");
      this.bootstrappingType = "auto";
      this.$emit("close-dialog", {
        status: "cancel",
      })
    },

  },
});
</script>

<style lang="css" scoped>
</style>
