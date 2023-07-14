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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IntegrationService {
    public IntegrationService(){

    }

    public Graph integrateData(GraphJenaImpl integratedGraph, Dataset dsB, List<Alignment> alignments) {
        integrationModuleInterface integrationInterface = new integrationModuleImpl();
        Graph graphB = dsB.getLocalGraph();
        Graph newIntegratedGraph = integrationInterface.integrate(integratedGraph, graphB, alignments);

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

            String domainA = graphA.getDomainOfProperty( a.getIriA());
            String domainB = graphB.getDomainOfProperty( a.getIriB());

            String domainLA = graphA.getRDFSLabel(domainA);
            String domainLB = graphB.getRDFSLabel( domainB);

            j.setDomainA(domainA);
            j.setDomainB(domainB);
            j.setDomainLabelA(domainLA);
            j.setDomainLabelB(domainLB);

            joinProperties.add(j);

        }
        return joinProperties;
    }
}
