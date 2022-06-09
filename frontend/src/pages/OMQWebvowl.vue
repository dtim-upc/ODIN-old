<template>
  <q-page>

    <q-dialog v-model="alert" full-width>
      <q-card>
        <!-- <q-card-section>
          <div class="text-h6">Alert</div>
        </q-card-section> -->

        <q-card-section>

          <TableQueryResult :columns="columns" :rows="rows" :no_shadow=true />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="OK" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <Webvowl
      :view="omq"
      :id="this.$route.params.id"
      style="position:absolute;bottom:0;"
    />
  </q-page>
</template>

<script >

import Webvowl from "components/graph/Webvowl.vue";
import { defineComponent, ref } from "vue";

import TableQueryResult from "components/tables/TableQueryResult.vue";
// import TableOMQ from 'components/tables/TableOMQ.vue';

export default defineComponent({
  name: "OMQWebvowl",
  components: { Webvowl, TableQueryResult },
  setup(){
    return {alert: ref(false)}
  },
  data() {
      const columns = [
      {name: "Name", required: true, label: "Name", align: "center", field: "name", sortable: true,},
      {name: "Type", required: true, label: "Type", align: "center", field: "type", sortable: true,},
      {name: "#Wrappers", label: "#Wrappers", align: "center", field: "wrappers", sortable: true,},
      {name: "View Metadata", label: "View Metadata", align: "center", field: "View Metadata", sortable: false,},
      {
        name: "View_Source_Graph", label: "View Source Graph", align: "center",
        field: "View Source Graph", sortable: false,
      },
      {name: "actions", label: "actions", align: "center", field: "actions", sortable: false,},
    ];
    const rows = [];
    return { omq: "omq", columns, rows };
  },
  methods:{

    addHandler() {
      window.addEventListener('queryResult', this.showResult);
    },
    removeHandler() {
      window.removeEventListener('queryResult', this.showResult);
    },
    showResult: function (event) {
      console.log("listener");
      console.log(event.detail)

      event.detail.columns

      const qcol = []
      for (const col in event.detail.columns){
        var c = new Object();
				c.name = event.detail.columns[col];
        c.label =event.detail.columns[col];
        c.field =event.detail.columns[col];
        c.align="center"
        c.sortable =  true;
        qcol.push(c)
      }
      this.columns = qcol;
      const qrows = []
      for (const col in event.detail.rows){
        qrows.push(JSON.parse(event.detail.rows[col]))
      }
      this.rows = qrows;
      this.alert = true;
      console.log(qcol)
      console.log(qrows)

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
