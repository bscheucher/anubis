# Configuration Overview

Documents the necessary configurations and their per-environment permutations.

## Variables

| Variable            | Description                                                                                                           |
|---------------------|-----------------------------------------------------------------------------------------------------------------------|
| `natifBaseEndpoint` | Base URL of the Natif AI API.                                                                                         |
| `natifApiKey`       | API Key for the Natif AI API.                                                                                         |
| `jobs.enabled`      | If "true", scheduled jobs are performed. <br/>If set to anything else or missing, scheduled job will not be performed |

## Local

Run in profile `localdev`.

### Optional: Auth flow with the frontend

Make sure the following env-variables are set:

| Variable            | Value                   |
|---------------------|-------------------------|
| `SSO_CLIENT_ID`     | Consult Frontend Readme |
| `SSO_CLIENT_SECRET` | Consult Frontend Readme |
| `SSO_TENANT_ID`     | Consult Frontend Readme |

### Optional: Enable scheduled jobs (inactive per default)

#### Note: These won't affect EnableScheduling (which is always set, except in profile "test"), but lead to the (non-)registering of beans containing the scheduled jobs.

| Variable                                     | Value                                       |
|----------------------------------------------|---------------------------------------------|
| `LEGACY_SCHEDULERS_ENABLED`                  | true                                        |
| `LHR_OUTBOX_SCHEDULER_ENABLED`               | true                                        |
| `LHR_OUTBOX_SCHEDULER_DELAY_IN_MILLISECONDS` | <millseconds as string; defaults to "1000"> |

### Optional: Connect to the test Moxis instance (ensure working connection to VPN):

| Variable         | Value                                                         |
|------------------|---------------------------------------------------------------|
| `USE_TEST_MOXIS` | true                                                          |
| `MOXIS_USERNAME` | _ask a colleague for the "API USER"_                          |
| `MOXIS_PASSWORD` | _ask a colleague for the "API USER" password_                 |
| `MOXIS_URL`      | https://moxisdev.ibisacam.at/webservices/rest/api/layer2/v1.0 |

### Optional: Connect to LHR Localdev-Firma

| Variable       | Value                                         |
|----------------|-----------------------------------------------|
| `USE_LHR_API`  | true                                          |
| `LHR_USERNAME` | _ask a colleague for the "API USER"_          |
| `LHR_PASSWORD` | _ask a colleague for the "API USER" password_ |

### Running Tests

To Apply database-dumps in .gz format, runs tests with the following env-vars:

| Variable                | Value                                       |
|-------------------------|---------------------------------------------|
| `APPLY_POSTGRESQL_DUMP` | true                                        |
| `POSTGRESQL_DUMP_PATH`  | <path to .gz file relative to project root> |
| `APPLY_MARIADB_DUMP`    | true                                        |
| `MARIADB_DUMP_PATH`     | <path to .gz file relative to project root> |

## Test

...

## Pre-Prod

...

## Production

...
