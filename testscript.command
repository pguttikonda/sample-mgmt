#!/bin/bash

if automator -v '/Users/pguttikonda/Downloads/test_workflow.workflow' | grep " completed."; then
	echo "Automator workflow completed successfully"
else
	echo "Something failed with the workflow or the script"
fi
