<template>
 <q-dialog v-model="showS" @hide="props.show=false">
 <!--  -->
   <q-card style="width: 400px; max-width: 80vw">
     <q-card-section>
       <div class="text-h6">Create new data source</div>
     </q-card-section>

     <q-card-section>

        <q-form ref="form" @submit="onSubmit" @reset="onReset" class="q-gutter-md">
          <q-input filled v-model="newDatasource.name" label="Introduce a data source name" lazy-rules
                   :rules="[(val) => (val && val.length > 0) || 'Please type a name', ]"/>
          <q-select v-model="DataSourceType" :options="options" label="Type" class="q-mt-none"/>

          
          <!-- <label class="fileBoxLabel"></label> -->


          <q-input v-model="newDatasource.description" filled autogrow label="Description (Optional)"/>

          <!-- <p>Select a bootstrapping type:</p>
          <q-option-group name="bootstrapping_type" v-model="bootstrappingType"
                          :options="[{label: 'Automatically', value: 'auto'},{label: 'Manual', value: 'manual'}]"
                          color="primary" inline/> -->

          <q-file ref="fileds" outlined v-model="uploadedFile" auto-expand label="Select the file you would like to import."
                  :headers="{ 'content-type': 'multipart/form-data' }" accept=".csv, application/json" :max-files="1"
                  lazy-rules :rules="[(val) => (val && val.name !== '') || 'Please upload a file' ]">
            <template v-slot:prepend>
              <q-icon name="attach_file" @click="this.$refs.fileds.pickFiles();"/>
            </template>


          </q-file>


          <!-- <q-file ref="fileds" v-model="uploadedFile" outlined  auto-expand label="File"
                  :headers="{ 'content-type': 'multipart/form-data' }" accept=".csv, application/json" :max-files="1"
                  lazy-rules :rules="[(val) => (val && val.name !== '') || 'Please upload a file' ]" class="fileBox">
            <template v-slot:prepend>
              <svg width="1em" height="1em" viewBox="0 0 24 20" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true"><path d="M21.569 2.398H7.829v1.586h13.74c.47 0 .826.5.826 1.094v9.853l-2.791-3.17a2.13 2.13 0 00-.74-.55 2.214 2.214 0 00-.912-.196 2.215 2.215 0 00-.912.191 2.131 2.131 0 00-.74.546l-2.93 3.385-2.973-3.36a2.147 2.147 0 00-.741-.545 2.228 2.228 0 00-1.824.007c-.286.13-.538.319-.739.553l-2.931 3.432V7.653H2.51v9.894c.023.153.06.304.108.452v.127l.041.095c.057.142.126.28.207.412l.099.15c.074.107.157.207.247.302l.124.119c.13.118.275.222.43.309h.024c.36.214.775.327 1.198.325h16.515c.36-.004.716-.085 1.039-.24.323-.153.606-.375.827-.648a2.78 2.78 0 00.504-.888c.066-.217.108-.44.124-.666V5.078a2.497 2.497 0 00-.652-1.81 2.706 2.706 0 00-1.776-.87z" fill="#32324D"></path><path d="M12.552 9.199c.912 0 1.651-.71 1.651-1.585 0-.876-.74-1.586-1.651-1.586-.912 0-1.652.71-1.652 1.586 0 .875.74 1.585 1.652 1.585zM3.303 6.408h.826V3.997h2.477v-.793-.793H4.129V0h-.826c-.219 0-.85.002-.826 0v2.411H0v1.586h2.477v2.41h.826z" fill="#32324D"></path></svg>
             
            </template>


          </q-file> -->

          <div v-if="showFormButtons" >
            <q-btn label="Submit" type="submit" color="primary"/>
            <q-btn label="Cancel" type="reset" color="primary" flat class="q-ml-sm" v-close-popup/>
          </div>
        </q-form>
     </q-card-section>
   </q-card>
 </q-dialog>

</template>

<script setup>
import {ref, reactive, onMounted, watch, computed} from "vue";
// import {odinApi} from "boot/axios";
import api from "src/api/dataSourcesAPI.js";
import {useNotify} from 'src/use/useNotify.js'
import { useRoute, useRouter } from "vue-router";
import { useDataSourceStore } from 'src/stores/datasources.store.js'
import { useIntegrationStore } from 'src/stores/integration.store.js'


// -------------------------------------------------------------
//                         PROPS & EMITS 
// -------------------------------------------------------------

const props = defineProps({
  show: {type:Boolean, default: false, required: true},
  showFormButtons: { type: Boolean, default: true },
  afterSubmitShowGraph : { type: Boolean, default: true },
});


const emit = defineEmits(["update:show"])
const showS = computed({
      get() { return props.show },
      set(newValue) { emit('update:show', newValue) }
    })

// -------------------------------------------------------------
//                         STORES & GLOBALS
// -------------------------------------------------------------
// const storeDS = useDataSourceStore()

const integrationStore = useIntegrationStore()

  onMounted( () => {
    // TODO: check if init is needed
    // storeDS.init()
    integrationStore.init()
   })

const route = useRoute()
const router = useRouter()
// -------------------------------------------------------------
//                         Others
// -------------------------------------------------------------

const form = ref(null)
const notify = useNotify()

defineExpose({
  form
})

const options = [
      "SQLDatabase", "Upload file"
];



  const newDatasource = reactive({
    name: '',
    description : '',
  })

 
    const uploadedFile  = ref(null);
    const DataSourceType = ref("Upload file");
    // const newDataSources = ref({id: "", name: "", type: "", description:"",tags:""});
    // const bootstrappingType = ref('auto');
    // const description = ref("");
    const onReset = () => {

      // emit("update:prueb", 2)
      // console.log(props.show)
      // props.show = !props.show
      // console.log("resettting.d..")

      // newDataSources.value.name = "";
      // newDataSources.value.type = "";
      // newDataSources.value.description = "";
      // newDataSources.value.tags = "";
      uploadedFile.value = null;
      // bootstrappingType.value = "auto";

      // props.show = false

    }

    const onSubmit = () => {
      var data = new FormData();
      data.append("file", uploadedFile.value);

      data.append("dataSource", new Blob([JSON.stringify(newDatasource)], {
        type: "application/json"
      }))
 

      integrationStore.addDataSource(route.params.id, data, successCallback)
    }

const successCallback = (datasource) => {

  console.log("success callback")

                  notify.positive(`Data Source ${datasource.name} successfully uploaded`)
                  onReset()
                  form.value.resetValidation()
	
              		showS.value = false;

                  integrationStore.addSelectedDatasource(datasource)

                if(props.afterSubmitShowGraph)
                  router.push({name:'dsIntegration'})
                
  }



 
</script>

<style lang="scss">
.fileBoxLabel{

  margin: 0px;
    padding: 0px;
    border: 0px;
    font: inherit;
  vertical-align: baseline;



}

.fileBox{

  .q-field__control{
    height: 150px;
  }

  .q-field__control:before{
    border: 1px dashed #A4A4A4;
  }


}

.fileUploadBox{

  position: absolute;
    z-index: 1;
    box-sizing: border-box;
    display: table;
    table-layout: fixed;
    width: 100px;
    height: 80px;
    top: 86px;
    left: 100px;
    border: 1px dashed #A4A4A4;
    border-radius: 3px;
    text-align: center;
    overflow: hidden;


    .contentFile{

          display: table-cell;
      vertical-align: middle;



    }

    input{

      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      opacity: 0;


    }


}


</style>
