<template>
  <q-page>
    <div class="q-pa-md">
      <!-- <h1> {{ $route.params.id }} </h1> -->
      <h4 style="margin-bottom: 6px">Data Sources View</h4>
      <p class="text-subtitle1">{{ dataSourceId }}</p>
      
      <q-input v-model="this.dataSources.name" label="name" readonly />
      <q-input
        v-model="this.dataSources.type"
        label="type"
        readonly
      />
    </div>
  </q-page>
</template>


<script lang="ts">
import { DataSources} from "src/components/models";
import { odinApi } from "boot/axios";
import { defineComponent } from 'vue';

export default defineComponent({
  data() {
    const dataSourceId: string | string[] = this.$route.params.id;
    const dataSources: DataSources = {
        id: "",
        name: "",
        type: "",
    };
    return { dataSourceId, dataSources };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    retrieveData() {
      odinApi.get("/dataSource/view/" + this.dataSourceId).then((response) => {
        if (response.status == 200) {
          this.dataSources = response.data;
          console.log(this.dataSources);
        }
      });
    },
  },
});
</script>
