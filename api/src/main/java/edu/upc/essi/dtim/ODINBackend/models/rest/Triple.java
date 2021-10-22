package edu.upc.essi.dtim.ODINBackend.models.rest;

import lombok.Data;

@Data
public class Triple{

    String g;
    String s;
    String p;
    String o;
}