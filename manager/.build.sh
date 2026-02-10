#!/bin/bash

dotnet tool restore
dotnet restore
dotnet lambda package --project-location src/TLAManager.Infrastructure --configuration release --framework net10.0 --output-package bin/release/net10.0/deploy-package.zip