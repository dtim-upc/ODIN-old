var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(SourceGraph_JSON.Seq.name)
            .guiLabel(SourceGraph_JSON.Seq.gui_name)
            .iri(SourceGraph_JSON.Seq.iri)
            .iriType(SourceGraph_JSON.Seq.iri)
            .background(SourceGraph_JSON.Seq.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
