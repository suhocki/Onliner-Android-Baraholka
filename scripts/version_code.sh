#!/bin/bash
versionCode=$(git shortlog origin/master --merges | grep ".*\/develop" | wc -l)
if [[ ${versionCode} -le 0 ]]
then
    versionCode=1
fi
echo ${versionCode}