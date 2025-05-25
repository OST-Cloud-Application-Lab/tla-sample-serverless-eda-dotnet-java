namespace TLAManager.Infrastructure.WebApi.Dtos;

public class TLAGroupDto
{
    public string Name { get; }

    public string Description { get; }

    public List<TLADto> Tlas { get; }

    public TLAGroupDto(string name, string description, List<TLADto> tlas)
    {
        Name = name;
        Description = description;
        Tlas = tlas;
    }
}