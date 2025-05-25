using Microsoft.Extensions.DependencyInjection;
using TLAManager.Infrastructure.Extensions;
using TLAManager.Services.Extensions;

namespace TLAManager.Infrastructure.WebApi;

public abstract class FunctionBase
{
    protected ServiceProvider ServiceProvider;

    protected FunctionBase()
    {
        var services = new ServiceCollection();
        services.AddServices().AddInfrastructure();
        ServiceProvider = services.BuildServiceProvider();
    }
}