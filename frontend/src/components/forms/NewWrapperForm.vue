<template>

  <!--  <q-dialog v-model="show" persistent>-->
  <!--    <q-card style="width: 700px; max-width: 80vw">-->
  <!--      <q-card-section>-->
  <!--        <div class="text-h6">Create new wrapper</div>-->
  <!--      </q-card-section>-->

  <!--      <q-card-section>-->
  <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md" ref="form">
    <q-input filled v-model="newWrapper.name" label="Introduce a wrapper name" lazy-rules
             :rules="[(val) => (val && val.length > 0) || 'Please type a name', ]"/>
    <q-select filled :disable="datasource.name != ''" :options="datasources" option-label="name"
              v-model="selectedDatasource"  @update:model-value="updateDS" label="Selected data source"
              :rules="[(val) => !!val || 'Field is required']" emit-value map-options/>

    <q-select filled v-model="newWrapper.attributes" label="Select attributes or add new ones" use-input use-chips multiple
              input-debounce="0" @new-value="addAttribute" :loading="loadingAttr"
              :options="attributes" option-label="name" @filter="filterFn">
      <template v-slot:no-option>
        <q-item>
          <q-item-section class="text-grey">
            No results
          </q-item-section>
        </q-item>
      </template>


      <template v-slot:loading>
        <q-spinner-gears color="primary" size="1em"/>
      </template>
    </q-select>

    <div v-if="showFormButtons">
      <q-btn label="Submit" type="submit" color="primary"/>
      <q-btn label="Cancel" type="reset" color="primary" flat class="q-ml-sm"/>
    </div>
  </q-form>

</template>

<script>

import {defineComponent, ref, onMounted, computed} from "vue";
import {odinApi} from "boot/axios";
import {useStore} from "vuex";
import notify from "components/hooks/notify";

export default defineComponent({

  name: "NewWrapperForm",
  props: {
    showFormButtons: {type: Boolean, default: false},
    // setDS: {type: Boolean, default: false },
    datasource: {type: Object, default: {id: "", name: "", type: ""}}
  },
  emits: ["submit-sucess"],
  setup(props, {emit}) {
    const store = useStore()
    const form = ref(null)
    const selectedDatasource = ref({id: "", name: "", type: ""})
    const newWrapper = ref({id: "", name: "", attributes: []});
    const inferSchemaAtt = ref( [] ) // {isID: false, name: nameAtt}
    const attributes = ref(null) // inferschema

    const loadingAttr = ref(false)


    const datasources = computed(() => store.state.datasource.datasources)

    if(props.datasource.id != "" && props.datasource.name != "") {

      selectedDatasource.value = props.datasource;

    }

    const onReset = () => {



      if(props.datasource.id) {
        attributes.value = []
      }

      newWrapper.value = {id: "", name: "", attributes: []}
      emit("update:show", false)

    }

    const onSubmit = () => {
      if (newWrapper.value.attributes.length === 0) {
        notify.negative("Please add at least 1 attribute")
      } else {

        newWrapper.value.dataSourcesId = selectedDatasource.value.id


        // console.log(newWrapper.value)
        odinApi.post("/wrapper", newWrapper.value).then((response) => {
          console.log(response)
          if (response.status == 201) {
            notify.positive(`Wrapper ${newWrapper.value.name} successfully created`)

            onReset();
            form.value.resetValidation()

            emit("submit-sucess")
          } else {
            notify.negative("Something went wrong in the server.")
          }
        }).catch( (error) => {
          notify.negative("Something went wrong in the server.")
        });
      }
      //
    }


    const addAttribute = (val, done) => {

      if (val.length > 0) {
        console.log(inferSchemaAtt.value)
        let newVal = {isID: false, name: val}
        if (!inferSchemaAtt.value.includes(newVal)) {
          inferSchemaAtt.value.push(newVal)
        }
        done(val, 'toggle')
      }
    }
    const filterFn = (val, update) => {


      // if (attributes.value !== null) {

        update(() => {
          if (val === '') {
            attributes.value = inferSchemaAtt.value
          } else {
            const needle = val.toLowerCase()

            attributes.value = inferSchemaAtt.value.filter(
              v => v.toLowerCase().indexOf(needle) > -1
            )
          }
        })

      // } else {

        // odinApi.get("/wrapper/inferschema", {
        //     params: { dataSourceId: props.datasource.id },
        //   }).then((response) => {
        //     console.log("retrieving data...")
        //     console.log(response.data);
        //     // this.currentInferredDataSourceId = this.newWrapper.dataSourcesId;
        //     // for (const at of response.data) {
        //     //   const obj = {
        //     //     isID: false,
        //     //     name: at,
        //     //   };
        //     //   this.newWrapper.attributes.push(obj);
        //     // }
        //     // this.inferring_schema = false;
        //   inferSchemaAtt.value.id = props.datasource.id
        //   inferSchemaAtt.value = response.data
        //
        //   update(() => {
        //     attributes.value = inferSchemaAtt.value
        //     newWrapper.value.attributes = inferSchemaAtt.value
        //     // attrM.value = inferSchemaAtt.value
        //   })
        //
        //   });

      // }
    }



    const inferAttAPI = () => {

        loadingAttr.value = true

        odinApi.get("/wrapper/inferschema", {
          params: { dataSourceId: selectedDatasource.value.id },
        }).then((response) => {


          if (response.status == 201 || response.status == 200) {


            // console.log("retrieving data...")
            // console.log(response.data);

            // inferSchemaAtt.value.id = selectedDatasource.value.id
            inferSchemaAtt.value = response.data.map(x => ( {isID: false, name:x} ))
            // console.log(inferSchemaAtt.value)

            attributes.value = inferSchemaAtt.value
            newWrapper.value.attributes = inferSchemaAtt.value

            loadingAttr.value = false
          } else {



          }



        }).catch( (error) => {

          notify.negative("Cannot infer schema. Something went wrong in the server.")

          });
    }

    const updateDS = (evt) => {
      inferAttAPI()
    }

    onMounted( () => {

      if(selectedDatasource.value.id) {
        inferAttAPI()
      }

      if (datasources.value.length == 0) {
        store.dispatch("getDatasources")
      }
    })

    return {newWrapper, onSubmit, form, updateDS, selectedDatasource,datasources, loadingAttr,onReset, addAttribute, attributes, filterFn}


  }


})

</script>
