var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Integration.IntegrationDProperty.name)
            .guiLabel(Integration.IntegrationDProperty.gui_name)
            .iri(Integration.IntegrationDProperty.iri)
            .iriType(Integration.IntegrationDProperty.iri)
            .background(Integration.IntegrationDProperty.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
