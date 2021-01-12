#!/usr/bin/env bash

set -e

# THIS SCRIPT IS ONLY SUPPOSED TO BE CALLED BY CIRCLE CI

PROJECT_SLUG="github/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME"

function trigger_pipeline {
    local check=$1
    local build_backend=$2
    local build_admin=$3
    local test=$4

    curl -s -u ${CIRCLE_API_USER_TOKEN}: \
	   -H "Content-Type: application/json" \
	   -d "{\
              \"parameters\": {\
                \"check\": $check,\
                \"build_backend\": $build_backend,\
                \"build_admin\": $build_admin,\
                \"test\": $test\
              }
		   }" \
       https://circleci.com/api/v2/project/$PROJECT_SLUG/pipeline
}

function collect_modified_dirs {
    # Originally influenced by
    # https://github.com/Tufin/circleci-monorepo/blob/master/.circleci/config.yml
    last_successful_build_url="https://circleci.com/api/v1.1/project/$PROJECT_SLUG/tree/$CIRCLE_BRANCH?filter=successful&limit=1"
    last_successful_commit=`curl -Ss -u "$CIRCLE_API_USER_TOKEN:" $last_successful_build_url | jq -r '.[0]["vcs_revision"]'`

    # First commit in a branch
    if [[ ${last_successful_commit} == "null" || -z "${last_successful_commit}" ]]
    then
        commits="origin/$CIRCLE_BRANCH"
    else
        commits="${CIRCLE_SHA1}..${last_successful_commit}"
    fi

    git diff --name-only $commits | cut -d/ -f1 | sort -u > modified-dirs
    echo -e "Modified directories:\n`cat modified-dirs`\n"
}

function was_modified {
    grep -Fxq $1 modified-dirs
    return $?
}

collect_modified_dirs

backend_dir="app-backend"
admin_dir="admin"
infra_config_dir="infra-config"
tests_dir="e2e-tests"

pipeline_triggered=false

if was_modified $backend_dir
then
    echo "Triggering pipeline: \"build_backend\""
    trigger_pipeline false true false false
    pipeline_triggered=true
fi

if was_modified $admin_dir
then
    echo "Triggering pipeline: \"build_admin\""
    trigger_pipeline false false true false
    pipeline_triggered=true
fi

if was_modified $infra_config_dir || was_modified $tests_dir
then
    echo "Triggering pipeline: \"test\""
    trigger_pipeline false false false true
    pipeline_triggered=true
fi

if ! $pipeline_triggered
then
    echo "No changes made to $backend_dir, $admin_dir, $infra_config_dir or $tests_dir, not triggering any pipeline"
fi
