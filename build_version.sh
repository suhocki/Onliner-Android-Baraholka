#!/bin/bash
commitsOnMasterBranch=$(git rev-list origin/master --count)
commitsOnDevelopBranch=$(git rev-list origin/develop --count)
minorVersionNumber=$((commitsOnDevelopBranch - commitsOnMasterBranch))
majorVersionNumber=$(git rev-list origin/master --merges --count)
echo "v0.$majorVersionNumber.$minorVersionNumber"