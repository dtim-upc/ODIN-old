import {ref} from 'vue'
import {BaseNode} from "components/graph/experiments/elements/nodes/BaseNode";
import {drawTools} from "components/graph/experiments/elements/drawTools";

export function RoundNode() {

  const collapsible = ref(false)
  const radius = ref(60)
  const rectangularRepresentation = ref(false)
  const renderingElement = ref(null)
  const textBlock = ref(null)

  const { collectCssClasses, nodeElement, backgroundColor ,...node } = BaseNode()

  // functions
  const draw = (parentElement, additionalCssClasses) => {

    let cssClasses = collectCssClasses()
    nodeElement.value = parentElement

    // if ( backgroundColor.value ) {
    // }
    //

    if (additionalCssClasses instanceof Array) {
      cssClasses = cssClasses.concat(additionalCssClasses);
    }


    if (rectangularRepresentation===true) {
      renderingElement.value = drawTools.appendRectangularClass(parentElement, 80,80, cssClasses, "that.labelForCurrentLanguage()", backgroundColor.value);
    }else {
      renderingElement.value = drawTools.appendCircularClass(parentElement, actualRadius(), cssClasses, "that.labelForCurrentLanguage()", backgroundColor.value);
    }

    postDrawActions(parentElement);

  }

  const actualRadius = () => {
    return radius.value
    // if(  ) {
    //   return radius.value
    // } else {
    //
    // }

  }

  const createTextBlock = () => {
    var bgColor= backgroundColor.value;
    // if (that.attributes().indexOf("deprecated")>-1)
    //   bgColor=undefined;

    var textBlock = new CenteringTextElement(that.nodeElement(), bgColor);

    var equivalentsString = that.equivalentsString();
    var suffixForFollowingEquivalents = equivalentsString ? "," : "";

    textBlock.addText(that.labelForCurrentLanguage(), "", suffixForFollowingEquivalents);
    textBlock.addEquivalents(equivalentsString);
    if (!graph.options().compactNotation()) {
      textBlock.addSubText(that.indicationString());
    }
    textBlock.addInstanceCount(that.individuals().length);

    return textBlock;
  }

  const postDrawActions = () => {

    textBlock.value = createTextBlock();

    that.addMouseListeners();
    if (that.pinned()) {
      that.drawPin();
    }
    if (that.halo()) {
      that.drawHalo(false);
    }
    if (that.collapsible()) {
      that.drawCollapsingButton();
    }
  };


  return {  collectCssClasses ,...node ,
    collapsible,
    radius,
    rectangularRepresentation,
    textBlock ,
    renderingElement
  }

}
