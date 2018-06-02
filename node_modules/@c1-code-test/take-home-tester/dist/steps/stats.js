'use strict';

var _cucumber = require('cucumber');

var _ramda = require('ramda');

(0, _cucumber.defineSupportCode)(({ When }) => {
  When(/^I get stats with parameters:$/, function (table) {
    const qs = (0, _ramda.reduceBy)((l, item) => l.concat(item.value), [], item => item.param, table.hashes());

    this.updateRequest({
      method: 'get',
      url: '/stats',
      qs
    });
  });
});
//# sourceMappingURL=stats.js.map