using Amazon.Lambda.Core;
using Microsoft.Extensions.DependencyInjection;
using TLAManager.Domain;
using TLAManager.Infrastructure.WebApi;
using TLAManager.Services;

namespace TLAManager.Infrastructure.Migration;

public class DatabaseSeedHandler : FunctionBase
{
    public async Task<string> SeedAsync(string unused, ILambdaContext context)
    {
        using var scope = ServiceProvider.CreateScope();
        var service = scope.ServiceProvider.GetService<ITlaGroupsApplicationService>()!;

        try
        {
            await service.AddTlaGroupAsync(new TLAGroup(
                new ShortName("common"),
                "Common Tla group",
                [
                    new(
                        new ShortName("TLA"),
                        "Three Letter Abbreviation",
                        new List<string> { "Three Letter Acronym" },
                        null,
                        TLAStatus.Accepted
                    )
                ]
            ));

            await service.AddTlaGroupAsync(new TLAGroup(
                new ShortName("AppArch"),
                "Application Architecture",
                [
                    new(
                        new ShortName("ADR"),
                        "Architectural Decision Record",
                        new List<string>(),
                        "https://adr.github.io/",
                        TLAStatus.Accepted
                    )
                ]
            ));

            await service.AddTlaGroupAsync(new TLAGroup(
                new ShortName("DDD"),
                "Domain-Driven Design",
                [
                    new(
                        new ShortName("OHS"),
                        "Open Host Service",
                        new List<string>(),
                        null,
                        TLAStatus.Accepted
                    ),
                    new(
                        new ShortName("PL"),
                        "Published Language",
                        new List<string>(),
                        null,
                        TLAStatus.Accepted
                    ),
                    new(
                        new ShortName("CF"),
                        "Conformist",
                        new List<string>(),
                        null,
                        TLAStatus.Accepted
                    ),
                    new(
                        new ShortName("SK"),
                        "Shared Kernel",
                        new List<string>(),
                        null,
                        TLAStatus.Accepted
                    ),
                    new(
                        new ShortName("ACL"),
                        "Anticorruption Layer",
                        new List<string>(),
                        null,
                        TLAStatus.Accepted
                    )
                ]
            ));
        }
        catch (Exception e)
        {
            context.Logger.LogError(e, "Internal error has happened");
            return e.Message;
        }

        return "ok";
    }
}