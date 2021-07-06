<template>

  <q-page>
    <Webvowl :view="global_Graph_Edit"/>
  </q-page>

</template>

<script lang="ts">
import { Todo, Meta } from 'components/models';
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
