var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Integration.IntegrationOProperty.name)
            .guiLabel(Integration.IntegrationOProperty.gui_name)
            .iri(Integration.IntegrationOProperty.iri)
            .iriType(Integration.IntegrationOProperty.iri)
            .background(Integration.IntegrationOProperty.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
