package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.jena.GraphJenaImpl;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.NextiaGraphy.nextiaGraphyModuleImpl;
import edu.upc.essi.dtim.odin.NextiaGraphy.nextiaGraphyModuleInterface;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationData;
import edu.upc.essi.dtim.odin.integration.pojos.JoinAlignment;
import edu.upc.essi.dtim.odin.project.Project;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IntegrationService {
    private final AppConfig appConfig;

    public IntegrationService(@Autowired AppConfig appConfig){
        this.appConfig = appConfig;
        //this.projec
    }

    public Graph integrateData(GraphJenaImpl integratedGraph, Dataset dsB, List<Alignment> alignments) {
        String graphNameB = dsB.getLocalGraph().getGraphName();
        GraphStoreInterface graphStoreInterface;
        try {
            graphStoreInterface = GraphStoreFactory.getInstance(appConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Graph localGraph = graphStoreInterface.getGraph(graphNameB);
        dsB.setLocalGraph((GraphJenaImpl) localGraph);

        Graph graphB = dsB.getLocalGraph();

        integrationModuleInterface integrationInterface = new integrationModuleImpl();
        Graph newIntegratedGraph = integrationInterface.integrate(integratedGraph, graphB, alignments);

        //generamos el esquema visual del grafo y lo asignamos
        nextiaGraphyModuleInterface visualLibInterface = new nextiaGraphyModuleImpl();
        String newGraphicalSchema = visualLibInterface.generateVisualGraph(newIntegratedGraph);
        newIntegratedGraph.setGraphicalSchema(newGraphicalSchema);

        return newIntegratedGraph;
    }

    public Project getProject(String projectId) {
        ProjectService projectService = new ProjectService(appConfig);

        return projectService.findById(projectId);
    }

    public Project saveProject(Project project) {
        ProjectService projectService = new ProjectService(appConfig);
        return projectService.saveProject(project);
    }

    public List<JoinAlignment> generateJoinAlignments(Graph graphA, Graph graphB, IntegrationData iData) {
        integrationModuleInterface integrationInterface = new integrationModuleImpl();

        List<Alignment> unusedAlignments = integrationInterface.getUnused(graphA, graphB, iData.getAlignments());

        Set<String> alignmentsAB = iData.getAlignments().stream()
                .filter(a -> a.getType().equals("datatype"))
                .map( al -> al.getIriA() + al.getIriB() )
                .collect(Collectors.toSet());

        List<Alignment> potentialJoinAlignments =
                unusedAlignments.stream()
                        .filter(e -> alignmentsAB.contains(e.getIriA()+e.getIriB()))
                        .collect(Collectors.toList());

        List<JoinAlignment> joinProperties = new ArrayList<>();
        for( Alignment a : potentialJoinAlignments) {

            JoinAlignment j = new JoinAlignment();
            j.wrap(a);

            String domainA = getDomainOfProperty(graphA, a.getIriA());
            String domainB = getDomainOfProperty(graphB, a.getIriB());

            String domainLA = getRDFSLabel(graphA, domainA);
            String domainLB = getRDFSLabel(graphB, domainB);

            j.setDomainA(domainA);
            j.setDomainB(domainB);
            j.setDomainLabelA(domainLA);
            j.setDomainLabelB(domainLB);

            joinProperties.add(j);

        }
        return joinProperties;
    }

    private String getDomainOfProperty(Graph graph, String propertyIRI) {
        String query = " SELECT ?domain WHERE { <"+propertyIRI+"> <"+ RDFS.domain.toString()+"> ?domain. }";
        List<Map<String, Object>> res = graph.query(query);
        if(!res.isEmpty()){
            return res.get(0).get("domain").toString();
        }
        return null;
    }

    private String getRDFSLabel(Graph graph, String resourceIRI) {
        String query = " SELECT ?label WHERE { <"+resourceIRI+"> <"+ RDFS.label.toString()+"> ?label. }  ";
        List<Map<String, Object>> res = graph.query(query);
        if(!res.isEmpty()){
            return res.get(0).get("label").toString();
        }
        return null;
    }

    public Project updateIntegratedGraphProject(Project project, Graph integratedGraph) {
        GraphJenaImpl integratedImpl = new GraphJenaImpl();
        integratedImpl.setGraph(integratedGraph.getGraph());
        project.setIntegratedGraph(integratedImpl);
        project.getIntegratedGraph().setGraphicalSchema(integratedGraph.getGraphicalSchema());
        return project;
    }

    public Project updateGlobalGraphProject(Project project, Graph globalGraph) {
        GraphJenaImpl globalImpl = new GraphJenaImpl();
        globalImpl.setGraph(globalGraph.getGraph());
        project.setGlobalGraph(globalImpl);
        project.getGlobalGraph().setGraphicalSchema(globalGraph.getGraphicalSchema());
        return project;
    }

    public Graph generateGlobalGraph(GraphJenaImpl integratedGraph, Dataset dsB, List<Alignment> alignments) {
        //PARTE DE LA INTEGRACIÓN PARA PODER GENERAR EL GLOBAL POR CONTEXTO
        String graphNameB = dsB.getLocalGraph().getGraphName();
        GraphStoreInterface graphStoreInterface;
        try {
            graphStoreInterface = GraphStoreFactory.getInstance(appConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Graph localGraph = graphStoreInterface.getGraph(graphNameB);
        dsB.setLocalGraph((GraphJenaImpl) localGraph);

        Graph graphB = dsB.getLocalGraph();
        /////////////////////////////////////////////////////////////////////

        integrationModuleInterface integrationInterface = new integrationModuleImpl();
        Graph globalGraph = integrationInterface.globalGraph(integratedGraph, graphB, alignments);
        globalGraph.write("..\\api\\dbFiles\\ttl"+"/globalGraph.ttl");
        System.out.println("+++++++++++++++++++++++++++++++++++++++ GLOBAL GRAPH GENERATED");

        //generamos el esquema visual del grafo y lo asignamos
        nextiaGraphyModuleInterface visualLibInterface = new nextiaGraphyModuleImpl();
        String newGraphicalSchema = visualLibInterface.generateVisualGraph(globalGraph);
        globalGraph.setGraphicalSchema(newGraphicalSchema);

        return globalGraph;
    }
}
