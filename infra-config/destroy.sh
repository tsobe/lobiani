#!/usr/bin/env bash

set -e

env=${1:-test}

if [[ "$env" == "test" ]]
then
    argocd_label="environment=test"
elif [[ "$env" == "prod" ]]
then
    argocd_label="environment=production"
else
    echo "Usage: $0 <test|prod>"
    exit 1
fi

if [[ "$env" == "prod" ]]
then
    read -ep "You are going to destroy production infra. Type \"yes\" to continue: " ack
    if [[ "$ack" != "yes" ]]
    then
        exit
    fi
fi

running_apps_count="`argocd app list -l $argocd_label -o name | grep -Ev "(apps|cert-manager)" | wc -l | xargs`"
if [[ $running_apps_count -gt 0 ]]
then
    argocd app list -l $argocd_label -o name | grep -Ev "(apps|cert-manager)" | xargs argocd app delete
    while [[ $running_apps_count -gt 0 ]]
    do
        running_apps_count="`argocd app list -l $argocd_label -o name | grep -Ev "(apps|cert-manager)" | wc -l | xargs`"
        echo "1st pass: waiting for $running_apps_count apps to get deleted..."
    done
fi


running_apps_count="`argocd app list -l $argocd_label -o name | wc -l | xargs`"
if [[ $running_apps_count -gt 0 ]]
then
    argocd app list -l $argocd_label -o name | xargs argocd app delete
    while [[ $running_apps_count -gt 0 ]]
    do
        running_apps_count="`argocd app list -l $argocd_label -o name | wc -l | xargs`"
        echo "2nd pass: waiting for $running_apps_count apps to get deleted..."
    done
fi


cd terraform/$env

tf_state="`terraform state list`"
while [[ -n "$tf_state" ]]
do
    terraform destroy -auto-approve
    tf_state="`terraform state list`"
done
