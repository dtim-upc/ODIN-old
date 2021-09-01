<template>
  <q-page>

<!--    <q-btn label="Medium" color="primary" @click="medium = true"/>-->
    <div class="q-pa-lg">
    <q-stepper v-model="step" ref="stepper" color="primary" animated class="no-padding-stepper">
      <q-step :name="1" title="Select data sources" icon="settings" :done="step > 1">
        For each ad campaign that you create, you can control how much you're willing to
        spend on clicks and conversions, which networks and geographical locations you want
        your ads to show on, and more.

        <TableDataSourcesAndIntegrations :no_shadow="true" @selected="setSelected" />
      </q-step>

      <q-step :name="2" title="Alignments" caption="Optional" icon="create_new_folder" :done="step > 2">
        <TableAlignments :ds-a="datasourceA" :ds-b="datasourceB" :no_shadow="true" @alignments="setAlignments"/>
      </q-step>


      <q-step :name="3" title="Result" icon="settings" >
        For each ad campaign that you create, you can control how much you're willing to
        spend on clicks and conversions, which networks and geographical locations you want
        your ads to show on, and more.
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

import {defineComponent} from "vue";
import {ref} from 'vue'
import TableAlignments from "components/tables/TableAlignments.vue"
import TableDataSourcesAndIntegrations from 'components/tables/integration/TableDataSourcesAndIntegrations.vue';

import { odinApi } from "boot/axios";

export default defineComponent({
  name: "Template",
  components: {TableAlignments, TableDataSourcesAndIntegrations},
  setup() {

    const datasourceA = {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""};
    const datasourceB = {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""};
    // const selectedA = {baseIri:'', iri:'a', type: '',  label: 'a'}
    // const selectedB = {baseIri:'', iri:'', type: '',  label: ''}
    const selected = ref([])
    const alignments = ref([])
    return {
      medium: ref(false),

      datasourceA,
      datasourceB,
      selected,
      alignments,
      step: ref(1)
      // datasourceId: "612c3940a4e8f763dbf907a2",
      // datasourceId: "612c3940a4e8f763dbf907a2",
      // selectedA2: ref(''),
      // selectedURIB: ref('')

    }
  },
  methods: {
    setAlignments(props){
      if(props.data){
        this.alignments = props.data;
      }
    },
    setSelected(props){
      if(props.data){
        this.selected = props.data
      }
    },
    stepLabel(){
      switch (this.step) {
        case 2:
          return "Integrate";
        case 3:
          return "Finish"
          break;
        default:
          return "Continue"
          break;
      }
      // switch ()
      // step === 3 ? 'Finish' : 'Continue'
    },
    clickOk(){
      // $refs.stepper.next()
      switch (this.step){
        case 1:
          this.datasourceA = this.selected[0]
          this.datasourceB = this.selected[1]
          this.step = this.step + 1
          break;
        case 2:

          var data = {
            dsA: this.datasourceA,
            dsB: this.datasourceB,
            alignments: this.alignments
          }


          odinApi.post("/integration", data).then((response) => {
            if (response.status == 201) {
              // this.$q.notify({
              //   color: "positive",
              //   textColor: "white",
              //   icon: "check_circle",
              //   message: `Global graph ${this.newGlobalGraph.name} sucessfully created`,
              // });
              // this.rows.push(response.data);
              // this.show_dialog = false;
            } else {
              this.$q.notify({
                message: "Something went wrong in the server.",
                color: "negative",
                icon: "cancel",
                textColor: "white",
              });
            }
          });


          // this.step = this.step + 1
          return false;
          break;
        case 3:
          return false;
          break;
        default:
          // this.step = 1;
          return false;
      }

    },
    disableStepBtn(){
      switch (this.step){
        case 1:
          return this.selected.filter(v => (v.graphicalGraph ) ).length != 2
          break;
        case 2:
          return false;
          break;
        case 3:
          return false;
          break;
        default:
          // this.step = 1;
          return false;
      }
    }
  }


});
</script>

<style lang="css" >
/*.no-padding-stepper .q-stepper__tab:first-child {*/
/*  padding: 0;*/
/*}*/
/*.no-padding-stepper .q-stepper__tab:last-child {*/
/*  padding: 0;*/
/*}*/
/*.no-padding-stepper .q-stepper__step-inner,*/
/*.no-padding-stepper .q-stepper__nav {*/
/*  padding: 0;*/

/*}*/
/*.no-padding-stepper .q-stepper__step-inner{*/
/*  padding: 0;*/

/*}*/

</style>
