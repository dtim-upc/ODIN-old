var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Integration.IntegrationClass.name)
            .guiLabel(Integration.IntegrationClass.gui_name)
            .iri(Integration.IntegrationClass.iri)
            .iriType(Integration.IntegrationClass.iri)
            .background(Integration.IntegrationClass.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
