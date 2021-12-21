var BaseProperty = require("../BaseProperty");

module.exports = (function () {

	var o = function (graph) {
		BaseProperty.apply(this, arguments);

		this.attributes(["rdf"])
			.styleClass("rdfproperty")
      .iriType("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property")
			.type("rdf:Property");
	};
	o.prototype = Object.create(BaseProperty.prototype);
	o.prototype.constructor = o;

	return o;
}());
