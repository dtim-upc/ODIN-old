<template lang="html">

  <iframe :src="`html/index.html?` + view + params()"
    frameborder="0"
    allowfullscreen class="fit"
    style="min-width: inherit; min-height: inherit;">
   </iframe>

</template>

<script>
import { defineComponent} from 'vue';

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
  },
  props: {
    view: {
      type: String,
      default: "global_Graph_Edit"
    },
    id: {
      type:String,
      default:''
    },
    LAVMappingID: {
      type:String,
      default:''
    },
    integratedID: {
      type:String,
      default:''
    },
    minimalI:{type: Boolean, default: false},
    integrated:{type: Boolean, default: false}
  },
  methods: {

    params() {

      console.log(this.view)
      // console.log("****")
      switch (this.view) {
        case "source_graph":
          console.log("int..:" +this.integrated)
          if(this.minimalI) {
            return "&dataSourceID="+this.id+"&minimal_integration_graph=true"
          } else if(this.integrated) {
            return "&dataSourceID="+this.id+"&integration_graph=true"
          } else {
            return "&dataSourceID="+this.id
          }

          break;
        case "mappings_Graph_select":
          return "&id="+this.id+"&LAVMappingID="+this.LAVMappingID
          break;
        case "bdi_manual_alignments":
          if(this.minimalI) {
            return "&dataSourceID="+this.id+"&minimal_integration_graph=true"
          } else if(this.integrated) {
            return "&dataSourceID="+this.id+"&integration_graph=true"
          } else {
            return "&dataSourceID="+this.id
          }
          break;
        case "global_Graph_Edit":
          return "&id="+this.id

        case "omq":
          return "&dataSourceID="+this.id+"&minimal_integration_graph=true"
        default:
          return "";
      }
      // if(this.view == "source_graph"){
      //
      //   if(this.id !== '') {
      //     return "&dataSourceID="+this.id
      //   }
      //   return ""
      //
      // } else {
      //   if(this.id !== '') {
      //     return "&id="+this.id+"&LAVMappingID="+this.LAVMappingID
      //   }
      //   return ""
      // }


    }

  }
});
</script>

<style scoped>
  /* @import './public/css/webvowl.app.css';
  @import './public/css/webvowl.css'; */
</style>
