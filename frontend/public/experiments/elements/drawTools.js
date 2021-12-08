

export function drawTools() {


  const appendCircularClass = (parent, radius, cssClasses, tooltip, backgroundColor) => {

    var circle = parent
      .append("circle")
      .classed("class", true)
      .attr("r", radius);

    addCssClasses(circle, cssClasses);
    addToolTip(circle, tooltip);
    addBackgroundColor(circle, backgroundColor);

    return circle;

  }


  const appendRectangularClass = (parent, width, height, cssClasses, tooltip, backgroundColor) => {
    var rectangle = parent.append("rect")
      .classed("class", true)
      .attr("x", -width / 2)
      .attr("y", -height / 2)
      .attr("width", width)
      .attr("height", height);

    addCssClasses(rectangle, cssClasses);
    addToolTip(rectangle, tooltip);
    addBackgroundColor(rectangle, backgroundColor);

    return rectangle;
  };

  const addCssClasses = (element, cssClasses) => {
    if (cssClasses instanceof Array) {
      cssClasses.forEach(function (cssClass) {
        element.classed(cssClass, true);
      });
    }
  }

  const addToolTip = (element, tooltip)  => {
    if (tooltip) {
      element.append("title").text(tooltip);
    }
  }

  const addBackgroundColor = (element, backgroundColor) => {
    if (backgroundColor) {
      element.style("fill", backgroundColor);
    }
  }



  return {appendCircularClass}


}
