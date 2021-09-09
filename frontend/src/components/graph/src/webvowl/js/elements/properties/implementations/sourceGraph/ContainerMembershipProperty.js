var BaseProperty = require("../../BaseProperty");

module.exports = (function () {

  var o = function (graph) {
    BaseProperty.apply(this, arguments);

    this.attributes(["object"]) //?
      .styleClass("objectproperty")
      .type(SourceGraph_JSON.ContainerMembershipProperty.name)
      .guiLabel(SourceGraph_JSON.ContainerMembershipProperty.gui_name)
      .baseIri(Namespaces.RDFS)
      .iriType(SourceGraph_JSON.ContainerMembershipProperty.iri);

    var label = SourceGraph_JSON.ContainerMembershipProperty.iri.split("/").slice(-1)[0];

    // Disallow overwriting the label
    // this.label = function (p) {
    //   if (!arguments.length) return label;
    //   return this;
    // };
  };
  o.prototype = Object.create(BaseProperty.prototype);
  o.prototype.constructor = o;

  return o;
}());


