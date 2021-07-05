import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/manualBootstrapping/GlobalGraph.vue') },
      { path: 'globalGraph', component: () => import('pages/manualBootstrapping/GlobalGraph.vue') },
      { path: 'globalGraph/edit_global_graph/:id', component: () => import('pages/Test_webvowl.vue') },
      { path: 'dataSources', component: () => import('pages/manualBootstrapping/DataSources.vue') },
      { path: 'wrappers', component: () => import('pages/manualBootstrapping/Wrappers.vue') },
      { path: 'LAVMappings', component: () => import('pages/manualBootstrapping/LAVMappings.vue') },
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
