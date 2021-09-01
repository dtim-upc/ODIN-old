var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(SourceGraph_JSON.ContainerMembershipProperty.name)
            .guiLabel(SourceGraph_JSON.ContainerMembershipProperty.gui_name)
            .iri(SourceGraph_JSON.ContainerMembershipProperty.iri)
            .iriType(SourceGraph_JSON.ContainerMembershipProperty.iri)
            .background(SourceGraph_JSON.ContainerMembershipProperty.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
