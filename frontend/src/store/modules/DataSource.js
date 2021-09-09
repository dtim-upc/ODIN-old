import {odinApi} from "boot/axios";
import {ref} from "vue"

const state = {
  datasources: [],
  selectedDatasources: [],

  alignments: []
}
// mutations
const mutations = {
  setDatasources: (state, payload) => {

    if(payload === "") { // when no datasources, api answer ""
      state.datasources = []
    } else {
      state.datasources = payload
    }


  },

  addDatasource: (state, payload) => {
    state.datasources.push(payload)
  },

  pushSelectedDatasource: (state, payload) => {
    // state.selectedDatasources = payload
    // console.log("sent:")
    // console.log(payload)
    state.selectedDatasources.push(payload)
    // state.selectedDatasources = state.selectedDatasources.concat((payload))
    // console.log(state.selectedDatasources)
  },

  deleteDataSource:(state,payload) => {

    console.log(payload)
    console.log(state.selectedDatasources)
    let index = state.datasources.indexOf(payload)
    if(index > -1) {
      console.log("dele index")
      state.datasources.splice(index,1)
    } else {
      // //comes from a table
      // console.log("filter ")
      // console.log(payload)
      // state.selectedDatasources = state.selectedDatasources.filter(x => x.id != payload.key)


    }

  },

  deleteSelectedDatasource: (state, payload) => {

    console.log(payload)
    console.log(state.selectedDatasources)
    let index = state.selectedDatasources.indexOf(payload)
    if(index > -1) {
      console.log("dele index")
      state.selectedDatasources.splice(index,1)
    } else {
      //comes from a table
      // console.log("filter ")
      // console.log(payload)
      // state.selectedDatasources = state.selectedDatasources.filter(x => x.id != payload.id)
      //
      //
    }




  }


}

// actions
const actions = {

  async getDatasources({commit}) {

    console.log("hl")
    const res = await odinApi.get("/dataSource").then(response => {

      console.log("data received")
      console.log(response.data)
      commit("setDatasources", response.data)

    }).catch(err => {
      console.log("error retrieving data sources")
      console.log(err)
    })

  },

  updateSelectedDatasourcesI ({commit}, payload ){
    // console.log("entra")
    // console.log(payload)
    commit("pushSelectedDatasource", payload)

  },
  deleteSelectedDatasource ({commit}, payload ) {
    commit("deleteSelectedDatasource", payload)
  },
  deleteDatasource ({commit}, payload ) {
    commit("deleteDataSource", payload)
  },

  addDatasource({commit}, payload ) {
    commit("addDatasource", payload)
  }


}


// getters
const getters = {

  // getDatasources2: state => state.datasources



};


export default {
  // namespaced: true,
  state,
  getters,
  actions,
  mutations
}
