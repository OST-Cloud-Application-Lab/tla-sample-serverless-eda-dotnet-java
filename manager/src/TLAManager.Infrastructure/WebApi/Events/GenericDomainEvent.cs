namespace TLAManager.Infrastructure.WebApi.Events;

public class GenericDomainEvent<T>
{
    public required DomainEventMetadata Metadata { get; set; }

    public required T Data { get; set; }

}