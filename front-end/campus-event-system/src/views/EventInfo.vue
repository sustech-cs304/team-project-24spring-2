<script setup>
import EventDescription from "@/components/EventDescription.vue";
import EventInformation from "@/components/EventInformation.vue";
import EventReservation from "@/components/EventReservation.vue";
</script>
<template>
  <a-tabs>
    <a-tab-pane key="1">
      <template #title>
        <icon-calendar/> Description
      </template>
      <div class="Description">
        <EventDescription/>
      </div>
    </a-tab-pane>
    <a-tab-pane key="2">
      <template #title>
        <icon-clock-circle/> Information
      </template>
      <div class="Information">
        <EventInformation/>
      </div>
    </a-tab-pane>
    <a-tab-pane key="3">
      <template #title>
        <icon-user/> Reservation
      </template>
        <EventReservation/>
    </a-tab-pane>
    <a-tab-pane key="4">
      <template #title>
        <icon-user/> Help
      </template>
      Coming soon....
    </a-tab-pane>
  </a-tabs>
</template>
<style scoped>
.Description {
  height: 100vh;
  width: 100vw;
}
</style>