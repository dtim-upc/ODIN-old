<template>
  <div class="q-pa-md">
    <q-table
      title="Triple store"
      :rows="rows"
      :columns="columns"
      row-key="name"
      :pagination.sync="pagination"
    >

      <template v-slot:top-right="props">
        <q-btn outline color="primary" label="Delete all data (jena, mongo)" class="q-mr-xs"  @click="deleteData()" />

      </template>


    </q-table>
  </div>
</template>


<script>

import {odinApi} from "boot/axios";
import { ref, onMounted} from 'vue'
export default {

  name: 'ViewTripleStore',
  setup() {

    const columns = [

      {name: "?g", label: "?g", align: "center", field: "g", sortable: true,},
      {name: "?s", label: "?s", align: "center", field: "s", sortable: true,},
      {name: "?p", label: "?p", align: "center", field: "p", sortable: true,},
      {name: "?o", label: "?o", align: "center", field: "o", sortable: true,},
    ]

    const rows = ref([])

    onMounted(() => {
      odinApi.get("user/triplestore").then((response) => {
        console.log(response)
        if (response.status == 200) {

          rows.value = response.data;
        }
      });
    })


    const deleteData = () => {

      odinApi.delete("/admin/all")
      window.location.reload()

    }

    return {
      columns,
      rows,
      deleteData,
      pagination: {
        rowsPerPage: 0 // current rows per page being displayed
      },
    }

  }

}

</script>
