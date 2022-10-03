package edu.upc.essi.dtim.odin.controller;


import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.nextiadi.exceptions.NoDomainForPropertyException;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.newBootstrapping.newDataSource;
import edu.upc.essi.dtim.odin.newBootstrapping.newDataSourceRepository;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary.Schema;
import static edu.upc.essi.dtim.nextiadi.config.Vocabulary.IntegrationClass;
import static edu.upc.essi.dtim.nextiadi.config.Vocabulary.IntegrationDProperty;

@Component
public class SurveyAlignments {

    @Autowired
    JenaConnection graph;

    @Autowired
    private newDataSourceRepository repository;

    double similarity = 0.8;

    List<Alignment> ds1_ds2;
    List<Alignment> ds1_ds2_ds3;
    List<Alignment> ds1_ds2_ds3_ds4;
    SurveyAlignments(){
        aligments_ds1_ds2_ds3_ds4();
        aligments_ds1_ds2_ds3();
        aligments_ds1_ds2();
    }


    public void aligments_ds1_ds2_ds3_ds4(){
        ds1_ds2_ds3_ds4 = new ArrayList<>();
        Alignment a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors");
        a.setIriB("_dsName_.Seq1.artworks.Seq2.contributors");
        a.setType("class");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_");
        a.setIriB("_dsName_");
        a.setType("class");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.medium");
        a.setIriB("_dsName_.Seq1.artworks.medium");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.url");
        a.setIriB("_dsName_.Seq1.artworks.url");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.title");
        a.setIriB("_dsName_.Seq1.artworks.title");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.acquisitionYear");
        a.setIriB("_dsName_.Seq1.artworks.acquisitionYear");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        // TODO: name of datasets
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.role");
        a.setIriB("_dsName_.Seq1.artworks.Seq2.contributors.role");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq2.creator.birth_date");
        a.setIriB("_dsName_.Seq1.artworks.Seq2.contributors.birthYear");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.gender");
        a.setIriB("_dsName_.Seq1.artworks.Seq2.contributors.gender");
        a.setType("datatype");
        ds1_ds2_ds3_ds4.add(a);

//        Alignment a = new Alignment();
//        a.setIriA("contributors_artists");
//        a.setIriB("creator");
//        a.setType("class");
//        ds1_ds2.add(a);
//        // TODO: name of datasets
//        a = new Alignment();
//        a.setIriA("artworks");
//        a.setIriB("cmoa");
//        a.setType("class");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("fc_fc");
//        a.setIriB("full_name");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("title");
//        a.setIriB("title");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("url");
//        a.setIriB("web_url");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("medium");
//        a.setIriB("medium");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("width");
//        a.setIriB("item_width");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("depth");
//        a.setIriB("item_depth");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("creditLine");
//        a.setIriB("credit_line");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("classification");
//        a.setIriB("classification");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("acquisitionYear");
//        a.setIriB("date_acquired");
//        a.setType("datatype");
//        ds1_ds2.add(a);
    }


    public void aligments_ds1_ds2_ds3(){
        ds1_ds2_ds3 = new ArrayList<>();
        Alignment a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors");
        a.setIriB("_dsName_.Seq2.creator");
        a.setType("class");
        ds1_ds2_ds3.add(a);
        // TODO: name of datasets
        a = new Alignment();
        a.setIriA("_dsName_.fc");
        a.setIriB("_dsName_.Seq2.creator.full_name");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_");
        a.setIriB("_dsName_");
        a.setType("class");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.title");
        a.setIriB("_dsName_.title");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.url");
        a.setIriB("_dsName_.web_url");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.medium");
        a.setIriB("_dsName_.medium");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.width");
        a.setIriB("_dsName_.item_width");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.depth");
        a.setIriB("_dsName_.item_depth");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.creditLine");
        a.setIriB("_dsName_.credit_line");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.classification");
        a.setIriB("_dsName_.classification");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.acquisitionYear");
        a.setIriB("_dsName_.date_acquired");
        a.setType("datatype");
        ds1_ds2_ds3.add(a);
//        Alignment a = new Alignment();
//        a.setIriA("contributors_artists");
//        a.setIriB("creator");
//        a.setType("class");
//        ds1_ds2.add(a);
//        // TODO: name of datasets
//        a = new Alignment();
//        a.setIriA("artworks");
//        a.setIriB("cmoa");
//        a.setType("class");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("fc_fc");
//        a.setIriB("full_name");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("title");
//        a.setIriB("title");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("url");
//        a.setIriB("web_url");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("medium");
//        a.setIriB("medium");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("width");
//        a.setIriB("item_width");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("depth");
//        a.setIriB("item_depth");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("creditLine");
//        a.setIriB("credit_line");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("classification");
//        a.setIriB("classification");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("acquisitionYear");
//        a.setIriB("date_acquired");
//        a.setType("datatype");
//        ds1_ds2.add(a);
    }


    public void aligments_ds1_ds2(){
        ds1_ds2 = new ArrayList<>();
        Alignment a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors");
        a.setIriB("_dsName_");
        a.setType("class");
        ds1_ds2.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.fc");
        a.setIriB("_dsName_.fc");
        a.setType("datatype");
        ds1_ds2.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.startLetter");
        a.setIriB("_dsName_.startLetter");
        a.setType("datatype");
        ds1_ds2.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.birthYear");
        a.setIriB("_dsName_.birthYear");
        a.setType("datatype");
        ds1_ds2.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.gender");
        a.setIriB("_dsName_.gender");
        a.setType("datatype");
        ds1_ds2.add(a);
        a = new Alignment();
        a.setIriA("_dsName_.Seq1.contributors.mda");
        a.setIriB("_dsName_.mda");
        a.setType("datatype");
        ds1_ds2.add(a);
//        ds1_ds2 = new ArrayList<>();
//        Alignment a = new Alignment();
//        a.setIriA("contributors");
//        a.setIriB("artists");
//        a.setType("class");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("fc");
//        a.setIriB("fc");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("startLetter");
//        a.setIriB("startLetter");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("birthYear");
//        a.setIriB("birthYear");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("gender");
//        a.setIriB("gender");
//        a.setType("datatype");
//        ds1_ds2.add(a);
//        a = new Alignment();
//        a.setIriA("mda");
//        a.setIriB("mda");
//        a.setType("datatype");
//        ds1_ds2.add(a);
    }

    public void getAlig(List<Alignment> listA,Graph gA, Graph gB, List<newDataSource> dsID, newDataSource dsB){

//        boolean foundB = false;
        for( Alignment a : listA  ) {
//            if( !a.getIriA().equals("_dsName_")) {

            // get iri or integrated
            dsID.forEach(ds -> {
                if(!a.getIriA().contains("http:")){
                    String posibleA = Schema.val() + ds.getId() +"/" + a.getIriA().replace("_dsName_", ds.getName());
                    if(containsSub(posibleA, gA)) {

                        if(a.getType().equals("class")){
                            a.setIriA(getSuperClass(posibleA, gA));
                        } else {
                            a.setIriA(getSuperProperty(posibleA, gA));
                        }
                    } else {
                        System.out.println("ERORORD a");
                    }
                }
            });

            String posibleB = Schema.val() + dsB.getId() +"/" + a.getIriB().replace("_dsName_", dsB.getName());
            if(containsSub(posibleB, gB)) {
                if(a.getType().equals("class")){
                    a.setIriB(getSuperClass(posibleB, gB));
                } else {
                    a.setIriB(getSuperProperty(posibleB, gB));
                }
            } else {
                System.out.println("ERORORD b");
            }
//            }
        }

    }

    public boolean containsSub(String iri, Graph g){
        String q = "PREFIX rdfs: <"+ RDFS.getURI()+"> " +
                "PREFIX rdf: <"+ RDF.getURI() +"> " +
                "SELECT * WHERE { " +
                " <"+iri+"> a ?o. " +
                " } ";
        ResultSet rsA = g.runAQuery(q);
        if(rsA.hasNext())
            return true;
        return false;

    }

    public String getSuperClass(String classS, Graph g) {

        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX rdf: <"+ RDF.getURI() +"> " +
                "SELECT ?superClass WHERE { <"+classS+"> rdfs:subClassOf ?superClass. ?superClass rdf:type <"+IntegrationClass.val()+">.}";
        ResultSet rsA = g.runAQuery(query);
        if(rsA.hasNext()) {
            QuerySolution solution = rsA.nextSolution();
            return solution.getResource("superClass").getURI();
        }
        return classS;

    }

    public String getSuperProperty(String propertyS, Graph g) {

        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX rdf: <"+ RDF.getURI() +"> " +
                "SELECT ?superProperty WHERE { <"+propertyS+"> rdfs:subPropertyOf ?superProperty. ?superProperty rdf:type <"+IntegrationDProperty.val()+">.}";
        ResultSet rsA = g.runAQuery(query);
        if(rsA.hasNext()) {
            QuerySolution solution = rsA.nextSolution();
            return solution.getResource("superProperty").getURI();
        }
        return propertyS;

    }


    public boolean check_ds1_ds2(Graph gA, Graph gB) {
        int contA = 0;
        int contB = 0;
        String q = "PREFIX rdfs: <"+ RDFS.getURI()+"> " +
                "PREFIX rdf: <"+ RDF.getURI() +"> " +
                "SELECT * WHERE { " +
                " ?s a ?o. " +
                " } ";

        ResultSet rsA = gA.runAQuery(q);
        contA = check(contA, rsA, true);
        ResultSet rsB = gB.runAQuery(q);
        contB = check(contB, rsB, false);


        double tmp = ds1_ds2.size()*similarity;
        if( contA >= tmp && contB >= tmp ){
            return true;
        }

        return false;
    }

    public int check(int cont, ResultSet rs, boolean isA){
        while (rs.hasNext()){

            QuerySolution r = rs.next();
            String localName = r.get("s").asResource().getLocalName();
            String label =  localName.substring(localName.lastIndexOf('.') + 1);

            Optional<Alignment> filter = ds1_ds2.stream().filter(a -> {
                boolean f = a.getIriA().equals(label);
                if(f){
                    if(isA){
                        a.setIriA( r.get("s").toString());
                    } else {
                        a.setIriB( r.get("s").toString());
                    }
                }
                return f;
            }).findFirst();
            if(filter.isPresent()){
                cont = cont +1;
            }
        }
        return cont;
    }


    public List<Alignment> getAlignments(Project project, List<newDataSource> dsAs ,newDataSource dsB){
        List<Alignment> alignments = new ArrayList<>();

        Graph graphA;
        if( project.getNumberOfDS().equals("1") ) {
            // datasource A is the only data source ingested in project
            List<newDataSource> listDS = repository.findAll(project.getId());
            graphA = graph.persistent().getGraph(listDS.get(0).getIri());
        } else {
            graphA = retrieveSchemaIntegration(project);
        }
        Graph graphB = graph.temporal().getGraph(dsB.getIri());

//        check_ds1_ds2(graphA, graphB);
//        System.out.println("hola");




        if(project.getNumberOfDS().equals("1")){
            aligments_ds1_ds2();
            getAlig(ds1_ds2,graphA, graphB, dsAs ,dsB);
            createILabels(ds1_ds2);
            return ds1_ds2;

        } else if( project.getNumberOfDS().equals("2") ) {
            aligments_ds1_ds2_ds3();
            getAlig(ds1_ds2_ds3,graphA, graphB, dsAs ,dsB);
            createILabels(ds1_ds2_ds3);
            return ds1_ds2_ds3;
        } else if( project.getNumberOfDS().equals("3") ) {
            aligments_ds1_ds2_ds3_ds4();
            getAlig(ds1_ds2_ds3_ds4,graphA, graphB, dsAs ,dsB);
            createILabels(ds1_ds2_ds3_ds4);
            return ds1_ds2_ds3_ds4;
        }



        return alignments;
    }

    public void createILabels(List<Alignment> al){

        for(Alignment a: al) {
            String nameA = a.getIriA().substring(a.getIriA().lastIndexOf('/') + 1);
            if(nameA.contains(".")) {
                nameA = nameA.substring(nameA.lastIndexOf('.') + 1);
            }
            a.setLabelA(nameA);
            String nameB = a.getIriB().substring(a.getIriB().lastIndexOf('/') + 1);
            if(nameB.contains(".")) {
                nameB = nameB.substring(nameB.lastIndexOf('.') + 1);
            }
            a.setLabelB(nameB);
            String label = nameA + "_" +nameB;
            a.setL(label);
        }

    }

//    public List<Alignment> getA_ds1_ds2(Graph gA, Graph gB){
//        List<Alignment> alig = new ArrayList<>();
//        String q = "PREFIX rdfs: <"+ RDFS.getURI()+"> " +
//                "PREFIX rdf: <"+ RDF.getURI() +"> " +
//                "SELECT * WHERE { " +
//                " ?s a ?o. " +
//                " } ";
//
//        ResultSet rsA = gA.runAQuery(q);
//
//        while (rs.hasNext()){
//
//            QuerySolution r = rs.next();
//            String localName = r.get("s").asResource().getLocalName();
//            String label =  localName.substring(localName.lastIndexOf('.') + 1);
//
//            boolean f = ds1_ds2.stream().filter(a -> a.getIriA().equals(label) ).findFirst().isPresent();
//            if(f){
//                cont = cont +1;
//            }
//        }
//        return cont;
//    }


    private Graph retrieveSchemaIntegration(Project p){

        Graph schemaInt = graph.persistent().getGraph(p.getSchemaIntegrationIRI());
        String querySTR = "SELECT ?graph WHERE {  <"+p.getSchemaIntegrationIRI()+"> <"+ DataSourceGraph.INTEGRATION_OF.val()+"> ?graph. }";

        ResultSet results  = graph.persistent().runAQuery(querySTR);
//        TODO: maybe some validation is needed. Compare the retrieve number of results against the project number of data sources
        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String gURI = solution.getResource("graph").getURI();
            schemaInt = schemaInt.unionG( graph.persistent().getGraph(gURI) );

        }
        return schemaInt;


    }



}