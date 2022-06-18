<template>
    <p>JSON preview</p>
</template>

<script>
import {defineComponent, ref} from "vue";
import {odinApi} from "boot/axios";
import notify from "components/hooks/notify";
import {useStore} from "vuex";

export default defineComponent({
  name: "PreviewJSON",
  props: {
    // showFormButtons: {type: Boolean, default: true},
  },
//   emits: ["submit-sucess"],
  setup(props) {

       const integratedId = ref("")
      
    return  integratedId
    
  },
});
</script>

<style lang="css" scoped>
</style>
