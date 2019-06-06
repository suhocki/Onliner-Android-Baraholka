#!/usr/bin/env bash

BOT_URL="https://api.telegram.org/bot${telegram_bot_token}/sendMessage"

PARSE_MODE="Markdown"

send_msg () {
    curl -s -X POST ${BOT_URL} -d chat_id=@starlord_team \
        -d text="$1" -d parse_mode=${PARSE_MODE}
}

send_msg "
New release is available on [Google Play](https://play.google.com/store/apps/details?id=kt.school.starlord)!

Whats new:
\`• ${GITHUB_ISSUE}\`
"
