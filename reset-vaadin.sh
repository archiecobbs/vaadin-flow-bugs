#!/bin/bash

# Discard all the cached crap that was downloaded by the vaadin-maven-plugin.
# Note "package-lock.json" is under source control so any changes will need
# be committed after you rebuild.

rm -rf \
  frontend \
  node_modules \
  package-lock.json \
  package.json \
  tsconfig.json \
  types.d.ts \
  webpack.config.js \
  webpack.generated.js
