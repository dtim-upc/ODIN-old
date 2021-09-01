<template>
  <q-page>
    <Webvowl
      :view="omq"
      :id="this.$route.params.id"
    />
  </q-page>
</template>

<script >

import Webvowl from "components/graph/Webvowl.vue";
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "OMQWebvowl",
  components: { Webvowl },
  data() {
    return { omq: "omq" };
  },
});
</script>
