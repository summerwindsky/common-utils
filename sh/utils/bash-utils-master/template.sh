#!/bin/bash

# locate dir
BASEDIR=$(dirname "$0")
cd "$BASEDIR" || exit
CURRENT_DIR=$(pwd)

# source utils
source "${CURRENT_DIR}"/utils.sh
utils.sh
# do something
if_file_not_exist_then_to