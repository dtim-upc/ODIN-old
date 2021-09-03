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
    state.datasources = payload
  },
  pushSelectedDatasource: (state, payload) => {
    // state.selectedDatasources = payload
    // console.log("sent:")
    // console.log(payload)
    state.selectedDatasources.push(payload)
    // state.selectedDatasources = state.selectedDatasources.concat((payload))
    // console.log(state.selectedDatasources)
  },

  deleteSelectedDatasource: (state, payload) => {
    console.log("dele")
    console.log(payload)
    console.log(state.selectedDatasources)
    let index = state.selectedDatasources.indexOf(payload)
    if(index > -1) {
      state.selectedDatasources.splice(index,1)
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
