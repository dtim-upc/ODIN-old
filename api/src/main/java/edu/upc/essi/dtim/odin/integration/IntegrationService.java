package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.jena.GraphJenaImpl;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.NextiaGraphy.nextiaGraphyModuleImpl;
import edu.upc.essi.dtim.odin.NextiaGraphy.nextiaGraphyModuleInterface;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationData;
import edu.upc.essi.dtim.odin.integration.pojos.JoinAlignment;
import edu.upc.essi.dtim.odin.project.Project;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IntegrationService {
    public IntegrationService(){

    }

    public Graph integrateData(GraphJenaImpl integratedGraph, Dataset dsB, List<Alignment> alignments) {
        Graph graphA = integratedGraph;
        Graph graphB = dsB.getLocalGraph();

        integrationModuleInterface integrationInterface = new integrationModuleImpl();
        Graph newIntegratedGraph = integrationInterface.integrate(graphA, graphB, alignments);

        //generamos el esquema visual del grafo y lo asignamos
        nextiaGraphyModuleInterface visualLibInterface = new nextiaGraphyModuleImpl();
        newIntegratedGraph.setGraphicalSchema(visualLibInterface.generateVisualGraph(newIntegratedGraph));

        return newIntegratedGraph;
    }

    public Project getProject(String projectId) {
        ProjectService projectService = new ProjectService();
        return projectService.findById(projectId);
    }

    public Project saveProject(Project project) {
        ProjectService projectService = new ProjectService();
        return projectService.saveProject(project);
    }

    public List<JoinAlignment> generateJoinAlignments(Graph graphA, Graph graphB, IntegrationData iData) {
        integrationModuleInterface integrationInterface = new integrationModuleImpl();

        List<Alignment> unusedAlignments = integrationInterface.getUnused();

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

    public Project updateGraphProject(Project project, Graph integratedGraph) {
        GraphJenaImpl integratedImpl = new GraphJenaImpl();
        integratedImpl.setGraph(integratedGraph.getGraph());
        project.setIntegratedGraph(integratedImpl);
        project.getIntegratedGraph().setGraphicalSchema(integratedGraph.getGraphicalSchema());
        return project;
    }
}
