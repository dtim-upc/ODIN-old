<template>
<!--  <q-dialog v-model="show" persistent>-->
<!--    <q-card style="width: 700px; max-width: 80vw">-->
<!--      <q-card-section>-->
<!--        <div class="text-h6">Create new data source</div>-->
<!--      </q-card-section>-->

<!--      <q-card-section>-->

        <q-form ref="form" @submit="onSubmit" @reset="onReset" class="q-gutter-md">
          <q-input filled v-model="newDataSources.name" label="Introduce a data source name" lazy-rules
                   :rules="[(val) => (val && val.length > 0) || 'Please type a name', ]"/>
          <q-select v-model="DataSourceType" :options="options" label="Type" class="q-mt-none"/>

          <q-file ref="fileds" outlined v-model="uploadedFile" auto-expand label="Select the file you would like to import."
                  :headers="{ 'content-type': 'multipart/form-data' }" accept="text/csv, application/json" :max-files="1"
                  lazy-rules :rules="[(val) => (val && val.name !== '') || 'Please upload a file' ]">
            <template v-slot:prepend>
              <q-icon name="attach_file" @click="this.$refs.fileds.pickFiles();"/>
            </template>


          </q-file>

          <p>Select a bootstrapping type:</p>
          <q-option-group name="bootstrapping_type" v-model="bootstrappingType"
                          :options="[{label: 'Automatically', value: 'auto'},{label: 'Manual', value: 'manual'}]"
                          color="primary" inline/>
          <div v-if="showFormButtons" >
            <q-btn label="Submit" type="submit" color="primary"/>
            <q-btn label="Cancel" type="reset" color="primary" flat class="q-ml-sm"/>
          </div>
        </q-form>
<!--      </q-card-section>-->
<!--    </q-card>-->
<!--  </q-dialog>-->

</template>

<script>
import {defineComponent, ref} from "vue";
import {odinApi} from "boot/axios";
import notify from "components/hooks/notify";
import {useStore} from "vuex";

export default defineComponent({
  name: "NewDataSourceForm",
  props: {
    showFormButtons: {type: Boolean, default: true},
  },
  emits: ["submit-sucess"],
  setup(props , {emit}) {

    const store = useStore()
    const form = ref(null)

    const options = [
      // "Avro",
      // "JSONFile",
      // "MongoDB",
      // "Neo4j",
      // "Parquet",
      // "RESTAPI",
      "SQLDatabase", "Upload file"
    ];
    const uploadedFile  = ref(null);
    const DataSourceType = ref("Upload file");
    const newDataSources = ref({id: "", name: "", type: ""});
    const bootstrappingType = ref('auto');
    const onReset = () => {

      // emit("update:prueb", 2)
      // console.log(props.show)
      // props.show = !props.show
      // console.log("resettting.d..")

      newDataSources.value.name = "";
      newDataSources.value.type = "";
      uploadedFile.value = null;
      bootstrappingType.value = "auto";

      // props.show = false

    }

    const onSubmit = () => {
      var data = new FormData();
      // console.log(uploadedFile)
      data.append("file", uploadedFile.value);
      // console.log(uploadedFile.value)
      newDataSources.value.type = "UNKNOWN"
      data.append("dataSource", new Blob([JSON.stringify(newDataSources.value)], {
        type: "application/json"
      }));
      // console.log(newDataSources.value)
      var btype = bootstrappingType.value == "auto" ? true : false;
      data.append("bootstrappingType", new Blob([JSON.stringify(btype)], {
        type: "application/json"
      }));

      odinApi.post("/dataSource", data, {headers: {"Content-Type": "multipart/form-data"},})
        .then((response) => {
          if (response.status == 201) {

            notify.positive(`Data Source ${newDataSources.value.name} successfully created`)
            onReset()
            form.value.resetValidation()
            // response.data.wrappers = 0;
            // console.log(response.data)



            let data = {ds: response.data, bootstrapping: btype }


            store.dispatch("addDatasource", response.data)
            emit("submit-sucess", data)
          } else {
            // console.log("error")
            notify.negative("Cannot create datasource. Something went wrong in the server.")
          }
        }).catch( (error) => {
        notify.negative("Something went wrong in the server.")
      });
    }




    return {
      DataSourceType, options, newDataSources, uploadedFile, bootstrappingType, onSubmit, onReset, form
    }
  },
  //
  // mounted() {
  //   this.DataSourceType =;
  //   this.newDataSources.type = "Upload file";
  // },
  // methods: {
  //
  //   // validate() {
  //   //
  //   //   (this.$refs.DSForm as QForm).validate().then(success => {
  //   //     if (success) {
  //   //       // yay, models are correct
  //   //       // this.onSubmit()
  //   //       (this.$refs.DSForm as any).resetValidation()
  //   //       console.log("completed")
  //   //
  //   //     } else {
  //   //       // oh no, user has filled in at least one invalid value
  //   //       console.log("validation failed")
  //   //     }
  //   //   })
  //   //
  //   // },
  //
  //
  //
  //
  // },
});
</script>

<style lang="css" scoped>
</style>
