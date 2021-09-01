<template>

  <q-page>
    <Webvowl :view="global_Graph_Edit" :id="this.$route.params.id"/>
  </q-page>

</template>

<script >
import Webvowl from 'components/graph/Webvowl.vue';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'Test_webvowl',
  components: { Webvowl },
  data() {
    return {global_Graph_Edit: "global_Graph_Edit"}
  }
});
</script>
