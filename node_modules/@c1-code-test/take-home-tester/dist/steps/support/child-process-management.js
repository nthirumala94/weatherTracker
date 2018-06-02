'use strict';

var _cucumber = require('cucumber');

var _runner = require('../../lib/runner');

function _asyncToGenerator(fn) { return function () { var gen = fn.apply(this, arguments); return new Promise(function (resolve, reject) { function step(key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { return Promise.resolve(value).then(function (value) { step("next", value); }, function (err) { step("throw", err); }); } } return step("next"); }); }; }

(0, _cucumber.defineSupportCode)(function ({ Before, After, runner, eventBroadcaster }) {
  let childConsoleLog;

  Before({ timeout: 3 * _runner.Runner.maxConnectTimeout }, _asyncToGenerator(function* () {
    try {
      yield runner.waitForServerUnavailable();
    } catch (err) {
      console.log(err.message);
      eventBroadcaster.emit('abort');
    }

    childConsoleLog = runner.start();
    yield runner.waitForServerAvailable();
  }));

  After((() => {
    var _ref2 = _asyncToGenerator(function* ({ result }) {
      yield runner.stop();
      result.consoleLog = yield childConsoleLog;
    });

    return function (_x) {
      return _ref2.apply(this, arguments);
    };
  })());
});
//# sourceMappingURL=child-process-management.js.map