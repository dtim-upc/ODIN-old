<template>

  <q-page>
    <Webvowl :view="viewMode" :id="this.$route.params.id"/>
  </q-page>

</template>

<script >

import Webvowl from 'components/graph/Webvowl.vue';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'DatasourceWebVowl',
  components: { Webvowl },
  data() {
    return {viewMode: "source_graph"}
  }
});
</script>
