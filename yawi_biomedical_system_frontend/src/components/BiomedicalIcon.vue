<template>
  <div style="display: inline-flex; align-items: center">
    <slot name="prefix"></slot>
    <component :color="color" :is="currentIcon" :size="size" :style="iconStyle" style="margin-right: 5px"></component>
    <slot></slot>
    <slot name="suffix"></slot>
  </div>
</template>

<script>
import IconSearch from './icons/IconSearch.vue';

export default {
  name: 'BiomedicalIcon',
  props: {
    icon: {
      type: String,
      default: '',
    },
    color: {
      type: String,
      default: 'white',
    },
    size: {
      type: String,
      default: 'md',
      validator: (val) => ['sm', 'md', 'lg'].includes(val),
    },
    iconStyle: {
      type: String,
      default: '',
    },
  },
  computed: {
    currentIcon() {
      switch (this.icon) {
        case 'search':
          return IconSearch;
        default:
          return '';
      }
    },
  },
};
</script>