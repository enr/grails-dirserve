#!/usr/bin/env bash

set -e

[[ -d target ]] && rm -rf target
rm -rf *.zip
chmod +x ./grailsw
./grailsw refresh-dependencies --non-interactive -plain-output
./grailsw compile --non-interactive -plain-output
./grailsw codenarc --non-interactive -plain-output
./grailsw test-app --non-interactive -plain-output
./grailsw package-plugin --non-interactive -plain-output

if [[ "${TRAVIS_BRANCH}" == 'master' && "${TRAVIS_REPO_SLUG}" == "enr/grails-dirserve" && "${TRAVIS_PULL_REQUEST}" == 'false' ]]; then
    ./grailsw publish-plugin --no-scm --allow-overwrite --non-interactive -plain-output
else
    echo "Not on master branch, so not publishing"
    echo "TRAVIS_BRANCH: $TRAVIS_BRANCH"
    echo "TRAVIS_REPO_SLUG: $TRAVIS_REPO_SLUG"
    echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
fi
