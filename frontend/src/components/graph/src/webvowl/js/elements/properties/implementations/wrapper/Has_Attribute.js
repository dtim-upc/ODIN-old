var BaseProperty = require("../../BaseProperty");

module.exports = (function () {

  var o = function (graph) {
    BaseProperty.apply(this, arguments);

    this.attributes(["object"]) //?
      .styleClass("objectproperty")
      .type(Wrapper.hasAttribute.name)
      .guiLabel(Wrapper.hasAttribute.gui_name)
      .baseIri(Namespaces.S)
      .iriType(Wrapper.hasAttribute.iri);

    var label = Wrapper.hasAttribute.iri.split("/").slice(-1)[0];

    // Disallow overwriting the label
    this.label = function (p) {
      if (!arguments.length) return label;
      return this;
    };
  };
  o.prototype = Object.create(BaseProperty.prototype);
  o.prototype.constructor = o;

  return o;
}());


