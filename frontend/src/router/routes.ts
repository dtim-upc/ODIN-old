import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/manualBootstrapping/GlobalGraph.vue') },
      { path: 'globalGraph', component: () => import('pages/manualBootstrapping/GlobalGraph.vue') },
      { path: 'globalGraph/view/:id', component: () => import('pages/manualBootstrapping/GlobalGraphView.vue') },
      { path: 'globalGraph/edit_global_graph/:id', component: () => import('pages/Test_webvowl.vue') },
      { path: 'dataSources', component: () => import('pages/manualBootstrapping/DataSources.vue') },
      { path: 'dataSources/view/:id', component: () => import('pages/manualBootstrapping/DataSourcesView.vue') },
      { path: 'wrappers', component: () => import('pages/manualBootstrapping/Wrappers.vue') },
      { path: 'wrappers/view/:id', component: () => import('pages/manualBootstrapping/WrappersView.vue') },
      { path: 'lavmappings', component: () => import('pages/manualBootstrapping/LAVMappings.vue') },
      { path: 'lavmappings/subgraphselect/:id/:LAVMappingID', component: () => import('pages/LavMappingWebvowl.vue') },
      { path: 'pruebaWebvowl', component: () => import('pages/Test_webvowl.vue') },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/Error404.vue'),
  },
];

export default routes;
