export interface Todo {
  id: number;
  content: string;
}

export interface Meta {
  totalCount: number;
}

export interface GlobalGraph {

  id:string;
  name:string;
  namedGraph:string;
  graphicalGraph:string;
  namespace:string;

}

export interface DataSources {
  id:string;
  name:string;
  type:string;
}

export interface Wrapper {
  id:string;
  name:string;
  attributes:[Attribute];
  dataSourcesId:string;
  dataSourcesLabel:string;
}

export interface LavMapping {
  id:string;
  wrapperId:string;
  globalGraphId:string;
  saveAs:[SaveAs];
  graphicalSubgraph: string;
}

export interface SaveAs {
  features: string;
  attribute: string;
}

export interface Attribute {
  isID:boolean;
  name:string;
}