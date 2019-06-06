#!/usr/bin/env bash

release_notes_file="app/src/main/play/release-notes/ru-RU/default.txt"
current_build_version=$(./scripts/versionizer/versionizer.sh name)
BOT_URL="https://api.telegram.org/bot${telegram_bot_token}/sendMessage"
PARSE_MODE="Markdown"

send_msg () {
    curl -s -X POST ${BOT_URL} -d chat_id=@starlord_team \
        -d text="$1" -d parse_mode=${PARSE_MODE}
}

send_msg "
New release is available on [Google Play](https://play.google.com/store/apps/details?id=kt.school.starlord)!

Whats new in v${current_build_version}:
\`$(cat ${release_notes_file})\`
"
