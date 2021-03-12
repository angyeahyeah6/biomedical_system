// import axios from 'axios';

const url = 'http://localhost:8080/';

// function getPredicate(drugName) {
//   return axios.post(`${url}/get_predicate`, { drugName });
// }

// function getAllDrug() {
//   return axios.get(`${url}/all_drug`);
// }

// function getEval(drugName) {
//   return axios.post(`${url}/get_eval`, { drugName });
// }

// export default {
//   getPredicate,
//   getAllDrug,
//   getEval,
// };

// import mock from "./mock";
// const devMode = process.env.NODE_ENV === "development";

const real = {
  // getPredicate: token => {
  //   return window
  //     .fetch(`${url}get_predicate`, {
  //       method: 'POST',
  //     })
  //     .then(res => res.json());
  // },
  getAllDrug: token => {
    return window
      .fetch(`${url}all_drug`, {
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      })
      .then(res => res.json());
  },
  // register: (register) => {
  //   return window
  //     .fetch("http://localhost:3001/api/register", {
  //       body: JSON.stringify(register), // must match 'Content-Type' header
  //       headers: {
  //         "content-type": "application/json",
  //       },
  //       method: "POST", // *GET, POST, PUT, DELETE, etc.
  //       mode: "cors", // no-cors, cors, *same-origin
  //       redirect: "follow", // manual, *follow, error
  //       referrer: "no-referrer", // *client, no-referrer
  //     })
  //     .then((response) => response.json());
  // },
  // login: (login) => {
  //   return window
  //     .fetch("http://localhost:3001/api/login", {
  //       body: JSON.stringify(login), // must match 'Content-Type' header
  //       headers: {
  //         "content-type": "application/json",
  //       },
  //       method: "POST", // *GET, POST, PUT, DELETE, etc.
  //       mode: "cors", // no-cors, cors, *same-origin
  //       redirect: "follow", // manual, *follow, error
  //       referrer: "no-referrer", // *client, no-referrer
  //     })
  //     .then((response) => response.json());
  // },
};
export default real;
// export default devMode ? mock : real;
