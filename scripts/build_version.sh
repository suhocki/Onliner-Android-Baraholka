#!/bin/bash
mergesInMasterFromDevelop=$(git shortlog origin/master --merges | grep ".*\/develop" | wc -l)
mergesInMaster=$(git rev-list origin/master --merges --count)
mergesInDevelop=$(git rev-list origin/develop --merges --count)
if [[ ${mergesInDevelop} -gt ${mergesInMaster} ]]
    then echo "v0.$mergesInMasterFromDevelop.$((mergesInDevelop - mergesInMaster))"
    else echo "v0.$mergesInMasterFromDevelop"
fi
