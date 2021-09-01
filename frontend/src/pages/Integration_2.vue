<!--<template>-->
<!--  <q-page>-->

<!--    <q-btn label="Medium" color="primary" @click="medium = true"/>-->


<!--    <q-dialog v-model="medium" full-height full-width>-->







<!--      &lt;!&ndash;      <q-card-section class="row items-center q-pb-none">&ndash;&gt;-->
<!--      &lt;!&ndash;        <div class="text-h6">Close icon</div>&ndash;&gt;-->
<!--      &lt;!&ndash;        <q-space />&ndash;&gt;-->
<!--      &lt;!&ndash;        <q-btn icon="close" flat round dense v-close-popup />&ndash;&gt;-->
<!--      &lt;!&ndash;      </q-card-section>&ndash;&gt;-->


<!--      <q-card flat bordered>-->
<!--        &lt;!&ndash;        <q-item>&ndash;&gt;-->
<!--        &lt;!&ndash;          <q-item-section avatar>&ndash;&gt;-->
<!--        &lt;!&ndash;            <q-avatar>&ndash;&gt;-->
<!--        &lt;!&ndash;              <img src="https://cdn.quasar.dev/img/boy-avatar.png">&ndash;&gt;-->
<!--        &lt;!&ndash;            </q-avatar>&ndash;&gt;-->
<!--        &lt;!&ndash;          </q-item-section>&ndash;&gt;-->

<!--        &lt;!&ndash;          <q-item-section>&ndash;&gt;-->
<!--        &lt;!&ndash;            <q-item-label>Create datasource</q-item-label>&ndash;&gt;-->
<!--        &lt;!&ndash;            <q-item-label caption>&ndash;&gt;-->
<!--        &lt;!&ndash;              Subhead&ndash;&gt;-->
<!--        &lt;!&ndash;            </q-item-label>&ndash;&gt;-->
<!--        &lt;!&ndash;          </q-item-section>&ndash;&gt;-->
<!--        &lt;!&ndash;        </q-item>&ndash;&gt;-->

<!--        &lt;!&ndash;        <q-separator/>&ndash;&gt;-->

<!--        <q-card-section class="row no-padding">-->
<!--          <q-card-section class="col-8 no-padding">-->

<!--            <div class=" row q-pb-none">-->


<!--              <q-banner  dense inline-actions class="text-white bg-red col-12" :hidden="alertAlignmentType">-->
<!--                Incompatible types. Please select another element with the same type.-->
<!--              </q-banner>-->


<!--              <q-card-section class="col">-->
<!--                <div class="row bg-primary justify-center text-white">{{ datasourceA.name }}</div>-->
<!--                <q-responsive :ratio="4/3">-->
<!--                  <Webvowl :view="'bdi_manual_alignments'" :id="datasourceA.id"/>-->
<!--                </q-responsive>-->
<!--                <div class="q-pt-md">-->

<!--                  <div class="q-gutter-md">-->

<!--                    <q-input outlined v-model="selectedA_baseIri" prefix="Namespace: " disable dense/>-->

<!--                    <q-input outlined v-model="selectedA_label" prefix="Label: " :suffix="selectedA_type"-->
<!--                             disable dense/>-->
<!--                  </div>-->

<!--                </div>-->
<!--              </q-card-section>-->

<!--              <q-card-section class="col q-pl-none">-->
<!--                <div class="row bg-primary justify-center text-white">{{ datasourceB.name }}</div>-->
<!--                <q-responsive :ratio="4/3">-->
<!--                  <Webvowl :view="'bdi_manual_alignments'" :id="datasourceB.id"/>-->
<!--                </q-responsive>-->

<!--                <div class="q-pt-md">-->
<!--                  <div class="q-gutter-md">-->

<!--                    <q-input outlined v-model="selectedB_baseIri" prefix="Namespace: " disable dense/>-->
<!--                    <q-input outlined v-model="selectedB_label" prefix="Label:" :suffix="selectedB_type"-->
<!--                             disable dense/>-->
<!--                  </div>-->

<!--                </div>-->
<!--              </q-card-section>-->


<!--            </div>-->
<!--            <q-card-section class="row q-pt-none">-->
<!--              <div >-->
<!--                <q-input outlined v-model="integratedLabel" prefix="Integrated label: " dense/>-->
<!--                <q-btn outline color="primary" label="Add alignment" />-->
<!--              </div>-->
<!--            </q-card-section>-->
<!--          </q-card-section>-->

<!--          <q-card-section class="bg-grey-4 col-4">-->
<!--            <TableAlignments />-->
<!--          </q-card-section>-->

<!--        </q-card-section>-->

<!--      </q-card>-->


<!--    </q-dialog>-->

<!--  </q-page>-->
<!--</template>-->

<!--<script >-->

<!--import {defineComponent} from "vue";-->
<!--import {ref} from 'vue'-->
<!--import TableAlignments from "components/tables/TableAlignments"-->
<!--import Webvowl from "components/graph/Webvowl.vue";-->
<!--// uncomment next line if you're making calls to ODIN API-->
<!--// import { odinApi } from "boot/axios";-->

<!--export default defineComponent({-->
<!--  name: "Template",-->
<!--  components: {TableAlignments, Webvowl},-->
<!--  setup() {-->

<!--    const datasourceA: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name1', type: ''};-->
<!--    const datasourceB: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name2', type: ''};-->
<!--    // const selectedA = {baseIri:'', iri:'a', type: '',  label: 'a'}-->
<!--    // const selectedB = {baseIri:'', iri:'', type: '',  label: ''}-->

<!--    return {-->
<!--      medium: ref(false),-->

<!--      datasourceA,-->
<!--      datasourceB,-->
<!--      selectedA_label: ref(''),-->
<!--      selectedA_type: ref(''),-->
<!--      selectedA_baseIri: ref(''),-->
<!--      selectedA_iri: ref(''),-->
<!--      selectedB_label: ref(''),-->
<!--      selectedB_type: ref(''),-->
<!--      selectedB_baseIri: ref(''),-->
<!--      selectedB_iri: ref(''),-->
<!--      integratedLabel: ref(''),-->
<!--      alertAlignmentType: ref(true)-->
<!--      // datasourceId: "612c3940a4e8f763dbf907a2",-->
<!--      // datasourceId: "612c3940a4e8f763dbf907a2",-->
<!--      // selectedA2: ref(''),-->
<!--      // selectedURIB: ref('')-->

<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    addHandler() {-->
<!--      window.addEventListener('clickEle_msg', this.selectAlignment);-->
<!--    },-->
<!--    removeHandler() {-->
<!--      window.removeEventListener('clickEle_msg', this.selectAlignment);-->
<!--    },-->
<!--    selectAlignment: function (event) {-->
<!--      console.log("listener");-->
<!--      console.log(event.detail)-->


<!--      if (event.detail.id == this.datasourceA.id) {-->
<!--        if (event.detail.isSelected) {-->

<!--          if (this.selectedB_type == event.detail.type || this.selectedB_type == "") {-->

<!--            this.selectedA_label = event.detail.label;-->
<!--            this.selectedA_type = event.detail.type;-->
<!--            this.selectedA_baseIri = event.detail.baseIri;-->
<!--            this.selectedA_iri = event.detail.iri;-->
<!--            this.alertAlignmentType = false;-->
<!--          } else {-->
<!--            this.alertAlignmentType = true;-->
<!--            // show alert about type incompatibility-->
<!--          }-->
<!--        }-->
<!--      } else {-->
<!--        if (event.detail.isSelected) {-->
<!--          if (this.selectedA_type == event.detail.type || this.selectedA_type == "") {-->

<!--            this.selectedB_label = event.detail.label;-->
<!--            this.selectedB_type = event.detail.type;-->
<!--            this.selectedB_baseIri = event.detail.baseIri;-->
<!--            this.selectedB_iri = event.detail.iri;-->
<!--            this.alertAlignmentType = false;-->
<!--          } else {-->
<!--            this.alertAlignmentType = true;-->
<!--          }-->
<!--        }-->
<!--      }-->

<!--      if(this.selectedA_label & this.selectedB_label){-->
<!--        this.integratedLabel = this.selectedA_label +"_"+this.selectedB_label;-->
<!--      } else {-->
<!--        this.integratedLabel = ""-->
<!--      }-->

<!--    }-->
<!--  },-->
<!--  mounted: function () {-->
<!--    this.addHandler();-->
<!--  },-->
<!--  destroyed() {-->
<!--    this.removeHandler();-->
<!--  }-->

<!--});-->
<!--</script>-->

<!--<style lang="css" scoped>-->


<!--</style>-->
