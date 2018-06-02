'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

exports.default = function ({ eventBroadcaster }) {
  eventBroadcaster.prependListener('gherkin-document', src => {
    for (const child of src.document.feature.children) {
      if (child.type !== 'Scenario') continue;

      removePointTags(child.tags);
    }
  });
};

function removePointTags(tagList) {
  const index = tagList.find(tag => tag.name.startsWith('@points='));
  tagList.splice(index, 1);
}
module.exports = exports['default'];
//# sourceMappingURL=strip-points-formatter.js.map