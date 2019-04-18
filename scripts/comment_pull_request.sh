#!/bin/bash
if [ "$TRAVIS_PULL_REQUEST" != "false" ] ; then
    pushd app/build/outputs
    zip -r build_artefacts.zip *
    popd
    GOOGLE_DRIVE_TOKEN="$(curl --request POST --data "$(cat keys/google-drive-credentials.json)" https://developers.google.com/oauthplayground/refreshAccessToken | jq -r '.access_token')"
    FILE_URL="$(curl -X POST -L -H "Authorization:Bearer $GOOGLE_DRIVE_TOKEN" -F "metadata={name:'build_artifacts.zip'};type=application/json;charset=UTF-8" -F " file=@app/build/outputs/build_artifacts.zip;type=application/zip" "https://www.googleapis.com/upload/drive/v2/files?uploadType=multipart" | jq -r '.alternateLink')"
    curl -H "Authorization:token ${github_comment_bot_api_key}" -X POST -d "{\"body\":\"[See build artifacts]($FILE_URL)\"}" "https://api.github.com/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"
fi