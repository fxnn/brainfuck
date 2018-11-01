#!/usr/bin/env bash
# based on http://www.debonair.io/post/maven-cd/

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

echo "Importing GPG key"
echo
openssl aes-256-cbc \
    -K $encrypted_8f8b80a3f07f_key -iv $encrypted_8f8b80a3f07f_iv \
    -in $DIR/codesigning.asc.enc -out $DIR/codesigning.asc -d
gpg --fast-import $DIR/codesigning.asc
echo

if [ "$TRAVIS_TAG" != "" ]; then
    echo "Releasing version $TRAVIS_TAG"
    echo
    mvn versions:set -DnewVersion=$TRAVIS_TAG -DgenerateBackupPoms=false
    echo
fi

echo "Deploying to repository"
echo
mvn deploy -P sign,build-extras --settings $DIR/mvnsettings.xml
echo
