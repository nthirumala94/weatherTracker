'use strict';

var _cucumber = require('cucumber');

var _bluebird = require('bluebird');

var _bluebird2 = _interopRequireDefault(_bluebird);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

(0, _cucumber.defineSupportCode)(({ eventBroadcaster, setDefinitionFunctionWrapper }) => {
  // auto-enroll all step definitions. Other support code can enroll themselves inline.
  for (const definition of _cucumber.supportCodeLibraryBuilder.options.stepDefinitions) definition.options.wrapperOptions = { watchResponses: true };

  setDefinitionFunctionWrapper((fn, options) => {
    if (!options || !options.watchResponses) return fn;

    return function () {
      const args = arguments;

      return _bluebird2.default.race([onResponseStatusCode(501, eventBroadcaster).return('pending'), _bluebird2.default.try(() => fn.apply(this, args))]);
    };
  });
});

function onResponseStatusCode(statusCode, eventBroadcaster) {
  return new _bluebird2.default(resolve => {
    function onResponse(data) {
      if (data.statusCode === statusCode) resolve();
    }

    eventBroadcaster.on('response', onResponse);

    eventBroadcaster.once('test-case-finished', () => eventBroadcaster.removeListener('response', onResponse));
  });
}
//# sourceMappingURL=response-watcher.js.map