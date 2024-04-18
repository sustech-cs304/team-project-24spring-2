<template>
  <div class="list-wrap" >
    <!-- <a-typography-title class="block-title" :heading="6">
      {{ $t('cardList.tab.title.service') }}
    </a-typography-title> -->
    <a-row
      v-for="item in renderData"
      :key="item.id"
      
      class="list-row"
      style="display: flex; justify-content: center; width: 600px; margin: auto; margin-bottom: 20px"
    >
      <CardWrap
        style="min-height: 200px; min-width: 100px"
        :loading="loading"
        :name="item.name"
        :title="item.title"
        :description="item.description"
        :open-txt="$t('cardList.service.open')"
        :close-txt="$t('cardList.service.cancel')"
        :expires-text="$t('cardList.service.renew')"
        :tag-text="$t('cardList.service.tag')"
        :expires-tag-text="$t('cardList.service.expiresTag')"
        :icon="item.icon"
        @click="handleClick(item)"
      >
        <template #skeleton>
          <a-skeleton :animation="true">
            <a-skeleton-line :widths="['100%', '100%', '100%']" :rows="3" />
            <a-skeleton-line :widths="['100%']" :rows="1" />
          </a-skeleton>
        </template>
      </CardWrap>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { queryPermissionGroup, UsersGroup } from '@/api/users';
  import useRequest from '@/hooks/request';
  import CardWrap from './card-wrap.vue';

  const defaultValue: UsersGroup[] = new Array(4).fill({});
  const { loading, response: renderData } = useRequest<UsersGroup[]>(
    queryPermissionGroup,
    defaultValue
  );

  const handleClick = (item: UsersGroup) => {
    console.log('this is clicked');
  };
</script>

<style scoped lang="less"></style>
