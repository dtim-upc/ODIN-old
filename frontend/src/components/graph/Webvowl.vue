<template lang="html">

  <iframe :src="`graph/main/index.html?global_Graph_Edit`" frameborder="0"
    allowfullscreen class="fit" style="min-height: inherit;min-width: inherit;">
   </iframe>

</template>

<script>
import { defineComponent, onMounted } from 'vue';
import { odinApi } from 'boot/axios';

// import './metamodel.js'
// import './public/js/d3.min.js';
// import './public/js/webvowl.js';
// import './public/js/webvowl.app.js';
// import './public/jsrc/components/graph/src/webvowl/js/entry.jss/webvowl.app.js';

// import {Global} from './metamodel.js'

// import * as d3 from 'd3'
// import {webvowl} from './src/webvowl/js/entry.js'
// import * as webvowlApp from './src/app/js/entry.js'

export default defineComponent({
  name: 'Webvowl',
  setup() {
    // mounted
    // onMounted(() => {
    //   console.log('Component is mounted!')
    //   // this.loadJS('./metamodel.js')
    //   // console.log(webvowl.hola)
    //   // webvowl.app().initialize("global_Graph_Edit");
    // })
  }
});
</script>

<style scoped>
  /* @import './public/css/webvowl.app.css';
  @import './public/css/webvowl.css'; */
</style>
