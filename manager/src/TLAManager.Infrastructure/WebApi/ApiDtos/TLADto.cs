using System.Text.Json.Serialization;

namespace TLAManager.Infrastructure.WebApi.Dtos;

public class TLADto
{
    public string Name { get; }

    public string Meaning { get; }

    [JsonIgnore(Condition = JsonIgnoreCondition.WhenWritingNull)]
    public ISet<string>? AlternativeMeanings { get; private set; }

    [JsonIgnore(Condition = JsonIgnoreCondition.WhenWritingNull)]
    public string? Link { get; private set; }

    public TLADto(string name, string meaning)
    {
        Name = name;
        Meaning = meaning;
    }

    public TLADto WithAlternativeMeanings(ISet<string> alternativeMeanings)
    {
        AlternativeMeanings = alternativeMeanings;
        return this;
    }

    public TLADto WithLink(string? link)
    {
        Link = link;
        return this;
    }
}