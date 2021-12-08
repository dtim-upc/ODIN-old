import {ref} from 'vue'
import {RoundNode} from "components/graph/experiments/elements/nodes/RoundNode";


export function OwlClass() {

  const {type, ...other } = RoundNode();

  type.value = "owl:Class";

  return {type, ...other}


}
