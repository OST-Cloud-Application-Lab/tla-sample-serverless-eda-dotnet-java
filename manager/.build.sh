#!/bin/bash

dotnet tool restore
dotnet restore
dotnet lambda package --project-location src/TLAManager.Infrastructure --configuration release --framework net8.0 --output-package bin/release/net8.0/deploy-package.zip