#!/bin/bash
mergesInMaster=$(git rev-list origin/master --merges --count)
mergesInDevelop=$(git rev-list origin/develop --merges --count)
echo "v0.$mergesInMaster.$((mergesInDevelop - mergesInMaster))"