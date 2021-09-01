<!--<template>-->
<!--  <q-table :rows="rows" :columns="columns" :filter="search"-->
<!--           row-key="name" no-data-label="I didn't find anything for you. Consider creating a new data source."-->
<!--           no-results-label="The filter didn't uncover any results" class="no-box-shadow">-->
<!--    <template v-slot:top-left="">-->
<!--      <div class="q-table__title">-->
<!--        {{ title }}-->
<!--        <q-btn padding="none" color="secondary" icon="add" @click="show_dialog = true"/>-->
<!--      </div>-->
<!--    </template>-->

<!--    <template v-slot:top-right="props">-->

<!--      &lt;!&ndash;      <q-input outlined dense debounce="400" color="primary" v-model="search">&ndash;&gt;-->
<!--      &lt;!&ndash;        <template v-slot:append>&ndash;&gt;-->
<!--      &lt;!&ndash;          <q-icon name="search"/>&ndash;&gt;-->
<!--      &lt;!&ndash;        </template>&ndash;&gt;-->
<!--      &lt;!&ndash;      </q-input>&ndash;&gt;-->

<!--      <q-btn flat round dense :icon="props.inFullscreen ? 'fullscreen_exit' : 'fullscreen'"-->
<!--             @click="props.toggleFullscreen">-->
<!--        <q-tooltip :disable="$q.platform.is.mobile" v-close-popup>-->
<!--          {{ props.inFullscreen ? "Exit Fullscreen" : "Toggle Fullscreen" }}-->
<!--        </q-tooltip>-->
<!--      </q-btn>-->
<!--    </template>-->

<!--    <template v-slot:body-cell-actions="props">-->
<!--      <q-td :props="props">-->
<!--        <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>-->
<!--      </q-td>-->
<!--    </template>-->

<!--    <template v-slot:no-data="{ icon, message, filter }">-->
<!--      <div class="full-width row flex-center text-accent q-gutter-sm">-->
<!--        <q-icon size="2em" name="sentiment_dissatisfied"/>-->
<!--        <span> Well this is sad... {{ message }} </span>-->
<!--        <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon"/>-->
<!--      </div>-->
<!--    </template>-->
<!--  </q-table>-->
<!--</template>-->

<!--<script >-->
<!--import {ref} from 'vue'-->
<!--import {defineComponent} from "vue";-->

<!--export default defineComponent({-->
<!--name: "TableIntegrations",-->
<!--setup() {-->
<!--  const columns = [-->
<!--    {name: "Name", required: true, label: "Name", align: "center", field: "name", sortable: true,},-->
<!--    {name: "Type", required: true, label: "Type", align: "center", field: "type", sortable: true,},-->
<!--    {name: "#Wrappers", label: "#Wrappers", align: "center", field: "wrappers", sortable: true,},-->
<!--    {name: "View Metadata", label: "View Metadata", align: "center", field: "View Metadata", sortable: false,},-->
<!--    {-->
<!--      name: "View_Source_Graph", label: "View Source Graph", align: "center",-->
<!--      field: "View Source Graph", sortable: false,-->
<!--    },-->
<!--    {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},-->
<!--  ];-->
<!--const columns = [-->
<!--{name: "name", required: true, label: "Name", align: "center", field: "name", sortable: true,},-->
<!--{name: "type", required: true, label: "Type", align: "center", field: "name", sortable: true,},-->
<!--  {name: "", required: true, label: "Name B", align: "center", field: "name", sortable: true,},-->
<!--];-->
<!--const rows: any[] = [{nameA:'hola', nameB:'hola2', label:'nns', type:'Ckass'}];-->
<!--const title = "Alignments";-->

<!--const datasourceA: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name1', type: ''};-->
<!--const datasourceB: DataSourceModel = {id: '612c3940a4e8f763dbf907a2', name: 'name2', type: ''};-->

<!--// const show_dialog = false;-->
<!--// const show_edit_dialog: boolean = false;-->
<!--// const newGlobalGraph = {-->
<!--//   id: "",-->
<!--//   name: "",-->
<!--//   namedGraph: "",-->
<!--//   namespace: "",-->
<!--//   graphicalGraph: "",-->
<!--// };-->

<!--return {-->
<!--datasourceA,-->
<!--datasourceB,-->
<!--columns,-->
<!--rows,-->
<!--title,-->
<!--// show_dialog,-->
<!--show_dialog: ref(false),-->
<!--// search: "",-->
<!--// show_edit_dialog,-->
<!--};-->
<!--},-->

<!--});-->

<!--</script>-->

<!--<style scoped>-->

<!--</style>-->
