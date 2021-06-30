String.prototype.replaceAll = function (search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};
module.exports = function () {
    var newOntologyCounter=1;
	var app = {},
		graph = webvowl.graph(),
		options = graph.graphOptions(),
		languageTools = webvowl.util.languageTools(),
		GRAPH_SELECTOR = "#graph",
        GUI_Container = "#graphmain",
	// Modules for the webvowl app
		exportMenu     = require("./menu/exportMenu")        (graph),
		filterMenu     = require("./menu/filterMenu")        (graph),
		gravityMenu    = require("./menu/gravityMenu")       (graph),
		modeMenu       = require("./menu/modeMenu")          (graph),
        debugMenu      = require("./menu/debugMenu")         (graph),
		ontologyMenu   = require("./menu/ontologyMenu")      (graph),
		pauseMenu      = require("./menu/pauseMenu")         (graph),
		resetMenu      = require("./menu/resetMenu")         (graph),
        saveGraphMenu  = require("./menu/saveGraphMenu")     (graph),
        clearSelectSG  = require("./menu/clearSelectSGMenu") (graph),
        clearQueryOMQ  = require("./menu/clearQueryOMQMenu") (graph),
        genSparqlOMQ   = require("./menu/GenSparqlOMQMenu")  (graph),
        searchMenu     = require("./menu/searchMenu")        (graph),
		navigationMenu = require("./menu/navigationMenu")    (graph),
        zoomSlider     = require("./menu/zoomSlider")        (graph),
		sidebar        = require("./sidebar")                (graph),
		leftSidebar    = require("./leftSidebar")            (graph),
        editSidebar    = require("./editSidebar")            (graph),
        configMenu     = require("./menu/configMenu")        (graph),
		loadingModule  = require("./loadingModule")          (graph),
        warningModule  = require("./warningModule")          (graph),
        alertModule    = require("./modules/alertModule")    (graph),
        directInputMod = require("./directInputModule")      (graph),
        MDMController  = require("./util/MDMController")     (graph),
        mdmConfig      = require("./config/mdmConfig")       (),


	// Graph modules
		colorExternalsSwitch 	 = webvowl.modules.colorExternalsSwitch(graph),
		compactNotationSwitch 	 = webvowl.modules.compactNotationSwitch(graph),
		datatypeFilter 			 = webvowl.modules.datatypeFilter(),
		disjointFilter 			 = webvowl.modules.disjointFilter(),
		focuser 				 = webvowl.modules.focuser(graph),
		emptyLiteralFilter		 = webvowl.modules.emptyLiteralFilter(),
		nodeDegreeFilter 		 = webvowl.modules.nodeDegreeFilter(filterMenu),
		nodeScalingSwitch 		 = webvowl.modules.nodeScalingSwitch(graph),
		objectPropertyFilter 	 = webvowl.modules.objectPropertyFilter(),
		pickAndPin 				 = webvowl.modules.pickAndPin(),
		selectionDetailDisplayer = webvowl.modules.selectionDetailsDisplayer(sidebar.updateSelectionInformation),
		statistics 				 = webvowl.modules.statistics(),
		subclassFilter 			 = webvowl.modules.subclassFilter(),
		setOperatorFilter 		 = webvowl.modules.setOperatorFilter(),
        selectionGraph           = webvowl.modules.selectionGraph(),
        selectionMarker          = webvowl.modules.selectionMarker();



	app.getOptions=function(){
	    return webvowl.opts;
    };
	app.getGraph=function(){
	    return webvowl.gr;
    };
	// app.afterInitializationCallback=undefined;




	app.initialize = function (viewType) {
        window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame || function(f){return setTimeout(f, 1000/60);}; // simulate calling code 60
        window.cancelAnimationFrame = window.cancelAnimationFrame || window.mozCancelAnimationFrame || function(requestID){clearTimeout(requestID);}; //fall back

        options.setEditorModeForDefaultObject(mdmConfig.getConf(viewType).editorMode);
        options.setEditModeForBdiVisualize(mdmConfig.getConf(viewType).bdi);
        options.setEditModeForTestIncDIVisualize(mdmConfig.getConf(viewType).testIncDI);
        options.setModeForSelectionSG(mdmConfig.getConf(viewType).selectSG);
        options.setModeForOMQ(mdmConfig.getConf(viewType).OMQ_mode);
        options.setModeBDIManualAlignment(mdmConfig.getConf(viewType).bdi_manualAl);

        options.graphContainerSelector(GRAPH_SELECTOR);
		options.selectionModules().push(focuser);
		options.selectionModules().push(selectionDetailDisplayer );
		options.selectionModules().push(pickAndPin);

        options.selectionMarker(selectionMarker);
        options.selectionGraph(selectionGraph);
        options.mdmController(MDMController);

		options.filterModules().push(emptyLiteralFilter);
        options.filterModules().push(statistics);

        options.filterModules().push(nodeDegreeFilter);
		options.filterModules().push(datatypeFilter);
		options.filterModules().push(objectPropertyFilter);
		options.filterModules().push(subclassFilter);
		options.filterModules().push(disjointFilter);
		options.filterModules().push(setOperatorFilter);
		options.filterModules().push(nodeScalingSwitch);
		options.filterModules().push(compactNotationSwitch);
		options.filterModules().push(colorExternalsSwitch);

        d3.select(window).on("resize", adjustSize);

		exportMenu.setup();
		gravityMenu.setup();
		filterMenu.setup(datatypeFilter, objectPropertyFilter, subclassFilter, disjointFilter, setOperatorFilter, nodeDegreeFilter);
        modeMenu.setup(pickAndPin, nodeScalingSwitch, compactNotationSwitch, colorExternalsSwitch, selectionMarker);
		pauseMenu.setup();
		saveGraphMenu.setup();
        clearSelectSG.setup();
        clearQueryOMQ.setup();
        genSparqlOMQ.setup();
		sidebar.setup();
		loadingModule.setup();
		leftSidebar.setup();
		editSidebar.setup();
		debugMenu.setup();
        var agentVersion=getInternetExplorerVersion();
       	if (agentVersion> 0 && agentVersion<= 11) {
            console.log("Agent version "+agentVersion);
        	console.log("This agent is not supported");
			d3.select("#browserCheck").classed("hidden", false);
			d3.select("#killWarning" ).classed("hidden", true);
			d3.select("#optionsArea" ).classed("hidden", true);
			d3.select("#logo").classed("hidden", true);
        } else {
			// d3.select("#logo").classed("hidden", false); //original
            d3.select("#logo").classed("hidden", true);
			if (agentVersion===12) {
				// allow Mircosoft Edge Browser but with warning
                d3.select("#browserCheck").classed("hidden", false);
                d3.select("#killWarning").classed("hidden", false);
            } else {
                d3.select("#browserCheck").classed("hidden", true);
            }

            resetMenu.setup([gravityMenu, filterMenu, modeMenu, focuser, selectionDetailDisplayer, pauseMenu]);
			searchMenu.setup();
			navigationMenu.setup();
			zoomSlider.setup();

			// give the options the pointer to the some menus for import and export
			options.literalFilter(emptyLiteralFilter);
			options.nodeDegreeFilter(nodeDegreeFilter);
			options.loadingModule(loadingModule);
			options.filterMenu(filterMenu);
			options.modeMenu(modeMenu);
			options.gravityMenu(gravityMenu);
			options.pausedMenu(pauseMenu);
			options.pickAndPinModule(pickAndPin);
			options.resetMenu(resetMenu);
			options.saveGraphMenu(saveGraphMenu);
			options.clearSelectSGMenu(clearSelectSG);
			options.clearQueryOMQMenu(clearQueryOMQ);
            options.generateSparqlOMQMenu(genSparqlOMQ);
			options.searchMenu(searchMenu);
			options.ontologyMenu(ontologyMenu);
			options.navigationMenu(navigationMenu);
			options.sidebar(sidebar);
            options.leftSidebar(leftSidebar);
            options.editSidebar(editSidebar);
			options.exportMenu(exportMenu);
			options.graphObject(graph);
			options.zoomSlider(zoomSlider);
			options.warningModule(warningModule);
            options.alertModule(alertModule);
			options.directInputModule(directInputMod);
            options.datatypeFilter(datatypeFilter);
            options.objectPropertyFilter(objectPropertyFilter);
            options.subclassFilter(subclassFilter);
            options.setOperatorFilter(setOperatorFilter);
            options.disjointPropertyFilter(disjointFilter);
            options.focuserModule(focuser);
            options.colorExternalsModule(colorExternalsSwitch);
            options.compactNotationModule(compactNotationSwitch);

            ontologyMenu.setup(loadOntologyFromText);
            configMenu.setup();
            MDMController.setup();

            leftSidebar.showSidebar(0);
            leftSidebar.hideCollapseButton(true);


            graph.start();

            var modeOp = d3.select("#modeOfOperationString");
            modeOp.style("font-size","0.6em");
            modeOp.style("font-style","italic");

			adjustSize();
			var defZoom;
			var w = graph.options().width();
			var h = graph.options().height();
			// defZoom = Math.min(w, h) / 1000;
            defZoom = Math.min(w, h) / 860;

            var hideDebugOptions = true;
            if (hideDebugOptions === false) {
                graph.setForceTickFunctionWithFPS();
            }

			graph.setDefaultZoom(defZoom);
            d3.selectAll(".debugOption").classed("hidden",hideDebugOptions);

			// prevent backspace reloading event
            var htmlBody=d3.select("body");
            d3.select(document).on("keydown", function (e) {
                if (d3.event.keyCode === 8 && d3.event.target===htmlBody.node() ) {
                	// we could add here an alert
                    d3.event.preventDefault();
                }
                // using ctrl+Shift+d as debug option
                if (d3.event.ctrlKey && d3.event.shiftKey && d3.event.keyCode===68) {
                    graph.options().executeHiddenDebugFeatuers();
                    d3.event.preventDefault();
                }
            });
            if (d3.select("#maxLabelWidthSliderOption")) {
                var setValue=!graph.options().dynamicLabelWidth();
                d3.select("#maxLabelWidthSlider").node().disabled=setValue;
                d3.select("#maxLabelWidthvalueLabel").classed("disabledLabelForSlider", setValue);
                d3.select("#maxLabelWidthDescriptionLabel").classed("disabledLabelForSlider", setValue);
            }

            d3.select("#blockGraphInteractions").style("position","absolute")
                .style("top","0")
                .style("background-color","#bdbdbd")
                .style("opacity","0.5")
                .style("pointer-events","auto")
                .style("width",graph.options().width()+"px")
                .style("height",graph.options().height()+"px")
                .on("click",function(){
                    d3.event.preventDefault();
                    d3.event.stopPropagation();
                })
                .on("dblclick",function(){
                    d3.event.preventDefault();
                    d3.event.stopPropagation();
                });

            d3.select("#direct-text-input").on("click", function () {
                directInputMod.setDirectInputMode();
            });
            d3.select("#blockGraphInteractions").node().draggable = false;
            options.prefixModule(webvowl.util.prefixTools(graph));
            adjustSize();
            sidebar.updateOntologyInformation(undefined, statistics);
            loadingModule.parseUrlAndLoadOntology(); // loads automatically the ontology provided by the parameters
            if(options.defaultConfig().selectSG_mode === "true")
                graph.loadGraphicalSelection();
            options.debugMenu(debugMenu);
            debugMenu.updateSettings();

            // connect the reloadCachedVersionButton
            d3.select("#reloadSvgIcon").on("click",function(){
                if(d3.select("#reloadSvgIcon").node().disabled===true){
                  graph.options().ontologyMenu().clearCachedVersion();
                  return;
                }
                d3.select("#reloadCachedOntology").classed("hidden",true);
                graph.options().ontologyMenu().reloadCachedOntology();

            });
            // add the initialized objects
            webvowl.opts=options;
            webvowl.gr=graph;

        }
	};


	function loadOntologyFromText(jsonText, filename, alternativeFilename) {
        d3.select("#reloadCachedOntology").classed("hidden",true);
		pauseMenu.reset();
		graph.options().navigationMenu().hideAllMenus();

		if ((jsonText===undefined && filename===undefined) || (jsonText.length===0)){
            loadingModule.notValidJsonFile();
			return;
		}
		graph.editorMode(); // updates the checkbox
		var data;
		if (jsonText) {
			// validate JSON FILE
			var validJSON;
			try {
				data =JSON.parse(jsonText);
				validJSON=true;
			} catch (e){
				validJSON=false;
			}
			if (validJSON===false){
				// the server output is not a valid json file
                loadingModule.notValidJsonFile();
				return;
			}

			if (!filename) {
				// First look if an ontology title exists, otherwise take the alternative filename
				var ontologyNames = data.header ? data.header.title : undefined;
				var ontologyName = languageTools.textInLanguage(ontologyNames);

				if (ontologyName) {
					filename = ontologyName;
				} else {
					filename = alternativeFilename;
				}
			}
		}


		// check if we have graph data
        var classCount =0;
		if (data.class!==undefined){
			classCount=data.class.length;
		}

		var loadEmptyOntologyForEditing=false;
        if (location.hash.indexOf("#new_ontology")!==-1){
            loadEmptyOntologyForEditing=true;
            newOntologyCounter++;
            d3.select("#empty").node().href="#opts=editorMode=true;#new_ontology"+newOntologyCounter;
        }
		if (classCount === 0 && graph.editorMode()===false && loadEmptyOntologyForEditing===false){
			// generate message for the user;
			loadingModule.emptyGraphContentError();
		}else {
            loadingModule.validJsonFile();
            ontologyMenu.setCachedOntology(filename, jsonText);
            exportMenu.setJsonText(jsonText);
            options.data(data);
            graph.options().loadingModule().setPercentMode();
            if (loadEmptyOntologyForEditing===true){
                graph.editorMode(true);

            }
            graph.load();
            sidebar.updateOntologyInformation(data, statistics);
            exportMenu.setFilename(filename);
            graph.updateZoomSliderValueFromOutside();
            adjustSize();
            if(options.showModesGui())
                var flagOfCheckBox=d3.select("#editorModeModuleCheckbox").node().checked;
            else
                var flagOfCheckBox=options.defaultConfig().editorMode==="true";
            graph.editorMode(flagOfCheckBox);// update gui

        }
	}

	function adjustSize() {
	    var dist_bottom_bar = 40;
	    var dist_nav = 0;
	    //changes for MDM gui
        //TO DO: add a global option to enable navbar and navbar selector.
        var navbar = d3.select(".navbar");
        if(!navbar.empty()){
            dist_nav = parseInt(navbar.style("height"),10);
            dist_bottom_bar = dist_bottom_bar + dist_nav;
            //padding for gui container
            d3.select(GUI_Container).style("padding-top",dist_nav+"px");
            d3.select("#detailsArea").style("padding-top",dist_nav+"px");
        }

	    //size of canvas
		var graphContainer = d3.select(GRAPH_SELECTOR),
			svg = graphContainer.select("svg"),
			height = window.innerHeight - dist_bottom_bar,
			width = window.innerWidth - (window.innerWidth * 0.22);

		if (sidebar.getSidebarVisibility()==="0"){
            height = window.innerHeight - dist_bottom_bar ;
            width = window.innerWidth;
        }

        directInputMod.updateLayout();
        d3.select("#blockGraphInteractions").style("width",window.innerWidth+"px");
        d3.select("#blockGraphInteractions").style("height",height+"px");
        d3.select("#blockGraphInteractions").style("padding-top",dist_nav+"px");

        d3.select("#WarningErrorMessagesContainer").style("width",width+"px");
        d3.select("#WarningErrorMessagesContainer").style("height",height+"px");
        d3.select("#WarningErrorMessagesContainer").style("padding-top",dist_nav+"px");

        d3.select("#WarningErrorMessages").style("max-height",(height-12)+"px");

        d3.select("#AlertMessagesContainer").style("width",width+"px");
        d3.select("#AlertMessagesContainer").style("height",height+"px");
        d3.select("#AlertMessagesContainer").style("padding-top",dist_nav+"px");

        d3.select("#AlertMessages").style("max-height",(height-12)+"px");

        graphContainer.style("height", height + "px");
		svg.attr("width", width)
			.attr("height", height);

		options.width ( width  )
			   .height( height );

		graph.updateStyle();

        if (isTouchDevice()===true){
            if (graph.isEditorMode()===true )
                d3.select("#modeOfOperationString").node().innerHTML="touch able device detected";
            graph.setTouchDevice(true);

        }else{
            if (graph.isEditorMode()===true )
                d3.select("#modeOfOperationString").node().innerHTML="point & click device detected";
            graph.setTouchDevice(false);
        }

        d3.select("#loadingInfo-container").style("height",0.5*(height-80)+"px");
        loadingModule.checkForScreenSize();

		adjustSliderSize();
		// update also the padding options of loading and the logo positions;
		var warningDiv=d3.select("#browserCheck");
		if (warningDiv.classed("hidden")===false ) {
            var offset=10+warningDiv.node().getBoundingClientRect().height;
            d3.select("#logo").style("padding", offset+"px 10px");
        }else {
			// remove the dynamic padding from the logo element;
            d3.select("#logo").style("padding", "10px");
        }

        // scrollbar tests;
		var element =d3.select("#menuElementContainer").node();
        var maxScrollLeft = element.scrollWidth - element.clientWidth;
        var leftButton=d3.select("#scrollLeftButton");
        var rightButton=d3.select("#scrollRightButton");
        if (maxScrollLeft>0){
        	// show both and then check how far is bar;
         	rightButton.classed("hidden",false);
            leftButton.classed("hidden",false);
            navigationMenu.updateScrollButtonVisibility();
         }else{
        	// hide both;
            rightButton.classed("hidden",true);
            leftButton.classed("hidden",true);
		}

		// adjust height of the leftSidebar element;
        editSidebar.updateElementWidth();
	}

    function adjustSliderSize(){
		// TODO: refactor and put this into the slider it self
        var height = window.innerHeight - 40;
        var fullHeight=height;
        var zoomOutPos=height-30;
        var sliderHeight=150;

        // assuming DOM elements are generated in the index.html
        // todo: refactor for independent usage of graph and app
        if (fullHeight<150) {
            // hide the slider button;
            d3.select("#zoomSliderParagraph").classed("hidden", true);
            d3.select("#zoomOutButton").classed("hidden", true);
            d3.select("#zoomInButton").classed("hidden", true);
            d3.select("#centerGraphButton").classed("hidden", true);
            return;
        }
        d3.select("#zoomSliderParagraph").classed("hidden",false);
        d3.select("#zoomOutButton").classed("hidden",false);
        d3.select("#zoomInButton").classed("hidden",false);
        d3.select("#centerGraphButton").classed("hidden",false);

        var zoomInPos=zoomOutPos-20;
        var centerPos=zoomInPos-20;
        if (fullHeight<280){
            // hide the slider button;
            d3.select("#zoomSliderParagraph").classed("hidden",true);//var sliderPos=zoomOutPos-sliderHeight;
            d3.select("#zoomOutButton").style("top",zoomOutPos+"px");
            d3.select("#zoomInButton").style("top",zoomInPos+"px");
            d3.select("#centerGraphButton").style("top",centerPos+"px");
            return;
        }

        var sliderPos=zoomOutPos-sliderHeight;
        zoomInPos=sliderPos-20;
        centerPos=zoomInPos-20;
        d3.select("#zoomSliderParagraph").classed("hidden",false);
        d3.select("#zoomOutButton").style("top",zoomOutPos+"px");
        d3.select("#zoomInButton").style("top",zoomInPos+"px");
        d3.select("#centerGraphButton").style("top",centerPos+"px");
        d3.select("#zoomSliderParagraph").style("top",sliderPos+"px");
    }

    function isTouchDevice() {
        try {
            document.createEvent("TouchEvent");
            return true;
        } catch (e) {
            return false;
        }
    }


	function getInternetExplorerVersion(){
        var ua,
            re,
            rv = -1;

        // check for edge
        var isEdge=/(?:\b(MS)?IE\s+|\bTrident\/7\.0;.*\s+rv:|\bEdge\/)(\d+)/.test(navigator.userAgent);
        if (isEdge){
            rv  = parseInt("12");
            return rv;
        }

        var isIE11 = /Trident.*rv[ :]*11\./.test(navigator.userAgent);
        if (isIE11){
            rv  = parseInt("11");
            return rv;
        }
        if (navigator.appName === "Microsoft Internet Explorer") {
            ua = navigator.userAgent;
            re = new RegExp("MSIE ([0-9]{1,}[\\.0-9]{0,})");
            if (re.exec(ua) !== null) {
                rv = parseFloat(RegExp.$1);
            }
        } else if (navigator.appName === "Netscape") {
            ua = navigator.userAgent;
            re = new RegExp("Trident/.*rv:([0-9]{1,}[\\.0-9]{0,})");
            if (re.exec(ua) !== null) {
                rv = parseFloat(RegExp.$1);
            }
        }
        return rv;
	}

	return app;
};
