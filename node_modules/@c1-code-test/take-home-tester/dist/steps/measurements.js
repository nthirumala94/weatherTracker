'use strict';

var _cucumber = require('cucumber');

var _datatable = require('../lib/datatable');

var _bluebird = require('bluebird');

var Bluebird = _interopRequireWildcard(_bluebird);

var _ramda = require('ramda');

var R = _interopRequireWildcard(_ramda);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

const acceptJson = { accept: 'application/json' };

(0, _cucumber.defineSupportCode)(({ Given, Then, When }) => {
  Given(/^I have submitted new measurements as follows:$/, function (table) {
    return Bluebird.map((0, _datatable.readTableValuesAsJson)(table), m => this.request(makeInsertRequest(m))).each(res => {
      if (res.statusCode !== 201) throw new AssertionError(`Could not insert measurement, got code ${res.statusCode}. ` + `Response body: \n${JSON.stringify(res.body, null, 2)}`);
    });
  });

  When(/^I submit a new measurement as follows:$/, function (table) {
    R.pipe(_datatable.readTableValuesAsJson, single, makeInsertRequest, this.updateRequest)(table);
  });

  When(/^I delete the measurement for "([^"]*)"$/, function (timestamp) {
    this.updateRequest({
      method: 'delete',
      url: `/measurements/${timestamp}`,
      headers: acceptJson
    });
  });

  When(/^I get (?:a measurement|measurements) for "([^"]*)"$/, function (dateOrTimestamp) {
    this.updateRequest(makeGetRequest(dateOrTimestamp));
  });

  When(/^I (replace|update) the measurement for "([^"]*)" as follows:$/, function (command, timestamp, table) {
    const measurement = single((0, _datatable.readTableValuesAsJson)(table));

    this.updateRequest({
      method: getMethod(command),
      url: `/measurements/${timestamp}`,
      headers: acceptJson,
      body: measurement
    });

    function getMethod(command) {
      switch (command) {
        case 'replace':
          return 'put';
        case 'update':
          return 'patch';
        default:
          throw new Error(`Unsupported command: ${command}`);
      }
    }
  });

  Then(/^the measurement for "([^"]*)" does not exist$/, function (timestamp) {
    const res = this.request(makeGetRequest(timestamp));
    return expect(res).to.eventually.have.property('statusCode', 404);
  });

  Then(/^the measurement for "([^"]*)" is:$/, function (timestamp, table) {
    const body = this.request(makeGetRequest(timestamp)).promise().get('body');
    return expect(body).to.eventually.deep.equal(single((0, _datatable.readTableValuesAsJson)(table)));
  });
});

function makeGetRequest(dateOrTimestamp) {
  return {
    method: 'get',
    url: `/measurements/${dateOrTimestamp}`,
    headers: acceptJson
  };
}

function makeInsertRequest(measurement) {
  return {
    method: 'post',
    url: '/measurements',
    body: measurement
  };
}
//# sourceMappingURL=measurements.js.map