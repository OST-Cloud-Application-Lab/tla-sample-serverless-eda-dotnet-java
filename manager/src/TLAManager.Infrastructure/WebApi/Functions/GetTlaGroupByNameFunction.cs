using System.Net;
using Amazon.Lambda.APIGatewayEvents;
using Amazon.Lambda.Core;
using Microsoft.Extensions.DependencyInjection;
using TLAManager.Services;
using TLAManager.Infrastructure.WebApi.Mappers;
using TLAManager.Services.Exceptions;

namespace TLAManager.Infrastructure.WebApi.Functions;

public class GetTlaGroupByNameFunction : FunctionBase
{
    private static readonly string GroupNameParam = "groupName";

    public async Task<APIGatewayProxyResponse> GetTlaGroupByNameAsync(APIGatewayProxyRequest request,
        ILambdaContext context)
    {
        context.Logger.LogInformation($"{nameof(GetTlaGroupByNameFunction)} called");

        using var scope = ServiceProvider.CreateScope();
        var service = scope.ServiceProvider.GetService<ITlaGroupsApplicationService>()!;
        var responseFactory = scope.ServiceProvider.GetService<ResponseFactory>()!;

        try
        {
            var name = request.PathParameters[GroupNameParam];
            var tlaGroup = await service.FindGroupByNameAsync(name);
            var tlaGroupDto = TlaApiDtoMapper.TlaGroupToDto(tlaGroup);
            return responseFactory.CreateResponse(tlaGroupDto, HttpStatusCode.OK);
        }
        catch (TLAGroupNameDoesNotExistException e)
        {
            context.Logger.LogError(e, "TLA group not found");
            return responseFactory.CreateErrorResponse(HttpStatusCode.NotFound, e.Message, context);
        }
        catch (TLAGroupNameNotValidException e)
        {
            context.Logger.LogError(e, "TLA group name not valid");
            return responseFactory.CreateErrorResponse(HttpStatusCode.NotFound, e.Message, context);
        }
        catch (Exception e)
        {
            context.Logger.LogError(e, "Internal server error");
            return responseFactory.CreateErrorResponse(HttpStatusCode.InternalServerError, e.Message, context);
        }
    }
}