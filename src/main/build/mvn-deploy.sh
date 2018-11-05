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

# from https://stackoverflow.com/a/26514030/3281722
PROJECT_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)
echo "Maven project version is '${PROJECT_VERSION}'"
echo

if [ "$TRAVIS_TAG" != "" ]; then
    echo "Releasing version '${TRAVIS_TAG}'"
    echo
    if [ "$PROJECT_VERSION" != "$TRAVIS_TAG" ]; then
        echo "ERROR: Project version does not equal Travis-CI Tag!" >&2
        exit 1
    fi
    echo
elif [[ "$PROJECT_VERSION" != *-SNAPSHOT ]]; then
    echo "ERROR: Project version is no SNAPSHOT version, but no tag is being built" >&2
    exit 1
fi

echo "Deploying to repository"
echo
mvn deploy -P sign,build-extras -DskipTests=true --settings $DIR/mvn-settings.xml
echo
