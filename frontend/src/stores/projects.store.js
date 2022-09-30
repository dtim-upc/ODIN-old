import { defineStore } from 'pinia'
import {useNotify} from 'src/use/useNotify.js'
import projectAPI from "src/api/projectAPI.js";
import { useAuthStore } from 'stores/auth.store.js'

export const useProjectsStore = defineStore('projects',{

    state: () => ({
        projects : [],
    }),

    getters : {},
    actions: {
        // initStores(){
            //  authStore = useAuthStore();
            //  
        // },
        init(){
            console.log("projects store init")
            // this.initStores();   
            const authStore = useAuthStore(); 
            if(authStore.user.accessToken && this.projects.length === 0) {
                this.getProjects()
            }
        },
        getProjects(){
            const authStore = useAuthStore(); 
            projectAPI.getAllProjects(authStore.user.accessToken)
                            .then(response => {

                                console.log("projects received")
                                console.log(response.data)

                                if(response.data === "") { // when no datasources, api answer ""
                                    this.projects = []
                                } else {
                                    this.projects = response.data
                                }      

                            }).catch(err => {
                            console.log("error retrieving data sources")
                            console.log(err)
                            }) 
        },
        createProject(project, successCallback){
            const authStore = useAuthStore(); 
            const notify = useNotify();

            console.log("create project store...")
            project.createdBy = authStore.user.username
            console.log("send project: ",project)
            projectAPI.createProject(project, authStore.user.accessToken ).then((response) => {
                if (response.status == 201) {
      
                    console.log(response)
                  notify.positive(`Project ${project.name} successfully created`)
                  // onReset()
                //   
                    this.projects.push(response.data)
                  successCallback()
                 
                } else {
                  // console.log("error")
                  notify.negative("Cannot create project. Something went wrong in the server.")
                }
              }).catch( (error) => {

                console.log("error is: "+error)
                if(error.response){
                    notify.negative("Something went wrong in the server for creating a project.")
                }
                  
              
            });





        }
       

    }



})    



// import { defineStore } from 'pinia'
// import {useNotify} from 'src/use/useNotify.js'
// import projectAPI from "src/api/projectAPI.js";
// import { useAuthStore } from 'stores/auth.store.js'

// // let notify;
// // let authStore;

// export const useProjectsStore = defineStore('projects',{

//     state: () => ({
//         projects : [],
//     }),

//     getters : {},
//     actions: {
//         // initStores(){
//             //  authStore = useAuthStore();
//             //  notify = useNotify();
//         // },
//         init(){
//             console.log("projects store init")
//             // this.initStores();   
//             const authStore = useAuthStore(); 
//             if(authStore.user.accessToken && this.projects.length === 0) {
//                 this.getProjects()
//             }

//         },
//         getProjects(){
//             const authStore = useAuthStore(); 
//             projectAPI.getAllProjects(authStore.user.accessToken)
//                             .then(response => {

//                                 console.log("projects received")
//                                 console.log(response.data)

//                                 if(response.data === "") { // when no datasources, api answer ""
//                                     this.projects = []
//                                 } else {
//                                     this.projects = response.data
//                                 }      

//                             }).catch(err => {
//                             console.log("error retrieving data sources")
//                             console.log(err)
//                             }) 
//         },
//         createProject(project, successCallback){
//             console.log("create project store...")

//             // console.log(authStore.user)
//             project.createdBy = authStore.user.username
//             console.log("send project: ",project)
//             projectAPI.createProject(project, authStore.user.accessToken ).then((response) => {
//                 if (response.status == 201) {
      
//                     console.log(response)
//                   notify.positive(`Project ${project.name} successfully created`)
//                   // onReset()
//                 //   
//                     this.projects.push(response.data)
//                   successCallback()
                 
//                 } else {
//                   // console.log("error")
//                   notify.negative("Cannot create project. Something went wrong in the server.")
//                 }
//               }).catch( (error) => {

//                 console.log("error is: "+error)
//                 if(error.response){
//                     notify.negative("Something went wrong in the server for creating a project.")
//                 }
                  
              
//             });





//         }
       

//     }



// })    