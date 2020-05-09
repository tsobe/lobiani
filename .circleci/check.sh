#!/usr/bin/env bash

# THIS SCRIPT IS ONLY SUPPOSED TO BE CALLED BY CIRCLE CI

PROJECT_SLUG="bitbucket/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME"

function trigger_pipeline {
	local check=$1
	local build=$2
	local deploy=$3

    curl -s -u ${CIRCLE_API_USER_TOKEN}: \
	   -H "Content-Type: application/json" \
	   -d "{
			  \"parameters\": {
				  \"check\": $check,
				  \"build\": $build,
				  \"deploy\": $deploy
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
		commits="origin/master"
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
if was_modified $code_dir
then
	echo "Triggering the build"
  	trigger_pipeline false true false
else
  	echo "No changes made to $code_dir, not triggering the build"
fi

infra_dir="infra-config"
if was_modified $infra_dir
then
	echo "Triggering the deploy"
  	trigger_pipeline false false true
else
  	echo "No changes made to $infra_dir, not triggering the deploy"
fi
