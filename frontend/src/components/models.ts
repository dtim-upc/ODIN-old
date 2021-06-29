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
  name:string;
  type:string;
}

export interface Wrapper {
  name:string;
  attributes:[string];
  dataSourcesId:string;
  dataSourcesLabel:string;
}
