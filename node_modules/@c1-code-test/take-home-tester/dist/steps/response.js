'use strict';

var _cucumber = require('cucumber');

var _url = require('url');

var _datatable = require('../lib/datatable');

(0, _cucumber.defineSupportCode)(({ Then }) => {
  Then(/^the response has a status code of (\d+)$/, function (statusCode) {
    return expect(this.getResponse()).to.eventually.have.property('statusCode', parseInt(statusCode, 10));
  });

  Then(/^the response body is:$/, function (table) {
    const expected = single((0, _datatable.readTableValuesAsJson)(table));
    return expect(this.getResponseBody()).to.eventually.deep.equal(expected);
  });

  Then(/^the response body is an array of:$/, function (table) {
    const expected = (0, _datatable.readTableValuesAsJson)(table);
    return expect(this.getResponseBody()).to.eventually.deep.equal(expected);
  });

  Then(/^the response body is an empty array$/, function () {
    return expect(this.getResponseBody()).to.eventually.deep.equal([]);
  });

  Then(/^the Location header has the path "([^"]*)"$/, function (path) {
    return expect(this.getResponse()).to.eventually.have.nested.property('headers.location').and.satisfy(r => (0, _url.parse)(r).pathname === path, `has path of ${path}`);
  });
});
//# sourceMappingURL=response.js.map