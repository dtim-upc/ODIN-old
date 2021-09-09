var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Wrapper.Attribute.name)
            .guiLabel(Wrapper.Attribute.gui_name)
            .iri(Wrapper.Attribute.iri)
            .iriType(Wrapper.Attribute.iri)
            .background(Wrapper.Attribute.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
