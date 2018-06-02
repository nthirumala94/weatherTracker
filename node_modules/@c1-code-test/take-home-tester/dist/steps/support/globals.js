'use strict';

var _chai = require('chai');

var _chai2 = _interopRequireDefault(_chai);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

_chai2.default.use(require('chai-as-promised'));
_chai2.default.use(require('dirty-chai'));

global.expect = _chai2.default.expect;
global.AssertionError = _chai2.default.AssertionError;

global.single = arr => {
  if (!Array.isArray(arr)) return arr;

  if (arr.length !== 1) throw new Error('Must have exactly one item.');

  return arr[0];
};
//# sourceMappingURL=globals.js.map