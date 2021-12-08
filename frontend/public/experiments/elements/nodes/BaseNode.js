import {BaseElement} from "components/graph/experiments/elements/BaseElement";
import {ref} from 'vue'

export function BaseNode()  {


  const individuals = ref([]);
  const disjointUnion = ref(null)
  const disjointWith = ref(null)

  // Element containers
  const nodeElement = ref(null)


  const {styleClass,visualAttributes, ...base} = BaseElement()

  // functions

  const collectCssClasses = () => {

    let cssClasses = [];

    if( typeof styleClass.value === "string" ) {
      cssClasses.push( styleClass.value )
    }

    cssClasses = cssClasses.concat(visualAttributes.value)
    return cssClasses

  }


  return {
    ...base, styleClass, individuals, disjointUnion, disjointWith,
    nodeElement,
    collectCssClasses
  }




}
