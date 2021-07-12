module.exports = function (graph) {

    var saveGraphMenu = {},
        saveButton,
        loadingModule,
        exportMenu;

    saveGraphMenu.setup = function () {

        saveButton = d3.select("#save-button")
            .on("click", function (d) {
                var config = graph.options().defaultConfig();
                if(config.editorMode === "true"){
                    loadingModule=graph.options().loadingModule();
                    exportMenu = graph.options().exportMenu();
                    var object = new Object();
                    object.modified = graph.prepareChangesObject();
                    if(!object.modified.isModified)
                        object.ttl = exportMenu.exportTurtleText();

                    // Javier: Uncomment in next iteration when we added Jena into ODIN.
                    // For now we are just interested to save the graphical graph into mongodb

                    console.log("success");
                    saveGraphMenu.saveGraphicalGraph();
                    graph.resetOriginalLabels();
                    graph.options().alertModule().showAlert("Information","Graph saved");


                    // $.ajax({
                    //     type: "POST",
                    //     url: '/globalGraph/'+encodeURIComponent(loadingModule.currentGlobalGraph().namedGraph)+'/TTL',
                    //     data: object,
                    //     success: function(data) {
                    //         console.log("success");
                    //         saveGraphMenu.saveGraphicalGraph();
                    //         graph.resetOriginalLabels();
                    //         graph.options().alertModule().showAlert("Information","Graph saved");
                    //     }
                    // });
                } else if(config.selectSG_mode === "true"){
                    saveGraphMenu.saveSubGraph();
                }
            });
    };

    saveGraphMenu.saveSubGraph = function(){
        var subGraph = new Object();
        subGraph.selection = graph.prepareSelectionObject();
        subGraph.LAVMappingID = getParameterByName("LAVMappingID");
        subGraph.graphicalSubGraph = graph.prepareGraphicalSelObject();
        $.ajax({
            url: '/LAVMapping/subgraph',
            type: 'POST',
            data: subGraph,
            success: function(data) {
                graph.options().alertModule().showAlert("Information","Mappings saved");
            }
        });
    };

    saveGraphMenu.saveGraphicalGraph = function () {
        loadingModule = graph.options().loadingModule();
        exportMenu = graph.options().exportMenu();
        console.log("loadingModule.currentGlobalGraph()")
        console.log(loadingModule.currentGlobalGraph());
        console.log("DEBUG")
        console.log(graph.prepareChangesObject());
        console.log(typeof(exportMenu.exportTurtleText()));
        console.log(graph.deletedURIProperties())
        const modified = graph.prepareChangesObject().isModified 
                        || (graph.deletedURIClasses().length > 0)
                        || (graph.deletedURIProperties().length > 0);
        const data = {
            globalGraph: loadingModule.currentGlobalGraph(),
            isModified: modified,
            ttl: modified ? exportMenu.exportTurtleText() : "",
            deleted: {
              classes: graph.deletedURIClasses(),
              properties: graph.deletedURIProperties(),
            },
        };
        data.globalGraph.graphicalGraph = exportMenu.getJson();
        console.log({data})
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: odinApi+'/globalGraph/'+loadingModule.currentGlobalGraph().id,
            data: JSON.stringify(data)
        });
    };

    saveGraphMenu.hide = function (flag) {
        if(flag){
            d3.select("#c_save").style("display","none")
        }else{
            d3.select("#c_save").style("display","")
        }
    };

    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    return saveGraphMenu;
};
