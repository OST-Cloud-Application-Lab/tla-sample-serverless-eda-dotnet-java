using System.Net;
using Amazon.Lambda.APIGatewayEvents;
using Amazon.Lambda.Core;
using Microsoft.Extensions.DependencyInjection;
using TLAManager.Infrastructure.WebApi.Mappers;
using TLAManager.Services;

namespace TLAManager.Infrastructure.WebApi.Functions;

public class GetAllTlasFunction : FunctionBase
{
    private static readonly string NameParam = "name";

    public async Task<APIGatewayProxyResponse> GetAllTlasAsync(APIGatewayProxyRequest request, ILambdaContext context)
    {
        context.Logger.LogInformation($"{nameof(GetAllTlasFunction)} called");

        using var scope = ServiceProvider.CreateScope();
        var service = scope.ServiceProvider.GetService<ITlaGroupsApplicationService>()!;
        var responseFactory = scope.ServiceProvider.GetService<ResponseFactory>()!;

        try
        {
            var name = request.PathParameters[NameParam];
            var tlaGroups = await service.FindAllTlasByNameAsync(name);
            var tlaGroupDtos = tlaGroups
                .Select(TlaApiDtoMapper.TlaGroupToDto)
                .ToList();

            context.Logger.LogInformation($"{nameof(GetAllTlasFunction)} returning {tlaGroups.Count} TLA groups");

            var statusCode = tlaGroupDtos.Any() ? HttpStatusCode.OK : HttpStatusCode.NotFound;
            return responseFactory.CreateResponse(tlaGroupDtos, statusCode);
        }
        catch (Exception e)
        {
            context.Logger.LogError(e, "Internal error has happened");
            return new APIGatewayProxyResponse
            {
                StatusCode = (int)HttpStatusCode.InternalServerError,
                Body = $"Internal error has happened: {e.Message}"
            };
        }
    }
}