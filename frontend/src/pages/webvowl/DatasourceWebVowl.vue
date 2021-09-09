<template>

  <q-page>
    <Webvowl :view="viewMode" :id="this.$route.params.id" :minimal-i="minimalI" :integrated="integrated"/>
  </q-page>

</template>

<script >

import Webvowl from 'components/graph/Webvowl.vue';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'DatasourceWebVowl',
  props: {
    minimalI:{type: Boolean, default: false},
    integrated:{type: Boolean, default: false}
  },
  components: { Webvowl },
  data() {
    return {viewMode: "source_graph"}
  }
});
</script>
