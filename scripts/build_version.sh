#!/bin/bash
mergesInMasterFromDevelop=$(git shortlog origin/master --merges | grep ".*\/develop" | wc -l)
mergesInMaster=$(git rev-list origin/master --merges --count)
mergesInDevelop=$(git rev-list origin/develop --merges --count)
minorVersion=0
if [[ ${mergesInDevelop} -gt ${mergesInMaster} ]]
then
    minorVersion=$((mergesInDevelop - mergesInMaster))
fi
echo "v0.$mergesInMasterFromDevelop.$minorVersion"