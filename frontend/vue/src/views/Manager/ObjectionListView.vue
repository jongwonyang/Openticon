<template>
  <Objectionlist/>
</template>

<script setup>
import Objectionlist from "@/components/objectionManage/Objectionlist.vue";
import { useAdminStore } from "@/stores/admin";

const adminStore = useAdminStore();
</script>
