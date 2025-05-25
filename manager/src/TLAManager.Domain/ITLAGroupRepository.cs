namespace TLAManager.Domain;

public interface ITLAGroupRepository
{
    Task<TLAGroup> SaveAsync(TLAGroup group);

    Task<TLAGroup?> FindByNameAsync(ShortName name);

    Task<List<TLAGroup>> FindAllAsync();
}