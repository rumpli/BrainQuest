# 1. Clean npm cache
 npm cache clean --force

 # 2. Remove node_modules & reinstall
 rm -rf node_modules package-lock.json
 npm install

 # 3. Prune devDependencies
 npm prune --production

 # 4. Remove unnecessary files from node_modules
 npx modclean -r -n default:safe

 # 5. Dedupe dependencies
 npm dedupe

 # 6. Audit and fix security issues
 npm audit fix --force

