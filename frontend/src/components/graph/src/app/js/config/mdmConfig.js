module.exports = function () {
    var config = {},
        global_Graph_Edit = {
            editorMode: "true",
            selectSG: "false",
            OMQ_mode: "false",
            source_graph: "false",
            bdi_manualAl: "false",
            testIncDI: "false"
        },
        mappings_Graph_select = {
            editorMode: "false",
            selectSG: "true",
            OMQ_mode: "false",
            source_graph: "false",
            bdi_manualAl: "false",
            testIncDI: "false"
        },
        omq = {
            editorMode: "false",
            selectSG: "false",
            OMQ_mode: "true",
            source_graph: "false",
            bdi_manualAl: "false",
            testIncDI: "false"
        },
        source_graph = { //bdi_visualization
            source_graph: "true",
            editorMode: "false",
            selectSG: "false",
            OMQ_mode: "false",
            bdi_manualAl: "false",
            testIncDI: "false"
        },
        bdi_manual_alignments = {
            source_graph: "false",
            editorMode: "false",
            selectSG: "false",
            OMQ_mode: "false",
            bdi_manualAl: "true"
        },
        test_visualization = { //test for incremental algorithm
            source_graph: "false",
            editorMode: "false",
            selectSG: "false",
            OMQ_mode: "false",
            bdi_manualAl: "false",
            testIncDI: "true"
        },
        default_Option = {
            editorMode: "false",
            selectSG: "false",
            OMQ_mode: "false",
            source_graph: "false",
            bdi_manualAl: "false",
            testIncDI: "false"
        };

    config.getConf = function (type) {
        switch (type) {
            case "mappings_Graph_select":
                return mappings_Graph_select;
            case "global_Graph_Edit":
                return global_Graph_Edit;
            case "omq":
                return omq;
            case "source_graph":
                return source_graph;
            case "bdi_manual_alignments":
                return bdi_manual_alignments;
            case "test_visualization":
                return test_visualization
            default:
                return default_Option;
        }
        return global_Graph_Edit;
    };

    config.global_Graph_Edit = function () {
        return global_Graph_Edit;
    };
    config.mappings_Graph_select = function () {
        return mappings_Graph_select;
    };
    config.default_Option = function () {
        return default_Option;
    };
    config.omq = function () {
        return omq;
    };
    config.source_graph = function () {
        return source_graph;
    };
    config.bdi_manual_alignments = function () {
        return bdi_manual_alignments;
    };
    return config;

};
