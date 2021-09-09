<template>
  <q-dialog v-model="show_dialog" full-height full-width>

    <q-card flat bordered>
      <q-item class="q-py-none">

        <q-item-section>
          <q-item-label>Select alignments</q-item-label>
        </q-item-section>

        <q-card-section class="row items-center q-pb-none">
          <q-space/>
          <q-btn icon="close" flat round dense v-close-popup @click="close"/>
        </q-card-section>
      </q-item>

      <q-separator/>

      <q-card-section class="row no-padding">
        <q-card-section class="col-12 no-padding">

          <div class=" row q-pb-none">

            <q-banner dense inline-actions class="text-white bg-red col-12" :class="{ hidden: !alertAlignmentType }">
              Incompatible types. Please select another element with the same type.
            </q-banner>


            <q-card-section class="col">
              <div class="row bg-primary justify-center text-white q-pa-xs">
                {{ dsA.name }}
              </div>
              <q-responsive :ratio="4/3" style="max-height: 53vh">
                <Webvowl :view="'bdi_manual_alignments'" :id="dsA.id" :minimal-i="dsA.type == 'INTEGRATED'? true: false"/>
              </q-responsive>
              <div class="q-pt-md">

                <div class="q-gutter-md">

                  <q-input outlined v-model="selectedA_baseIri" prefix="Namespace: " disable dense/>

                  <q-input outlined v-model="selectedA_label" prefix="Label: " :suffix="selectedA_type" disable dense/>
                </div>

              </div>
            </q-card-section>

            <q-card-section class="col q-pl-none">
              <div class="row bg-primary justify-center text-white q-pa-xs">  {{ dsB.name }}</div>
              <q-responsive :ratio="4/3" style="max-height: 53vh">
                <Webvowl :view="'bdi_manual_alignments'" :id="dsB.id"   :minimal-i="dsB.type == 'INTEGRATED'? true: false"/>
              </q-responsive>

              <div class="q-pt-md">
                <div class="q-gutter-md">

                  <q-input outlined v-model="selectedB_baseIri" prefix="Namespace: " disable dense/>
                  <q-input outlined v-model="selectedB_label" prefix="Label:" :suffix="selectedB_type" disable dense/>
                </div>

              </div>
            </q-card-section>


          </div>
          <q-card-section class="row justify-between q-pt-none">
            <!--                  <div>-->
            <div class="col-3">
              <q-input outlined v-model="integratedLabel" prefix="Integrated label: " dense/>
            </div>
            <div class="col-3">
              <q-btn outline color="primary" label="Add alignment" @click="addAlignment" :disable="disableAdd"/>
              <q-btn outline color="primary" label="Close" @click="close"/>
            </div>

          </q-card-section>
        </q-card-section>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import {defineComponent, ref} from "vue";

import Webvowl from "components/graph/Webvowl.vue";

export default defineComponent({
  name: "SelectAlignments",
  components: {Webvowl},
  setup() {

    return {
      selectedA_label: ref(""),
      selectedA_type: ref(""),
      selectedA_baseIri: ref(""),
      selectedA_iri: ref(""),
      selectedB_label: ref(""),
      selectedB_type: ref(""),
      selectedB_baseIri: ref(""),
      selectedB_iri: ref(""),
      integratedLabel: ref(""),
      alertAlignmentType: ref(false),
      fullscreen: ref(false),
      disableAdd: ref(true),
      // toggle (e) {
      //   console.log(e)
      //   const target = e.target.parentNode.parentNode.parentNode.parentNode
      //
      //   $q.fullscreen.toggle(target)
      //     .then(() => {
      //       // success!
      //     })
      //     .catch((err) => {
      //       alert(err)
      //       // uh, oh, error!!
      //       // console.error(err)
      //     })
      // }
    }
  },
  props: {
    dsA: {type: Object, default: {id: "", name: "", type: "", graphicalGraph: "", iri: "", path: ""}},
    dsB: {type: Object, default: {id: "", name: "", type: "", graphicalGraph: "", iri: "", path: ""}},
    show_dialog: {type: Boolean, default: false}
  },
  emits: {
    "close-dialog": null,
    "add-alignment": null
  },
  methods: {
    close() {
      this.$emit('close-dialog')
      this.resetLabelsA()
      this.resetLabelsB()
    },
    resetLabelsA() {
      this.selectedA_label = "";
      this.selectedA_type = "";
      this.selectedA_baseIri = "";
      this.selectedA_iri = "";
      this.integratedLabel = "";
    },
    resetLabelsB() {
      this.selectedB_label = "";
      this.selectedB_type = "";
      this.selectedB_baseIri = "";
      this.selectedB_iri = "";
      this.integratedLabel = "";
    },
    addHandler() {
      window.addEventListener('clickEle_msg', this.selectAlignment);
    },
    removeHandler() {
      window.removeEventListener('clickEle_msg', this.selectAlignment);
    },
    addAlignment() {

      this.$emit("add-alignment", {
        row: {iriA: this.selectedA_iri, iriB: this.selectedB_iri, l: this.integratedLabel, type: this.selectedA_type}
      })
      this.resetLabelsA()
      this.resetLabelsB()

    },
    selectAlignment: function (event) {
      console.log("listener");
      console.log(event.detail)

      if (event.detail.id == this.dsA.id) {
        if (event.detail.isSelected) {

          if (this.selectedB_type == event.detail.type || this.selectedB_type == "") {

            this.selectedA_label = event.detail.label;
            this.selectedA_type = event.detail.type;
            this.selectedA_baseIri = event.detail.baseIri;
            this.selectedA_iri = event.detail.iri;
            this.alertAlignmentType = false;
          } else {
            // if(this.se)
            this.alertAlignmentType = true;
            this.resetLabelsA()
            // show alert about type incompatibility
          }
        }
      } else {
        if (event.detail.isSelected) {
          if (this.selectedA_type == event.detail.type || this.selectedA_type == "") {

            this.selectedB_label = event.detail.label;
            this.selectedB_type = event.detail.type;
            this.selectedB_baseIri = event.detail.baseIri;
            this.selectedB_iri = event.detail.iri;
            this.alertAlignmentType = false;
          } else {
            this.alertAlignmentType = true;
            this.resetLabelsB()
          }
        }
      }

      if (this.selectedA_label && this.selectedB_label) {

        this.disableAdd = false;
        this.integratedLabel = this.selectedA_label + "_" + this.selectedB_label;
      } else {
        this.integratedLabel = ""
        this.disableAdd = true
      }

    }
  },
  mounted: function () {
    this.addHandler();
  },
  destroyed() {
    this.removeHandler();
  }

});
</script>

<style lang="css">


</style>
