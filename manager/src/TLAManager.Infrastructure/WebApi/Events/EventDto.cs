namespace TLAManager.Infrastructure.WebApi.Events;

public class EventDto<T>
{
    public string UtcTimestamp { get; set; } = string.Empty;

    public string EventType { get; set; } = string.Empty;

    public T EventData { get; set; }
}