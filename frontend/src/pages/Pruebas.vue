<template>
  <div class="q-pa-md">
<NewDatasourceWrapperStepper/>
  </div>
</template>
<script>
import {computed, ref} from 'vue'
import NewDatasourceWrapperStepper from "components/stepper/NewDatasourceWrapperStepper";
import {useStore} from "vuex";


export default {
  components: {NewDatasourceWrapperStepper},
  setup () {
    return {
      step: ref(1)
    }
  }
}
</script>


<!--<template>-->
<!--  <div>-->
<!--    <h2 ref="titleRef">{{ formattedMoney }}</h2>-->
<!--    <input v-model="delta" type="number">-->
<!--    <button @click="add">Add</button>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--// In Vue 3, you'll import from "vue"-->
<!--import { ref, computed, onMounted } from "@vue/composition-api";-->

<!--export default {-->
<!--  setup(props) {-->
<!--    // State-->
<!--    const money = ref(10);-->
<!--    const delta = ref(1);-->

<!--    // Refs-->
<!--    const titleRef = ref(null);-->

<!--    // Computed props-->
<!--    const formattedMoney = computed(() => money.value.toFixed(2));-->

<!--    // Hooks-->
<!--    onMounted(() => {-->
<!--      console.log("titleRef", titleRef.value);-->
<!--    });-->

<!--    // Methods-->
<!--    const add = () => (money.value += Number(delta.value));-->

<!--    return {-->
<!--      delta,-->
<!--      money,-->
<!--      titleRef,-->
<!--      formattedMoney,-->
<!--      add-->
<!--    };-->
<!--  }-->
<!--};-->
<!--</script>-->
