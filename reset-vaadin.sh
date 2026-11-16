#!/bin/bash

# Discard leftover Vaadin junk

rm -rf \
  frontend \
  src/main/frontend/generated \
  src/main/frontend/index.html \
  src/main/bundles \
  node_modules \
  package-lock.json \
  package.json \
  src/main/bundles \
  src/main/resources/VAADIN \
  tsconfig.json \
  types.d.ts \
  .vaadin-node-tasks.lock \
  vite.config.ts \
  vite.generated.ts
