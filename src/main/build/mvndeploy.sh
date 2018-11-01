#!/usr/bin/env bash
# based on http://www.debonair.io/post/maven-cd/

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc \
        -K $encrypted_8f8b80a3f07f_key -iv $encrypted_8f8b80a3f07f_iv \
        -in $DIR/codesigning.asc.enc -out $DIR/codesigning.asc -d
    gpg --fast-import $DIR/codesigning.asc
    mvn deploy -P sign,build-extras --settings $DIR/mvnsettings.xml
fi