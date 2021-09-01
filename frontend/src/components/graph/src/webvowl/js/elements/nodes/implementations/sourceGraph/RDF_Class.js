var RoundNode = require("../../RoundNode");

module.exports = (function () {

  var o = function (graph) {
    RoundNode.apply(this, arguments);
    this.type(SourceGraph_JSON.RDF_Class.name)
      .guiLabel(SourceGraph_JSON.RDF_Class.gui_name)
      .iri(SourceGraph_JSON.RDF_Class.iri)
      .iriType(SourceGraph_JSON.RDF_Class.iri)
      .background(SourceGraph_JSON.RDF_Class.color);
  };
  o.prototype = Object.create(RoundNode.prototype);
  o.prototype.constructor = o;

  return o;
}());
