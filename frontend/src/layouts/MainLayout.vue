<template>
  <q-layout view="lHr LpR lFr">
    <q-header class="bg-color text-black">
      <q-toolbar>
        <q-btn flat @click="drawer = !drawer" round dense icon="menu"/>

        <q-toolbar-title>
          <!--          <q-avatar>-->
          <!--            <img src="~assets/logoODIN.png"/>-->
          <!--          </q-avatar>-->
          ODIN
        </q-toolbar-title>

        <q-space/>

        <q-btn class="q-mr-xs" flat round @click="$q.dark.toggle()"
               :icon="$q.dark.isActive ? 'nights_stay' : 'wb_sunny'"/>

        <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round dense flat color="black" :icon="$q.fullscreen.isActive ? 'fullscreen_exit' : 'fullscreen'"
                 @click="$q.fullscreen.toggle()" v-if="$q.screen.gt.sm">
          </q-btn>
          <q-btn round flat>
            <q-avatar size="26px">
              <img src="https://cdn.quasar.dev/img/boy-avatar.png"/>
            </q-avatar>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <!--    <q-drawer v-model="leftDrawerOpen" side="left" bordered>-->
    <!--      &lt;!&ndash; drawer content &ndash;&gt;-->
    <!--    </q-drawer>-->
    <q-drawer
      v-model="drawer"
      show-if-above

      :mini="!drawer || miniState"

      :width="200"
      :breakpoint="500"
      bordered
      class="bg-white"
    >
      <q-scroll-area class="fit">
        <q-list padding>

          <q-item style="max-width: 200px">
            <q-item-section>
              <q-img src="~assets/logoODIN.png" style="max-width: 180px; max-height: 35px; " fit="contain"/>
              <!--                <img src="~assets/logoODIN.png"/>-->
              <!--              :src=" miniState ? '/assets/logoODIN_short.png':'/assets/logoODIN.png'"-->
            </q-item-section>
          </q-item>

          <q-separator/>
          <q-item @click="miniState = !miniState">
            <q-item-section avatar>
              <q-btn dense round unelevated :icon="miniState == true ? 'mdi-arrow-collapse-right' : 'mdi-arrow-collapse-left'" @click="miniState = !miniState"/>
            </q-item-section>
            <!--            <q-item-section avatar>-->
            <!--              <q-icon name="chevron_left" />-->
            <!--            </q-item-section>-->
          </q-item>

          <q-item clickable v-ripple to="/" active-class="bg-active" exact>
            <q-item-section avatar>
              <q-icon name="o_cottage"/>
            </q-item-section>

            <q-item-section>
              Home
            </q-item-section>
          </q-item>

          <!--          <q-item active clickable v-ripple>-->
          <!--            <q-item-section avatar>-->
          <!--              <q-icon name="star" />-->
          <!--            </q-item-section>-->

          <!--            <q-item-section>-->
          <!--              Pending-->
          <!--            </q-item-section>-->
          <!--          </q-item>-->

          <q-item clickable v-ripple to="/globalGraph" active-class="bg-active">
            <q-item-section avatar>
              <!--              o_hub-->
              <q-icon name="o_hub"/>
            </q-item-section>

            <q-item-section>
              Global graph
            </q-item-section>
          </q-item>

<!--          <q-separator/>-->

          <q-item clickable v-ripple to="/dataSources" active-class="bg-active">
            <q-item-section avatar>
              <!--      o_file_copy o_spoke   o_category  workspaces   category spoke-->
              <q-icon name="o_bubble_chart "/>
            </q-item-section>

            <q-item-section>
              Data sources
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple to="/wrappers" active-class="bg-active">
            <q-item-section avatar>
              <!--              settings_input_svideo-->
              <q-icon name="mdi-google-circles-communities"/>
            </q-item-section>

            <q-item-section>
              Wrappers
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple to="/lavmappings" active-class="bg-active">
            <q-item-section avatar>
              <!--          link   google-circles-extended mdi-vector-circle  egg_alt-->
              <q-icon name="mdi-google-circles-extended "/>
            </q-item-section>

            <q-item-section>
              LAV Mapping
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple to="/omq" active-class="bg-active">
            <q-item-section avatar>
              <q-icon name="mdi-selection-search"/>
            </q-item-section>

            <q-item-section>
              Query
            </q-item-section>
          </q-item>

        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
import {ref} from "vue";

export default {
  setup() {
    // const leftDrawerOpen = ref(false);
    const miniState = ref(false)

    return {
      // leftDrawerOpen,
      // toggleLeftDrawer() {
      //   leftDrawerOpen.value = !leftDrawerOpen.value;
      // },
      drawer: ref(false),
      miniState
    };
  },
};
</script>

<style>

.bg-color {
  background-color: #f4f8fb;
}

.bg-white {
  background-color: #ffffff;
}

.bg-active {
  color: #8f81bd;
  background: #f0edfd;

}


.body--light {
  background-color: #f4f8fb;
}

.body--dark {
  background-color: black;
}

/*@import url("https://fonts.googleapis.com/css?family=Source+Sans+Pro");*/
/*body {*/
/*  background-color: #fff;*/
/*  color: #171717;*/
/*  transition: background-color 0.2s ease, color 0.2s ease;*/
/*}*/

/*body.dark-mode {*/
/*  background-color: #242424;*/
/*}*/
/*body.dark-mode .flex h1 {*/
/*  color: #fff;*/
/*}*/

.mode-toggle {
  position: relative;
  padding: 0;
  width: 44px;
  height: 24px;
  min-width: 36px;
  min-height: 20px;
  background-color: #262626;
  border: 0;
  border-radius: 24px;
  outline: 0;
  overflow: hidden;
  cursor: pointer;
  z-index: 2;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  -webkit-touch-callout: none;
  appearance: none;
  transition: background-color 0.5s ease;
}

.mode-toggle .toggle {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  margin: auto;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 3px solid transparent;
  box-shadow: inset 0 0 0 2px #a5abba;
  overflow: hidden;
  transition: transform 0.5s ease;
}

.mode-toggle .toggle #dark-mode {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 50%;
}

.mode-toggle .toggle #dark-mode:before {
  content: "";
  position: relative;
  width: 100%;
  height: 100%;
  left: 50%;
  float: left;
  background-color: #a5abba;
  transition: border-radius 0.5s ease, width 0.5s ease, height 0.5s ease, left 0.5s ease, transform 0.5s ease;
}

.mode-toggle {
  background-color: #333333;
}

body.dark-mode .mode-toggle .toggle {
  transform: translateX(19px);
}

body.dark-mode .mode-toggle .toggle #dark-mode:before {
  border-radius: 50%;
  width: 150%;
  height: 85%;
  left: 40%;
  transform: translate(-10%, -40%), rotate(-35deg);
}


</style>
