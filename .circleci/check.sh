#!/usr/bin/env bash

set -e

# THIS SCRIPT IS ONLY SUPPOSED TO BE CALLED BY CIRCLE CI

PROJECT_SLUG="github/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME"

function trigger_pipeline {
	local check=$1
	local build=$2
	local test=$3

    curl -s -u ${CIRCLE_API_USER_TOKEN}: \
	   -H "Content-Type: application/json" \
	   -d "{
			  \"parameters\": {
				  \"check\": $check,
				  \"build\": $build,
				  \"test\": $test
			  }
		   }" \
	   https://circleci.com/api/v2/project/$PROJECT_SLUG/pipeline
}

function collect_modified_dirs {
	# Originally influenced by
	# https://github.com/Tufin/circleci-monorepo/blob/master/.circleci/config.yml
  	last_completed_build_url="https://circleci.com/api/v1.1/project/$PROJECT_SLUG/tree/$CIRCLE_BRANCH?filter=completed&limit=1"
  	last_successful_commit=`curl -Ss -u "$CIRCLE_API_USER_TOKEN:" $last_completed_build_url | jq -r '.[0]["vcs_revision"]'`

	# First commit in a branch
	if [[ ${last_successful_commit} == "null" ]]
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

code_dir="app"
infra_config_dir="infra-config"

if was_modified $code_dir
then
	echo "Triggering pipeline: \"build\""
  	trigger_pipeline false true false
fi

if was_modified $infra_config_dir
then
	echo "Triggering pipeline: \"test\""
	trigger_pipeline false false true
fi

if ! was_modified $code_dir && ! was_modified $infra_config_dir
then
  	echo "No changes made to $code_dir or $infra_config_dir, not triggering any pipeline"
fi
