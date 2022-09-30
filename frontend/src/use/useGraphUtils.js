import * as d3 from "d3";

export function useGraphUtils() {


    const maxNameLength = 14; //15
    const defaultPropertyHeight = 20;

    // maybe delete or update default radius value
    var defaultRadius = 50;

    const labelFromURI = (uri) => {

        var uriLabel = '';
        if (uri !== undefined && uri !== '') {
            var lastSlash = uri.lastIndexOf('/');
            var lastHash = uri.lastIndexOf('#');
            uriLabel = uri.substr(Math.max(lastSlash, lastHash) + 1).replace(/\_/g, ' ');
        }
        return uriLabel;
    };

    const getLabel = (name) => {

        var label = '';
        if (name !== undefined && name !== '') {
            label = name;
        } else {
            label = that.labelFromURI(obj.uri);
        }

        if (label.length > maxNameLength) {
            label = label.substring(0, maxNameLength-2) + '...';
        }

        return label;

    };


    const setClass = d => {
        if(d.type == 'rdfs:Class')
            return 'rdfs-class'
        return 'rdfs-class'    
    }


    const measureTextWidth = ( text, textStyle ) => {
        // Set a default value
        if ( !textStyle ) {
          textStyle = "text";
        }
        var d = d3.select("body")
            .append("div")
            .attr("class", textStyle)
            .attr("id", "width-test") // tag this element to identify it
            .attr("style", "position:absolute; float:left; white-space:nowrap; visibility:hidden;")
            .text(text),
          w = document.getElementById("width-test").offsetWidth;
        d.remove();
        return w;
      }


    // TODO: change *8 to a more stable width for labels
    const calcPropertyBoxWidth = name => measureTextWidth(getLabel(name) + 20) //getLabel(name).length * 8;



const drag = simulation => {


    const dragstarted = (event) => {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      event.subject.fx = event.subject.x;
      event.subject.fy = event.subject.y;
    }

    const dragged = (event) => {
      // simulation.restart()
      event.subject.fx = event.x;
      event.subject.fy = event.y;
    }
    
    const dragended = (event) => {
      // console.log("dragended")
      if (!event.active) {
        simulation.alphaTarget(0);
        // console.log("alpha to 0")
      }
      event.subject.fx = null;
      event.subject.fy = null;
    }

    return d3.drag().on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended);

}

    return {
        labelFromURI,
        getLabel,
        setClass,
        calcPropertyBoxWidth,
        defaultPropertyHeight,
        defaultRadius,
        drag
    }


}