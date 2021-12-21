var RoundNode = require("../RoundNode");

module.exports = (function () {

	var o = function (graph) {
		RoundNode.apply(this, arguments);

		this.attributes(["rdf"])
      .iriType("http://www.w3.org/2000/01/rdf-schema#Class")
			.type("rdfs:Class");
	};
	o.prototype = Object.create(RoundNode.prototype);
	o.prototype.constructor = o;

	return o;
}());
