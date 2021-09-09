var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Wrapper.Wrapper.name)
            .guiLabel(Wrapper.Wrapper.gui_name)
            .iri(Wrapper.Wrapper.iri)
            .iriType(Wrapper.Wrapper.iri)
            .background(Wrapper.Wrapper.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
