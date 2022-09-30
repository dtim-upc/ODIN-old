import {odinApi} from 'boot/axios';


const headers = (token) => {
      return {headers: { Authorization: `Bearer ${token}` }}
}

export default {

    createProject(data, token) { return odinApi.post('/projects', data, headers(token)) },
    getAllProjects(token) { return odinApi.get('projects', headers(token)) },

    getProjectByID(id, token) { return odinApi.get('projects/'+id, headers(token) ) }

}