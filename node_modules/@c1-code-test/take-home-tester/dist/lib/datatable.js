'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.readTableValuesAsJson = readTableValuesAsJson;

var _ramda = require('ramda');

var R = _interopRequireWildcard(_ramda);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function readTableValuesAsJson({ rawTable: data }) {
  const values = [];
  for (let i = 1; i < data.length; i++) {
    values.push(R.zipObj(data[0], data[i].map(v => v ? JSON.parse(v) : undefined)));
  }

  return values;
}
//# sourceMappingURL=datatable.js.map