namespace TLAManager.Infrastructure.WebApi.Events;

public class TLAAcceptEventDto
{
    public string TlaGroupName { get; set; } = string.Empty;

    public string TlaGroupDescription { get; set; } = string.Empty;

    public string TlaName { get; set; } = string.Empty;

    public string TlaMeaning { get; set; } = string.Empty;

    public List<string> TlaAlternativeMeanings { get; set; } = [];

    public string? TlaLink { get; set; }
}