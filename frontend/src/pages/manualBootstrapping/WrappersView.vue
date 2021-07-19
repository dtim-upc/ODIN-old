<template>
  <q-page>
    <div class="q-pa-md">
      <!-- <h1> {{ $route.params.id }} </h1> -->
      <h4 style="margin-bottom: 6px">Wrapper View</h4>
      <p class="text-subtitle1">{{ wrapperId }}</p>
      
      <q-input v-model="this.wrapper.name" label="name" readonly />
      <q-input
        v-model="this.wrapper.attributes"
        label="attributes"
        readonly
      /><q-input
        v-model="this.wrapper.dataSourcesId"
        label="dataSourcesId"
        readonly
      />
    </div>
  </q-page>
</template>


<script lang="ts">
import { Wrapper } from "src/components/models";
import { odinApi } from "boot/axios";
import { defineComponent } from 'vue';

export default defineComponent({
  data() {
    const wrapperId: string | string[] = this.$route.params.id;
    const wrapper: Wrapper = {
        id: "",
        name: "",
        attributes: [{isID: false, name: ""}],
        dataSourcesId: "",
        dataSourcesLabel: "",
    };
    return { wrapperId, wrapper };
  },
  mounted() {
    this.retrieveData();
  },
  methods: {
    retrieveData() {
      odinApi.get("/wrapper/view/" + this.wrapperId).then((response) => {
        if (response.status == 200) {
          this.wrapper = response.data;
        }
      });
    },
  },
})  ;
</script>
