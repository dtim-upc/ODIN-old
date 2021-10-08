<template>
  <div class="q-pa-md">
<!--    <Graph></Graph>-->
  </div>
</template>
<script>
import {computed, ref} from 'vue'
// import NewDatasourceWrapperStepper from "components/stepper/NewDatasourceWrapperStepper";
import {useStore} from "vuex";
// import Graph from "components/graph/experiments/Graph";

export default {
  // components: {Graph},
  setup () {
    return {
      step: ref(1)
    }
  }
}
</script>
