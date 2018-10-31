#!/usr/bin/env bash
# from http://www.debonair.io/post/maven-cd/
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -P sign,build-extras --settings src/main/deploy/mvnsettings.xml
fi