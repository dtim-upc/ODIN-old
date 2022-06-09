<template>
  <q-page>
    
    <div class="q-pa-xl">

<!--      get started-->
      <img src="~assets/home/home_bg.svg" alt="" width="500" style="top:0;right:0;position:absolute;margin-right: 140px;" class="">

      <div>

        <div class="relative-position">
          <!-- <h3 class="no-margin">Hey User, Welcome to ODIN</h3> -->
          <h3 class="no-margin">Hello <q-icon name="o_waving_hand"/></h3>
          
          <h6 class="q-mt-sm">Here are some quick options</h6>
        </div>

      </div>

      
     
      <div class="row">
        <!-- offset-md-1 -->
        <div class="col-xs-12  col-md-12 ">
          <div >
            <!-- class="q-pa-md" -->
            <div class="q-gutter-xl row justify-center">
              <q-card :flat=true class="flexg1 bg-gradient-green home-cards shadow-4" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section class="text-center">
                  <img src="~assets/home/datasources_pattern2.svg" style="width:60%">
                  <!-- <img src="~assets/home/datasource_pattern.svg" style="width:60%"></img> -->
                  
                </q-card-section>
                
                <q-card-section class="text-center">
                  <!-- <div class="text-h6">Add data source</div> -->
                  <q-btn color="black" label="Add data source" unelevated rounded icon-right="mdi-arrow-right"  @click="createDS = true"/>
                </q-card-section>
              </q-card>
              <!-- <q-card class=" flexg1 home-cards shadow-4 bg-gradient-purple" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section class="text-center">
                  <img src="~assets/home/join_pattern.svg" style="width:60%">
                </q-card-section>
                <q-card-section class="text-center">
                  <q-btn color="black" label="Integrate data sources" unelevated rounded icon-right="mdi-arrow-right"  />
                </q-card-section>
              </q-card> -->
              <q-card class=" flexg1 home-cards shadow-4 bg-gradient-purple" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section class="text-center">
                  <img src="~assets/home/integration_pattern.svg" style="width:60%">
                </q-card-section>
                <q-card-section class="text-center">
                  <q-btn color="black" label="Integrate data sources" unelevated rounded icon-right="mdi-arrow-right"  />
                </q-card-section>
              </q-card>
              <q-card class=" flexg1 shadow-4 home-cards bg-gradient-test" :class="{'flexb0':$q.screen.gt.sm}" >
                <!-- <img class="full-width" src="~assets/getStarted/add_ds2.png"> -->
                <q-card-section class="text-center">
                  <img src="~assets/home/query_pattern2.svg" style="width:60%">
                </q-card-section>
                <q-card-section class="text-center">
                  <q-btn color="black" label="Query data sources" unelevated rounded icon-right="mdi-arrow-right"  />

                  <!-- <div class="text-h6"> Query </div> -->
                </q-card-section>
                
                <!-- <q-card-section >
                  <div class="row justify-center">
                    <q-btn unelevated rounded color="white" text-color="black" label="Unelevated Rounded" />
                  </div>

                </q-card-section> -->
              </q-card>
            </div>
          </div>
        </div>

      </div>


      <!-- <div class="row">
        <div class="col-md-8 offset-md-2">
          <div class="q-pa-md">
            <div class="q-gutter-lg row justify-center">
              <q-card class="my-card bg-gradient-green flexg1 home-cards shadow-8" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section>
                  <div class="text-h6">Add data sources</div>
                </q-card-section>
                <img class="full-width" src="~assets/getStarted/add_ds2.png">
              </q-card> -->
<!--              <q-card class="my-card bg-gradient-blue flexg1 home-cards " :class="{'flexb0':$q.screen.gt.sm}" >-->
<!--                <q-card-section>-->
<!--                  <div class="text-h6">Make integrations</div>-->
<!--                </q-card-section>-->
<!--              </q-card>-->
              <!-- <q-card class="my-card flexg1 home-cards shadow-8" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section>

                  <q-avatar color="red" text-color="white" icon="directions" />
                  <div class="text-h6">Make integrations</div>
                </q-card-section>
              </q-card>
              <q-card class="my-card bg-gradient-green2 flexg1 shadow-8 home-cards" :class="{'flexb0':$q.screen.gt.sm}" >
                <q-card-section>
                  <div class="text-h6"> Query </div>
                </q-card-section>
                <img class="full-width" src="~assets/getStarted/add_ds2.png">
                <q-card-section >
                  <div class="row justify-center">
                    <q-btn unelevated rounded color="white" text-color="black" label="Unelevated Rounded" />
                  </div>

                </q-card-section>
              </q-card>
            </div>
          </div>
        </div>

      </div> -->




<!-- 
      <br><br><br><br><br>
      <h3 class="no-margin">Welcome to ODIN!</h3>
      <h6>Here is what you have done in ODIN</h6>

      <div class="row q-col-gutter-lg justify-center">
        <div class="col-md-3 col-sm-4">
          <q-card class="my-card started-cards text-center column" >
            <img class="full-width"  src="~assets/getStarted/add_datasources.png">
            <q-card-section class="no-padding">
              <div>

                <h6 class="no-margin">1. Add data sources</h6>
                <span>First things first, lets add at least two data sources to ODIN</span>
              </div>
            </q-card-section>

            <q-card-section class="q-mt-auto">
              <q-btn rounded :color="datasources.length >1? 'positive':'warning'" icon="note_add" :label="datasources.length >1? 'done': datasources.length+'/2 data sources'" @click="createDS = true"/>
            </q-card-section>
          </q-card>

        </div>
        <div class="col-md-3 col-sm-4">

          <q-card class="my-card started-cards text-center column">
            <img class="full-width" src="~assets/getStarted/integration.png">

            <q-card-section class="no-padding">
              <h6 class="no-margin">2. Integrate them</h6>
              <span>Need something quick? Try the automatic integration or create your manual integration</span>
            </q-card-section>


            <q-card-section class="q-pa-xs">

              <q-btn-group rounded>
                <q-btn rounded color="primary" icon="person" label="Manual" />

                <q-btn rounded color="primary" icon="memory" label="Automatic" @click="startIntegration =true" />

                </q-btn-group>
            </q-card-section>

          </q-card>

        </div>
        <div class="col-md-3 col-sm-4">

          <q-card class="my-card started-cards text-center column ">
            <img class="full-width" src="~assets/getStarted/query.png">

            <q-card-section class="no-padding">
              <h6 class="no-margin">3. Query global graph</h6>
              <span>Finally, you can query your global graphs</span>
            </q-card-section>

            <q-card-section class="q-mt-auto">
              <q-btn color="primary" icon="mail" label="Query" />
            </q-card-section>
          </q-card>

        </div>
      </div> -->



<!--      <div class="row q-col-gutter-md">-->
<!--        <div class="col-4">-->
<!--          <q-card class="my-card">-->
<!--            <q-card-section>-->
<!--              Global graphs-->
<!--            </q-card-section>-->
<!--          </q-card>-->

<!--        </div>-->
<!--        <div class="col-4">-->

<!--          <q-card class="my-card">-->
<!--            <q-card-section>-->
<!--              Data sources-->
<!--            </q-card-section>-->
<!--          </q-card>-->

<!--        </div>-->
<!--        <div class="col-4">-->

<!--          <q-card class="my-card">-->
<!--            <q-card-section>-->
<!--              Integration-->
<!--            </q-card-section>-->
<!--          </q-card>-->

<!--        </div>-->
<!--      </div>-->

      <q-dialog v-model="createDS" >
        <NewDatasourceWrapperStepper style="max-width: calc(100vh - 48px)" @finished="createDS = false"/>
      </q-dialog>

      <q-dialog v-model="startIntegration" full-width>
        <IntegrationStepper  @finished="startIntegration = false"/>
      </q-dialog>

<!--min-width: calc(50vw - 48px)-->

    </div>

  </q-page>
</template>


<script >

// import TableGlobalGraph from 'components/tables/TableGlobalGraph.vue';
import {computed, ref, onMounted} from 'vue'
import NewDatasourceWrapperStepper from "components/stepper/NewDatasourceWrapperStepper";
import IntegrationStepper from "components/stepper/IntegrationStepper";
import {useStore} from "vuex";

export default {

  name: 'Home',
  components: {NewDatasourceWrapperStepper, IntegrationStepper },
  setup() {

    const store = useStore()
    const createDS = ref(false)
    const startIntegration = ref(false)

    const datasources = computed(() => store.state.datasource.datasources)



    onMounted(() => {

        store.dispatch("getDatasources")

    })

    return { createDS, datasources, startIntegration}

  }

}

</script>

<style>

.home-cards{

  border-radius: 15px;
  max-width: 280px;
}

.started-cards{
  height: 45vh;
}

.flexg1{
  flex-grow: 1;
}

/* .flexb0 {
  flex-basis: 0;
} */

.bg-gradient-green{
  /* background-image: linear-gradient(225deg,#0abac2,#b2de94); */
  /* background-image: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%); */
  /* background-color: #E7E2F3; */
  /*background-color: #efffe5;*/
    background: #49BCF6; /* fallback for old browsers */
  background: -webkit-linear-gradient(to top, #49BCF6, #49DEB2); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to top, #49BCF6, #49DEB2); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to top, #49BCF6, #49DEB2);
}

.bg-gradient-green2{
  background-image: linear-gradient(225deg,#0abac2,#b2de94);
  /*background-color: #efffe5;*/
}

.bg-gradient-purple{
  /* background-image: linear-gradient(150deg,#c99fff -20%,#4a4eff); */
  /* background-image: linear-gradient(150deg,#450b7c -20%,#563cc9 -20%,#49e9fb); */
  /* background-image: linear-gradient(120deg, #e0c3fc 0%, #8ec5fc 100%); */
  /* background-color: #DBE7FA; */
    background: #9A85C6; /* fallback for old browsers */
  background: -webkit-linear-gradient(to top, #9A85C6, #F9C1EB); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to top, #9A85C6, #F9C1EB); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to top, #9A85C6, #F9C1EB);

}


.bg-gradient-test{
  /* background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); */
  /* background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%); */
  /* background-color: #E2F3E7; */
  background: linear-gradient( to top , #3494E6, #9A85C6); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to top , #3494E6, #9A85C6);
}
.bg-gradient-test:before{
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  z-index: 0;
  top: 0;
  right: 0;
  background-image: url(~assets/bg-pattern.png);
  background-repeat: no-repeat;
  background-position: 50%;
  background-size: 100% auto;
} 
.bg-gradient-purple:before{
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  z-index: 0;
  top: 0;
  right: 0;
  background-image: url(~assets/home/pattern_bg.png);
  background-repeat: no-repeat;
  background-position: 50%;
  background-size: 100% auto;
} 
.bg-gradient-green:before{
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  z-index: 0;
  top: 0;
  right: 0;
  background-image: url(~assets/bg-pattern.png);
  background-repeat: no-repeat;
  background-position: 50%;
  background-size: 100% auto;
}

/* .bg-gradient-blue:before{
  width: 100%;
  height: 100%;
  content: "";
  position: absolute;
  z-index: 0;
  top: 0;
  right: 0;
  background-image: url(~assets/bg-pattern.png);
  background-repeat: no-repeat;
  background-position: 50%;
  background-size: 100% auto;
}

.bg-gradient-blue{
  background-image: linear-gradient(150deg,#c99fff -20%,#4a4eff);
} */


</style>



<!--<template>-->
<!--  <q-page>-->
<!--&lt;!&ndash;    <p>welcome USER </p> &ndash;&gt;-->
<!--    <h3>Welcome!</h3>-->
<!--    <h5>Here is what you have done in ODIN</h5>-->

<!--&lt;!&ndash;    <q-card class="my-card">&ndash;&gt;-->
<!--&lt;!&ndash;      <q-card-section>&ndash;&gt;-->
<!--&lt;!&ndash;         lorem&ndash;&gt;-->
<!--&lt;!&ndash;      </q-card-section>&ndash;&gt;-->
<!--&lt;!&ndash;    </q-card>&ndash;&gt;-->


<!--  </q-page>-->

<!--</template>-->


<!--<script >-->


<!--export default {-->

<!--  name: 'Home'-->

<!--}-->

<!--</script>-->
