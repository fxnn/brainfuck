#!/usr/bin/env bash
# from http://www.debonair.io/post/maven-cd/
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_8f8b80a3f07f_key -iv $encrypted_8f8b80a3f07f_iv -in src/main/deploy/codesigning.asc.enc -out src/main/deploy/codesigning.asc -d
    gpg --fast-import src/main/deploy/codesigning.asc
fi