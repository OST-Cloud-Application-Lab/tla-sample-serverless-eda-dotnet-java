using Microsoft.Extensions.DependencyInjection;
using TLAManager.Domain;
using TLAManager.Infrastructure.Persistence;
using TLAManager.Infrastructure.WebApi;

namespace TLAManager.Infrastructure.Extensions;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services)
    {
        services.AddTransient<ITLAGroupRepository, TLAGroupRepository>();
        services.AddTransient<DynamoDbThreeLetterAbbreviationRepository>();
        services.AddTransient<ResponseFactory>();
        return services;
    }
}