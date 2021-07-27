<template>
  <q-page>
    <Webvowl
      :view="mappings_Graph_select"
      :id="this.$route.params.id"
      :LAVMappingID="this.$route.params.LAVMappingID"
    />
  </q-page>
</template>

<script lang="ts">
import { Todo, Meta } from "components/models";
import Webvowl from "components/graph/Webvowl.vue";
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "LavMappingWebvowl",
  components: { Webvowl },
  data() {
    return { mappings_Graph_select: "mappings_Graph_select" };
  },
});
</script>
