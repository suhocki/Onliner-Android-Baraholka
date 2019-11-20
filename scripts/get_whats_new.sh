#!/usr/bin/env bash

end_point="https://api.github.com/repos/suhocki/starlord/"
release_notes_file="app/src/main/play/release-notes/ru-RU/default.txt"
issues=$(curl -H "Authorization:token ${github_comment_bot_api_key}"  -X GET "${end_point}pulls?state=closed&per_page=1&sort=updated&direction=desc" | jq '[.[]][0]' | jq -r '.body' | grep -oP '(?<=#)[0-9]+')

for issue in ${issues} ; do
    release_note=$(curl -H "Authorization:token ${github_comment_bot_api_key}" -X GET "${end_point}issues/${issue}" | jq -r '.title')
	echo "• ${release_note}" >> ${release_notes_file} ;
done
