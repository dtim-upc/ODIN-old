<template>
  <div class="q-pa-md">
    <q-table
      title="Triple store"
      :rows="rows"
      :columns="columns"
      row-key="g"
      wrap-cells="true"
      :pagination.sync="pagination"
    >

      <template v-slot:top-right="props">
        <q-btn outline color="primary" label="Delete all data (jena, mongo)" class="q-mr-xs"  @click="deleteData()" />
      </template>

      <template #body="props">
        <q-tr :props="props">
          <q-td key="expand" name="expand" :props="props" auto-width>
            <q-btn flat round :icon="props.expand ? 'remove' : 'add'" @click="props.expand = !props.expand"/>
          </q-td>
          <!-- <q-td v-for="col in props.cols.filter(c => c.name == 'expand' )" :key="col.name" :props="props">
            {{ col.value }}
          </q-td> -->
          <q-td key="g" :props="props">
            {{props.row.g}}
          </q-td>
          <q-td key="s" :props="props">
            ...
          </q-td>
          <q-td key="p" :props="props">
            ...
          </q-td>
          <q-td key="o" :props="props">
            ...
          </q-td>
        </q-tr>
        <!-- CHILD ROW -->
        <q-tr
            v-for="triple in props.row.triples"
            v-show="props.expand"
            :key="triple.name"
            :props="props"
          >

          <q-td key="expand"/>
          <q-td key="g"/>
          <q-td key="s" :props="props">
            {{triple.s}}
          </q-td>
          <q-td key="p">
            {{triple.p}}
          </q-td>
          <q-td key="o" >
            {{triple.o}}
          </q-td>
          <!-- {{props.cols}} -->
        </q-tr>
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
      {name: 'expand', label: 'expand', align:"center", field: 'expand' },
      {name: "g", label: "?g", align: "center", field: "g", sortable: true,},
      {name: "s", label: "?s", align: "center", field: "s", sortable: true,},
      {name: "p", label: "?p", align: "center", field: "p", sortable: true,},
      {name: "o", label: "?o", align: "center", field: "o", sortable: true,},
    ]

    const rows = ref([])

    onMounted(() => {
      // odinApi.get("user/triplestore").then((response) => {
      //   console.log(response)
      //   if (response.status == 200) {

      //     rows.value = response.data;
      //   }
      // });
      odinApi.get("user/triples").then((response) => {
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
