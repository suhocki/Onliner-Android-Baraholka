#!/bin/bash
if [[ "$TRAVIS_PULL_REQUEST" != "false" ]] ; then
    pushd app/build/outputs
    zip -r output_artifacts.zip *
    popd
    GOOGLE_DRIVE_TOKEN="$(curl --request POST --data "$(cat keys/google-drive-credentials.json)" https://developers.google.com/oauthplayground/refreshAccessToken | jq -r '.access_token')"
    FILE_ID="$(curl -X POST -L -H "Authorization:Bearer $GOOGLE_DRIVE_TOKEN" -F "metadata={name:'output_artifacts.zip'};type=application/json;charset=UTF-8" -F " file=@app/build/outputs/output_artifacts.zip;type=application/zip" "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart" | jq -r '.id')"
    curl --request POST -H "Authorization:Bearer $GOOGLE_DRIVE_TOKEN" -H "Content-Type:application/json" --data "{\"role\":\"reader\",\"type\":\"anyone\"}" https://www.googleapis.com/drive/v3/files/${FILE_ID}/permissions?key=${GOOGLE_DRIVE_TOKEN}
    curl -H "Authorization:token ${github_comment_bot_api_key}" -X POST -d "{\"body\":\"Build successful. [Output artifacts](https://drive.google.com/uc?id=${FILE_ID}&export=download)\"}" "https://api.github.com/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"
fi
