<template>
  <q-page>

    <div class="q-pa-lg">
    <q-stepper v-model="step" ref="stepper" color="primary" animated class="no-padding-stepper">
      <q-step :name="1" title="Select data sources" icon="settings" :done="step > 1">
        For each ad campaign that you create, you can control how much you're willing to
        spend on clicks and conversions, which networks and geographical locations you want
        your ads to show on, and more.

        <TableDataSourcesAndIntegrations :no_shadow="true" />
      </q-step>

      <q-step :name="2" title="Alignments" caption="Optional" icon="create_new_folder" :done="step > 2">
        <TableAlignments :no_shadow="true" :alignments.sync="alignments"/>
      </q-step>


      <q-step :name="3" title="Result" icon="settings" >
        <q-responsive :ratio="4/3" style="max-height: 53vh">
          <Webvowl view="source_graph" :id="integratedId"/>
        </q-responsive>

      </q-step>

      <template v-slot:navigation>
        <q-stepper-navigation>
          <q-btn @click="clickOk" :disable="disableStepBtn()" color="primary" :label="stepLabel()"/>
          <q-btn v-if="step > 1" flat color="primary" @click="$refs.stepper.previous()" label="Back" class="q-ml-sm"/>
        </q-stepper-navigation>
      </template>
    </q-stepper>
    </div>


  </q-page>
</template>

<script >

import {computed, defineComponent, onMounted} from "vue";
import {ref} from 'vue'
import TableAlignments from "components/tables/TableAlignments.vue"
import TableDataSourcesAndIntegrations from 'components/tables/integration/TableDataSourcesAndIntegrations.vue';
import Webvowl from 'components/graph/Webvowl.vue';

import { odinApi } from "boot/axios";
import {useStore, mapActions} from "vuex";
import notify from 'components/hooks/notify';

export default defineComponent({
  name: "Template",
  components: {TableAlignments, TableDataSourcesAndIntegrations, Webvowl},

  setup() {
    const store = useStore()
    const integratedId = ref("")

    const selectedDS = computed(() => store.state.datasource.selectedDatasources)

    // const datasourceA = {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""};
    // const datasourceB = {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""};

    const alignments = ref([])


    const step = ref(1)
    const disableStepBtn = () =>{
      switch (step.value){
        case 1:
          return selectedDS.value.filter(v => (v.graphicalGraph ) ).length != 2
          // return false;
          break;
        case 2:
          return alignments.value.length == 0
          break;
        case 3:
          return false;
          break;
        default:
          // this.step = 1;
          return false;
      }
    }

    const stepLabel = () =>{
      switch (step.value) {
        case 2:
          return "Integrate";
        case 3:
          return "Finish"
          break;
        default:
          return "Continue"
          break;
      }
    }
    const clickOk = () => {
      switch (step.value){
        case 1:
          // datasourceA = selected[0]
          // datasourceB = selected[1]
          step.value++
          break;
        case 2:

          var data = {
            dsA: selectedDS.value[0],
            dsB: selectedDS.value[0],
            alignments: alignments.value
          }

          console.log(alignments.value)

          odinApi.post("/integration", data).then((response) => {
            if (response.status == 201 || response.status) {
              notify.positive("Integration succeeded")
              // console.log(response.data)
              integratedId.value = response.data.id
                step.value++
            } else {
              notify.negative("There was an error for the integration task")
            }
          });


          return false;
          break;
        case 3:
          return false;
          break;
        default:
          // step = 1;
          return false;
      }

    }

    return {
      disableStepBtn,
        stepLabel,
      integratedId,
      // datasourceA,
      // datasourceB,
      selectedDS,
      clickOk,
      alignments,
      step,
    }
  },


});
</script>

<style >


</style>
