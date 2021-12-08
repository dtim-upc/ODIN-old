
import {ref, computed} from 'vue'

export function BaseElement() {


  const equivalents = ref([])
  const id = ref(null)
  const label = ref(null)
  const type = ref(null)
  const iri = ref(null)
  // iriType?
  //baseIri?
  const guiLabel = ref(null)
  //annotations?
  //attributes = []?
  const backgroundColor = ref(null)
  //comment
  //description
  //equivalentBase
  const visualAttributes = ref([])
  const focused = ref(false)
  // indications = [],
  const mouseEntered = ref(false)
  const styleClass = ref(null)
  const visible = ref(false)
  // // const background = ref(null)
  // originalLabel
  // 			changeTypeNode =false,
  // 			backupLabel,
  // languageTools = require("../util/languageTools")();


  // functions...

  // const labelForCurrentLanguage = () => {
  //
  //   let preferredLanguage = graph???
  //
  // }


  return {
    equivalents,
    id,
    label,
    type,
    iri,
    guiLabel,
    backgroundColor,
    visualAttributes,
    focused,
    mouseEntered,
    styleClass,
    visible
  }

}
