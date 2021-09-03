<template>

  <q-table :rows="alignments" :columns="columns" :class="{ 'no-shadow': no_shadow }"
           row-key="uriA"
           no-data-label="Consider adding some alignments to start the integration process"
           no-results-label="The filter didn't uncover any results" >
    <template v-slot:top-left="">
      <div class="q-table__title">
        {{ title }}
        <q-btn padding="none" color="secondary" icon="add" @click="show_dialog = true"/>
      </div>
    </template>

    <template v-slot:top-right="props">

<!--      <q-input outlined dense debounce="400" color="primary" v-model="search">-->
<!--        <template v-slot:append>-->
<!--          <q-icon name="search"/>-->
<!--        </template>-->
<!--      </q-input>-->

      <q-btn flat round dense :icon="props.inFullscreen ? 'fullscreen_exit' : 'fullscreen'"
             @click="props.toggleFullscreen">
        <q-tooltip :disable="$q.platform.is.mobile" v-close-popup>
          {{ props.inFullscreen ? "Exit Fullscreen" : "Toggle Fullscreen" }}
        </q-tooltip>
      </q-btn>
    </template>

    <template v-slot:body-cell-actions="props">
      <q-td :props="props">
        <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>
      </q-td>
    </template>

    <template v-slot:no-data="{ icon, message, filter }">
      <div class="full-width row flex-center text-accent q-gutter-sm">
<!--        <q-icon size="2em" name="sentiment_dissatisfied"/>-->
        <span> {{ message }} </span>
<!--        <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon"/>-->
      </div>
    </template>
  </q-table>

  <SelectAlignments :show_dialog="show_dialog" :ds-a="selectedDS[0]" :ds-b="selectedDS[1]"  @close-dialog="show_dialog=false" @add-alignment="addRow"/>

</template>

<script >
import {computed, ref} from 'vue'
import {defineComponent} from "vue";
import SelectAlignments from "components/forms/integration/SelectAlignments.vue";
import {useStore} from "vuex";
import {useQuasar} from "quasar";
// import alerts from "components/hooks/alerts"
import notify from 'components/hooks/notify';

export default defineComponent({
  name: "TableAlignments",
  components: {SelectAlignments},
  props: {
    no_shadow: {type: Boolean, default: false},
    alignments: {type :Array, default: []}
  },
  // emits: {
  //   "alignments": null,
  // },
  setup(props , {emit}){
    const store = useStore()
    const $q = useQuasar()
    const selectedDS = computed(() => store.state.datasource.selectedDatasources)

    const columns = [
      {name: "uriA", required: true, label: "Name A", align: "center", field: "iriA", sortable: true,},
      {name: "uriB", required: true, label: "Name B", align: "center", field: "iriB", sortable: true,},
      {name: "label", required: true, label: "Integrated name", align: "center", field: "l", sortable: true,},
      {name: "type", required: true, label: "Type", align: "center", field: "type", sortable: true,},
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];
    // const rows = [];
    const title = "Alignments";


    const addRow= (props2  )=> {
      if(props2){
        if(props2.row){

          console.log(props.alignments)
          console.log(props.row)
          console.log(props.alignments.indexOf(props2.row))
          if(props.alignments.indexOf(props2.row) === -1) {
            props.alignments.push(props2.row);
            // console.log(this.items);
          }

          console.log("emitted")
          emit("update:alignments", props.alignments )
          notify.positive(`Alignment ${props2.row.l} added`)
          // console.log(this.rows)
        }
      }


    }
    const deleteRow = (props2) =>{
      props.alignments.splice(props2.rowIndex, 1);
      emit("update:alignments", props.alignments )
      // emit("alignments", { data: rows          } )
      // this.this.$emit("alignments", { data: this.rows          } )
    }


    return {selectedDS, columns, title,show_dialog: ref(false), deleteRow, addRow}

  }


});
</script>

<style lang="css" scoped>


</style>
