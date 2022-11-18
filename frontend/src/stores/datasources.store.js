import { defineStore } from 'pinia'
import {useNotify} from 'src/use/useNotify.js'
// import {odinApi} from "boot/axios";
import api from "src/api/dataSourcesAPI.js";
import { useAuthStore } from 'stores/auth.store.js'
import { useRoute, useRouter } from "vue-router";
import projectAPI from 'src/api/projectAPI';
import { useIntegrationStore } from 'src/stores/integration.store.js'
import download from 'downloadjs'
// const notify = useNotify()


export const useDataSourceStore = defineStore('datasource',{

    state: () => ({
        project : {},
        datasources: [],
    }),

    getters : {

      getDatasourcesNumber(state){
        return state.datasources.length
      },

      getGlobalSchema(state){

        if(state.project.graphicalGlobalSchema)
          return state.project.graphicalGlobalSchema
        return ""  
      },
      getGraphicalSchemaIntegration(state){
        if(state.project.graphicalSchemaIntegration)
          return state.project.graphicalSchemaIntegration
         return "" 
      },
    },
    actions: {

      async init(){
        
        // const router = useRouter()
                // console.log("***INIT*",router)
        // console.log("ds stores init...")
        // const authStore = useAuthStore()
        // const route = useRoute()
        // if(authStore.user.accessToken && !this.project.name){
        //   const response = await projectAPI.getProjectByID(route.params.id, authStore.user.accessToken)
        //     if(response.status == 200) {
        //       this.project = response.data
        //       // console.log("project assigned ", this.project)
        //     } else {
        //       console.log("something wrong with response: ", response)
        //     }
        // }
        // if(authStore.user.accessToken && this.datasources.length === 0) {
        //   console.log("retrieving persistent data sources...")
        //    this.getDatasources()
        //  }
      },
      async setProject(proj){
        const route = useRoute()
        const authStore = useAuthStore()
        const integrationStore = useIntegrationStore()
        console.log("setting project to datasources store", proj)

        if(proj ){ // if no proj provided 
          console.log("if proj")
          this.project = proj
          integrationStore.setProject(proj)

          
        } else if(!this.project.name || this.project.id != route.params.id) {
          console.log("dfs", route.params.id)
          const response = await projectAPI.getProjectByID(route.params.id, authStore.user.accessToken)

            if(response.status == 200){
              this.project = response.data
              integrationStore.setProject(this.project)
            }

        }
        
        // && this.datasources.length === 0
        if(authStore.user.accessToken ) {
          console.log("retrieving persistent data sources...")
           this.getDatasources()
         }  
         return this.project;
      },
      async getTriples(project, dsID){
        // TODO: change pinias to setup structure, route is only supported one time in this structure. Changing will make things easier
        //https://stackoverflow.com/questions/71249575/i-cant-access-my-routes-from-the-store-pinia-vuejs3
        
        const authStore = useAuthStore()
        
        // console.log("***")
        // console.log("dfs", route.params.id)
        // console.log("gettriples", route.params.id)
        let response = await api.getTriples(project.id, dsID ,authStore.user.accessToken)
        return response.data
        // .then((response => {

        //   console.log("response",response)
        //   if(response.status == 200){
        //     return response.data
        //   }
        //   return []

        // }))
        


      },

      async updateProjectInfo(){
        console.log("updating project info")
        const authStore = useAuthStore()
        const integrationStore = useIntegrationStore()
        const response = await projectAPI.getProjectByID(this.project.id, authStore.user.accessToken)

            if(response.status == 200){
              this.project = response.data
              integrationStore.setProject(this.project)
            }
      },


        async getDatasources() {
          const notify  = useNotify()
          const authStore = useAuthStore()
            console.log("Pinia getting data sources...")
            const res = await api.getAll(this.project.id, authStore.user.accessToken).then(response => {
        
              console.log("ds received", response.data)
      
              if(response.data === "") { // when no datasources, api answer ""
                this.datasources = []
              } else {
                this.datasources = response.data
              }      

              console.log(this.datasources)
            }).catch(err => {
              console.log("error retrieving data sources")
              // check how to get err status e.g., 401
              console.log(err)
              notify.negative("Cannot conect to the server.")
            })
        
        },

        persistDataSource(datasource){
          // const router2 = useRouter();
          const notify  = useNotify()
          const authStore = useAuthStore()
          const integrationStore = useIntegrationStore()

            

            console.log("persist data source...", datasource)

            api.createDSPersistent(this.project.id,datasource, authStore.user.accessToken)
            .then((response) => {
              console.log("createPersistentDS()",response)
              if (response.status == 201) {
  
                
                this.datasources.push(response.data)

                // remove from temporal
                
                
                integrationStore.finishIntegration(datasource)
                //to update project info
                this.updateProjectInfo()
                

                // we use go since the user can come from home or table sources pages
                this.router.go(-1)
                // this.router.push({name:"datasources"})

              } else {
                // console.log("error")
                notify.negative("Cannot integrate datasource with project. Something went wrong in the server.")
              }
            }).catch( (error) => {
              console.log("error integrating ds with project: ", error)
            notify.negative("Something went wrong in the server.")
          });

            // console.log(ds)
            // this.datasources.push(ds)
            // console.log(this.datasources)
        },

        deleteDataSource(ds){
          const authStore = useAuthStore()
          const notify  = useNotify()
          api.deleteDS(this.project.id,ds.id,authStore.user.accessToken)
          .then((response) => {
            if (response.status == 204) {
              notify.positive("Successfully deleted")
              // storeDS.deleteDataSource(ds)
              
              let index = this.datasources.indexOf(ds)
              if(index > -1) {
                  console.log("dele index")
                  this.datasources.splice(index,1)
                  
              } 
              this.updateProjectInfo()
            } else {
              // 500
              notify.negative("Something went wrong in the server.")
            }
          }).catch(err => {
            console.log("error deleting data sources")
            // check how to get err status e.g., 401
            console.log(err)
            notify.negative("Cannot delete data source. Error in the server.")
          })
      


        },
        async downloadSource(dsID){
          console.log("download....",dsID)

            

          const authStore = useAuthStore()
          const notify  = useNotify()
          const response = await api.downloadSourceGraph(this.project.id,dsID,authStore.user.accessToken);

          const content = response.headers['content-type'];
          download(response.data, "prueba.ttl", content)


          // const path = Path.resolve(__dirname, 'prueba.ttl')
          // const writer = Fs.createWriteStream(path)

          // response.data.pipe(writer)

          // return new Promise((resolve, reject) => {
          //   writer.on('finish', resolve)
          //   writer.on('error', reject)
          // })

        },
        async downloadProjectS(){
          console.log("download project....")

            

          const authStore = useAuthStore()
          const notify  = useNotify()
          const response = await api.downloadProjectGraph(this.project.id,authStore.user.accessToken);

          const content = response.headers['content-type'];
          download(response.data, "source_graph.ttl", content)

        }

        

    }



})    