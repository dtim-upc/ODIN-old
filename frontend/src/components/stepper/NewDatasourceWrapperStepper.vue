<template>

  <q-stepper v-model="step" ref="stepper" color="primary" animated>
    <q-step :name="1" title="Create datasource" icon="settings" :done="step > 1">
      <NewDataSourceForm ref="newDSForm" :showFormButtons="false" @submit-sucess="submitted" />
    </q-step>

    <q-step :name="2" title="Preview" icon="create_new_folder" :done="step > 2">
      <PreviewCSV/>
      <!-- <PreviewJSON/> -->
    </q-step>

    <!-- <q-step :name="3" title="Create view" icon="assignment"  >
      <NewWrapperForm ref="newWForm"  :datasource="datasource" @submit-sucess="submitted" />
    </q-step> -->

    <template v-slot:navigation>
      <q-stepper-navigation>
        <q-btn @click="clickOK" color="primary" :label="step === 2 ? 'Confirm' : 'Continue'"/>
<!--        <q-btn v-if="step > 1" flat color="primary" @click="$refs.stepper.previous()" label="Back" class="q-ml-sm"/>-->
      </q-stepper-navigation>
    </template>
  </q-stepper>


</template>


<script>


import {defineComponent, ref, onMounted} from "vue";
import NewDataSourceForm from "components/forms/NewDataSourceForm";
import NewWrapperForm from "components/forms/NewWrapperForm";
import PreviewCSV from "./PreviewCSV.vue";
import PreviewJSON from "./PreviewJSON.vue";
import {odinApi} from "boot/axios";
import notify from "components/hooks/notify";

export default defineComponent({

  name: "NewDataSourceWrapperStepper",
  components: { NewWrapperForm, NewDataSourceForm, PreviewCSV, PreviewJSON, PreviewCSV },
  emits: ["finished"],
  setup (props,{emit}) {

    const step = ref(1)
    const newDSForm = ref(null);
    const newWForm = ref(null);
    const datasource = ref({id:"", name:"", type:""} )
    // onMounted(() => {
    //   console.log("newDSForm", newDSForm.value.form);
    // });

    const clickOK = () => {

      switch (step.value){

        case 1:
          // step.value++
          // newDSForm.value.form.onSubmit()
          newDSForm.value.form.submit()
          break;
        case 2:

        console.log("confirmed")
        console.log(datasource.value)    
        odinApi.post("/dataSource/persist", datasource.value)
        .then((response) => {
          if (response.status == 201) {
            notify.positive(`Data Source ${datasource.value.name} successfully created`)
          } else {
            // console.log("error")
            notify.negative("Cannot create datasource. Something went wrong in the server.")
          }
        }).catch( (error) => {
        notify.negative("Something went wrong in the frontend.")
      });
      emit("finished")

          break;
        // case 3:
        //   console.log(newWForm.value)
        //   newWForm.value.form.submit()
        //   console.log("wrapper submitter");

        //   break;

        default:

      }

    }

    const submitted = (ds) => {


      switch (step.value) {

        case 1:

          if(ds) {
            console.log("submitted success.......")
            console.log(ds)

            if(ds.bootstrapping){

              // emit("finished")
              datasource.value = ds.ds
              step.value = 2;
            } else  {
              datasource.value = ds.ds
              step.value = 3;
            }
          }
          break;
        case 2:
          break;

        // case 3:

        //   console.log("submitted success case 3")
        //   emit("finished")

        //   break;

        default:

      }



    }
    return {
      step, clickOK, newDSForm, newWForm, datasource, submitted
    }
  }

})


</script>
