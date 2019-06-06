#!/usr/bin/env bash

if [[ "$TRAVIS_PULL_REQUEST" != "false" ]] ; then
BOT_URL="https://api.telegram.org/bot${telegram_bot_token}/sendMessage"

PARSE_MODE="Markdown"

send_msg () {
    curl -s -X POST ${BOT_URL} -d chat_id=@starlord_team \
        -d text="$1" -d parse_mode=${PARSE_MODE}
}

send_msg "
Code-review time!

Guys, take a moment to review [pull request](https://github.com/suhocki/starlord/pull/${TRAVIS_PULL_REQUEST}) made by ${AUTHOR_NAME}, please.
"
fi
