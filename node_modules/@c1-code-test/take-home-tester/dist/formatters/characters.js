'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.statusCharacters = undefined;

var _cucumber = require('cucumber');

const statusCharacters = exports.statusCharacters = {
  [_cucumber.Status.PASSED]: '✔',
  [_cucumber.Status.FAILED]: '✘',
  [_cucumber.Status.PENDING]: '✏',
  [_cucumber.Status.SKIPPED]: '-',
  [_cucumber.Status.UNDEFINED]: '?'
};
//# sourceMappingURL=characters.js.map