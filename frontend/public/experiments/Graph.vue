<template>

<!--  <div id="graph"  ref="graphSVG" ></div>-->
<!--  <svg id="graphC" ref="graphSVG"> </svg>-->

  <svg ref="graphRef"></svg>


</template>

<script>
import {computed, ref, onMounted, defineComponent} from 'vue'
import {select} from "d3"
import {zoom} from "d3-zoom"
import {BaseElement} from "components/graph/experiments/elements/BaseElement"
// import {BaseNode} from "components/graph/experiments/elements/nodes/BaseNode";
import {OwlClass} from "components/graph/experiments/elements/nodes/implementations/OwlClass";

export default defineComponent({
  name: "Graph",
  props: { height: {type: Number, default: 600}, width: {type: Number, default: 600} },
  setup(props){

    const graphRef = ref(null)

    const zoomed = () => {console.log("zoomed function")}

    const node = OwlClass();
    console.log("owl:class")
    console.log(node)

    onMounted( () => {

      console.log("on..")

      const handlerZoom = zoom()
        .duration(150)
        .scaleExtent([0.01, 4])
        .on("zoom", zoomed)


      // d3.selectAll("#graph").append("svg")
      const svg = select(graphRef.value)
        .classed("vowlGraph", true)
        .attr("width",props.width )
        .attr("height", props.height )
        .call(handlerZoom)
        .append("g");


      const nodeContainer = svg.append("g").classed("nodeContainer", true);

      // graphContainer.value = d3.selectAll(graphSVG.value).append("svg")
      //   .classed("vowlGraph", true)
      //   .attr("width",props.width )
      //   .attr("height", props.height )
      //   .append("g");
      //
      // console.log(graphContainer.value)
      // console.log("***")
      // console.log(d3.selectAll(graphSVG.value)  )
        // .append("svg")
        // .classed("vowlGraph", true)
        // .attr("width", options.width())
        // .attr("height", options.height())
        // // .call(zoom)
        // .append("g");

    } )

return { graphRef, zoomed, node};

  }

});


</script>
