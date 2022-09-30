import {odinApi} from 'boot/axios';



export default {


    integrate(projectID,data, token) { return odinApi.post('/project/'+projectID+'/integration', data, {headers: { Authorization: `Bearer ${token}` }})  },

    finishIntegration(projectID, token){ return odinApi.post('/project/'+projectID+'/integration/persist', null,{headers: { Authorization: `Bearer ${token}` }}) },
}

