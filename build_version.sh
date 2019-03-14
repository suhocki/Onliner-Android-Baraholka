#!/bin/bash
majorVersionNumber=$(git rev-list origin/master --merges --count)
minorVersionNumber=$(git rev-list origin/develop --count)
echo "v0.$majorVersionNumber.$minorVersionNumber"
