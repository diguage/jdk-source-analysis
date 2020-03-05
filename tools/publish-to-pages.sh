#!/usr/bin/env bash

cd ..

mvn clean package

git stash

git switch gh-pages

rm -rf images assets *.html *.svg

mv target/docs/html/* .

git add -A

git commit -am "`date +"%Y-%m-%d %H:%M:%S"`"

git push -f origin gh-pages

git switch master

