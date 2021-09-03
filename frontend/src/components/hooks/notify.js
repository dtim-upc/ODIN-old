
import { Notify } from 'quasar';

// Taken from https://stackoverflow.com/questions/67983127/how-can-i-set-some-global-functions-mixins-in-quasar-vue-v2-framework

const negative = (message) => {
  Notify.create({
    message,
    color: "negative",
    icon: "cancel",
    textColor: "white",
  });
};

const positive = (message) => {
  Notify.create({
    message,
    color: "positive",
    textColor: "white",
    icon: "check_circle",
  });
};


export default {
  negative,
  positive,
};

// const primary = (message) => {
//   Notify.create({
//     message,
//     color: 'primary',
//     avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
//     actions: [
//       { label: 'Reply', color: 'yellow', handler: () => { /* ... */ } },
//       { label: 'Dismiss', color: 'white', handler: () => { /* ... */ } },
//     ],
//   });
// };
