<template>
  <q-page>
    <div class="q-pa-md">

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
<!--              -->
<!--              <q-btn rounded color="primary" icon="person" label="Manual" />-->
<!--              <q-btn rounded color="primary" icon="memory" label="Automatic" />-->
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
      </div>



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

.started-cards{
  height: 45vh;
}

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
