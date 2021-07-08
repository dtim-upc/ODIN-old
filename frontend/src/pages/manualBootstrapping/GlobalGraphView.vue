<template>
  <q-page>
    <div class="q-pa-md">
      <!-- <h1> {{ $route.params.id }} </h1> -->
      <h4 style="margin-bottom: 6px">Global Graph View</h4>
      <p class="text-subtitle1">{{$route.params.id}}</p>
      
      <q-input v-model="this.globalGraph.name" label="name" readonly />
      <q-input
        v-model="this.globalGraph.namedGraph"
        label="namedGraph"
        readonly
      />
      <q-input
        type="textarea"
        v-model="this.globalGraph.graphicalGraph"
        label="graphicalGraph"
        readonly
      />
      <q-input
        v-model="this.globalGraph.namespace"
        label="namespace"
        readonly
      />
    </div>
  </q-page>
</template>


<script lang="ts">

import { GlobalGraph } from "src/components/models";
import { odinApi } from "boot/axios";
import { defineComponent } from 'vue';

export default defineComponent({
  data() {
    const graphId: string | string[] = this.$route.params.id;
    const globalGraph: GlobalGraph = {
      id: "",
      name: "",
      namedGraph: "",
      graphicalGraph: "",
      namespace: "",
    };
    return { graphId, globalGraph };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    retrieveData() {
      odinApi.get("/globalGraph/view/" + this.graphId).then((response) => {
        if (response.status == 200) {
          this.globalGraph = response.data;
          console.log(this.globalGraph);
        }
      });
    },
  },
});
</script>
