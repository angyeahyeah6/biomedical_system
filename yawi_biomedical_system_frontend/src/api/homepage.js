import axios from 'axios';

const url = 'url';

function getPredicate(drugName) {
  return axios.post(`${url}/get_predicate`, { drugName });
}

function getAllDrug() {
  return axios.get(`${url}/all_drug`);
}

function getEval(drugName) {
  return axios.post(`${url}/get_eval`, { drugName });
}

export default {
  getPredicate,
  getAllDrug,
  getEval,
};
