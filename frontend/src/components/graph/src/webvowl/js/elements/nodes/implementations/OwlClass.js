var RoundNode = require("../RoundNode");

module.exports = (function () {

	var o = function (graph) {
		RoundNode.apply(this, arguments);

		this.type("owl:Class")
      .iriType("http://www.w3.org/2002/07/owl#Class");
	};
	o.prototype = Object.create(RoundNode.prototype);
	o.prototype.constructor = o;

	return o;
}());
