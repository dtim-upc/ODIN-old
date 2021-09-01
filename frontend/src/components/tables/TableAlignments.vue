<template>

  <q-table :rows="rows" :columns="columns" :class="{ 'no-shadow': no_shadow }"
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

  <SelectAlignments :show_dialog="show_dialog" :ds-a="dsA" :ds-b="dsB"  @close-dialog="show_dialog=false" @add-alignment="addRow"/>

</template>

<script >
import {ref} from 'vue'
import {defineComponent} from "vue";
import SelectAlignments from "components/forms/integration/SelectAlignments.vue";


export default defineComponent({
  name: "TableAlignments",
  components: {SelectAlignments},
  props: {
    no_shadow: {type: Boolean, default: false},
    dsA: {type: Object, default: {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""}},
    dsB: {type: Object, default: {id:"", name:"", type:"", graphicalGraph:"", iri:"", path:""}},
  },
  emits: {
    "alignments": null,
  },
  data() {

    const columns = [
      {name: "uriA", required: true, label: "Name A", align: "center", field: "iriA", sortable: true,},
      {name: "uriB", required: true, label: "Name B", align: "center", field: "iriB", sortable: true,},
      {name: "label", required: true, label: "Integrated name", align: "center", field: "l", sortable: true,},
      {name: "type", required: true, label: "Type", align: "center", field: "type", sortable: true,},
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];
    const rows = [];
    const title = "Alignments";

    // const datasourceA: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name1', type: '', graphicalGraph:""};
    // const datasourceB: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name2', type: '', graphicalGraph:""};

    // const show_dialog = false;
    // const show_edit_dialog: boolean = false;
    // const newGlobalGraph = {
    //   id: "",
    //   name: "",
    //   namedGraph: "",
    //   namespace: "",
    //   graphicalGraph: "",
    // };

    return {
      // datasourceA,
      // datasourceB,
      columns,
      rows,
      title,
      // show_dialog,
      show_dialog: ref(false),
      // search: "",
      // show_edit_dialog,
    };
  },
  mounted(){
    // this.rows.push({
    //   uriA: "http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/bikes",
    //   uriB: "http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/cities",
    //   label: "bikes_cities",
    //   type: "Class"
    // })
  },
  methods:{
    addRow(props){
      if(props){
        if(props.row){
          // console.log("add _")
          // console.log(props.row)

          // this.rows.push({
          //   uriA: "http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/bikes",
          //   uriB: "http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/cities",
          //   label: "bikes_cities",
          //   type: "Class"
          // })

          console.log(this.rows)
          console.log(props.row)
          console.log(this.rows.indexOf(props.row))
          if(this.rows.indexOf(props.row) === -1) {
            this.rows.push(props.row);
            // console.log(this.items);
          }

          // this.rows.push(props.row)
          this.$emit("alignments", { data: this.rows          } )
          this.$q.notify({
            color: "positive",
            textColor: "white",
            icon: "check_circle",
            message: `Alignment ${props.row.l} added`,
          });

          // console.log(this.rows)
        }
      }


    },
    deleteRow(props){
      this.rows.splice(props.rowIndex, 1);
      this.$emit("alignments", { data: this.rows          } )
    }
  }

});
</script>

<style lang="css" scoped>


</style>
