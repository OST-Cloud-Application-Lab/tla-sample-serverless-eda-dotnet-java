namespace TLAManager.Domain;

public class ShortName : IComparable<ShortName>
{
    public string Name { get; }

    public ShortName(string name)
    {
        if (string.IsNullOrWhiteSpace(name))
        {
            throw new ArgumentException("ShortName cannot be empty", nameof(name));
        }

        if (name.Contains(' '))
        {
            throw new ArgumentException("A single short name cannot contain spaces.", nameof(name));
        }

        Name = name;
    }

    public override bool Equals(object? obj)
    {
        if (obj is ShortName other)
        {
            return string.Equals(Name, other.Name, StringComparison.Ordinal);
        }

        return false;
    }

    public override int GetHashCode()
    {
        return Name.GetHashCode(StringComparison.Ordinal);
    }

    public int CompareTo(ShortName? other)
    {
        return string.Compare(Name, other?.Name, StringComparison.OrdinalIgnoreCase);
    }
}