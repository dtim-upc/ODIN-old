var BaseProperty = require("../../BaseProperty");

module.exports = (function () {

    var o = function (graph) {
        BaseProperty.apply(this, arguments);

        this.attributes(["object"]) //?
            .styleClass("objectproperty")
            .type(SourceGraph_JSON.Property.name)
            .guiLabel(SourceGraph_JSON.Property.gui_name)
            .baseIri(Namespaces.RDF)
            .iriType(SourceGraph_JSON.Property.iri);

        var label = SourceGraph_JSON.Property.iri.split("/").slice(-1)[0];

        // Disallow overwriting the label
        this.label = function (p) {
            if (!arguments.length) return label;
            return this;
        };
    };
    o.prototype = Object.create(BaseProperty.prototype);
    o.prototype.constructor = o;

    return o;
}());


