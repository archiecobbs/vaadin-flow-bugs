#!/bin/bash

set -ex

# Keep this synchronized with .gitignore
rm -f frontend/index.html
rm -f package-lock.json
rm -f package.json
rm -f tsconfig.json
rm -f types.d.ts
rm -f vite.config.ts
rm -f vite.generated.ts
rm -rf frontend/generated
rm -rf node_modules
rm -rf src/main/bundles/

rm -rf target
