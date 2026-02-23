# ibosng-backend

This repository serves as the new single backend service for **ibosng**.

This application is structured as a modulith. Starting from the historic gateway-service all other distributed services
and packages are included as a module here. Starting point was the gateway-service's main branch.
Some changes were made to other branches after the last commit on the main branch but they
are not important.

This decision was made to reduce overall system complexity and improve development speed.

## Local Development Setup

#### 1. ⤵️ Clone [ibosng-backend repo](https://github.com/squer-solutions/ibosng-backend).

#### 2. 🔨 Build the project by running `./gradlew build`

#### 3. 💾 Setup Local Dev Environment

Run `docker compose up -d`, which will start:

- postgres instance + script create users
- mariadb instance + script to create schema
- redis instance

To initialize the postgres database upon first usage, migrate the flyway scripts: `./gradlew flywayMigrate`

#### 4. Run `IbosngBackendApplication`

- run with profile "localdev" to ensure properties from application-localdev.yaml are applied.
- Authentication-flow with Frontend -> see [here](CONFIGURATION.md)
- optional: Run connecting to the Moxis test instance 
  - set MOXIS parameters -> see [here](CONFIGURATION.md)
  - ensure being connected to the Ibis VPN (consult wiki documentation) so the MOXIS url can be reached
  
- Consult the documentation [here](CONFIGURATION.md) for info about environment variables to set.

## Running Tests

When running tests, testcontainer instance for PostgreSQL, MariaDB and Redis will be created.
It is possible to apply database-dumps on these instances, see [here](CONFIGURATION.md).

## Environment Variables

See the documentation [here](CONFIGURATION.md).
