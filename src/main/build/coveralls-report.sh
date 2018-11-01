#!/usr/bin/env bash
set -e

mvn clean test jacoco:report coveralls:report -P record-coverage --batch-mode