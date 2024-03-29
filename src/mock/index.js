import Mock from 'mockjs';

Mock.XHR.prototype.proxy_send = Mock.XHR.prototype.send;
Mock.XHR.prototype.send = function() {
  if (this.custom.xhr) {
    this.custom.xhr.withCredentials = this.withCredentials || false;
  }
  this.proxy_send(...arguments);
};

function getPredicate({ drugName }) {
  print(drugName);
  return {
    status: '1',
    data: {
      B1: {
        C1: {
          p1: 2,
          p2: 3,
        },
        C2: {
          p1: 2,
        },
      },
      B2: {
        C2: {
          p3: 3,
        },
      },
      B3: {
        C3: {
          p1: 1,
          p4: 2,
        },
        c4: {
          p1: 1,
        },
      },
    },
  };
}

function getAllDrug() {
  return {
    status: '1',
    msg: 'mock upgrade',
    data: ['d1', 'd2', 'd3', 'd4'],
  };
}

function getEval({ drugName }) {
  print(drugName);
  return {
    status: '1',
    msg: 'mock upgrade',
    data: {
      C1: {
        eval1: 1,
        eval2: 2,
        eval3: 3,
        eval4: 4,
      },
      C2: {
        eval1: 1,
        eval2: 2,
        eval3: 3,
        eval4: 4,
      },
      C3: {
        eval1: 2,
        eval2: 1,
        eval3: 4,
        eval4: 3,
      },
    },
  };
}

Mock.mock(/\/url\/get_predicate/, 'post', getPredicate);
Mock.mock(/\/url\/all_drug/, 'get', getAllDrug);
Mock.mock(/\/url\/get_eval/, 'post', getEval);

export default Mock;
