// import axios from 'axios';

const url = 'http://localhost:8080/';
// import mock from "./mock";
// const devMode = process.env.NODE_ENV === "development";

const real = {
  getPredicate: token => {
    return window
      .fetch(`${url}get_predicate`, {
        method: 'POST',
        body: JSON.stringify(token),
        headers: {
          'content-type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      })
      .then(res => res.json());
  },
  getEval: token => {
    return window
      .fetch(`${url}get_eval`, {
        method: 'POST',
        body: JSON.stringify(token),
        headers: {
          'content-type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      })
      .then(res => res.json());
  },
  getAllDrug: () => {
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
  getDetailPredicate: token => {
    return window
      .fetch(`${url}get_detail_predicate`, {
        method: 'POST',
        body: JSON.stringify(token),
        headers: {
          'content-type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      })
      .then(res => res.json());
  },
};
export default real;
// export default devMode ? mock : real;
