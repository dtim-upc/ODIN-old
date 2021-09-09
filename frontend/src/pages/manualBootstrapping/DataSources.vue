<template>
  <q-page>
<!--      <TableDataSources/>-->
    <TableDataSourcesAndIntegrations />
  </q-page>
</template>


<script >

import TableDataSourcesAndIntegrations from 'components/tables/integration/TableDataSourcesAndIntegrations';

export default {

  name: 'PageDataSources',
  components: {TableDataSourcesAndIntegrations }

}

</script>
