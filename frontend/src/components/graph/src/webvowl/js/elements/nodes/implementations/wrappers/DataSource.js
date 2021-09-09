var RoundNode = require("../../RoundNode");

module.exports = (function () {

    var o = function (graph) {
        RoundNode.apply(this, arguments);
        this.type(Wrapper.DataSource.name)
            .guiLabel(Wrapper.DataSource.gui_name)
            .iri(Wrapper.DataSource.iri)
            .iriType(Wrapper.DataSource.iri)
            .background(Wrapper.DataSource.color);
    };
    o.prototype = Object.create(RoundNode.prototype);
    o.prototype.constructor = o;

    return o;
}());
