<template>
    <div>
        <h1>ProcessLogin</h1>
    </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

onMounted(() => {
    console.log(route.query.access_token);
    window.opener.postMessage(route.query.access_token, import.meta.env.VITE_APP_URL);
    window.close();
});
</script> 
