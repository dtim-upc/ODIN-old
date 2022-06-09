package edu.upc.essi.dtim.odin.models.rest;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TripleStore {
    String g;
    List<Triple> triples;

    public TripleStore(){
        triples = new ArrayList<>();
    }
}