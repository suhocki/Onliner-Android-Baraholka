#!/bin/bash
x=$(git rev-list origin/master --count)
y=$(git rev-list origin/develop --count)
ans=$(( y - x ))
echo "v0.$x.$ans"
