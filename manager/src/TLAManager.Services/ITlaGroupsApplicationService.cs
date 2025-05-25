using TLAManager.Domain;

namespace TLAManager.Services;

public interface ITlaGroupsApplicationService
{
    Task<List<TLAGroup>> FindAllTlaGroupsAsync();
    Task<List<TLAGroup>> FindAllTlaGroupsAsync(TLAStatus status);
    Task<List<TLAGroup>> FindAllTlasByNameAsync(string name);
    Task<TLAGroup> FindGroupByNameAsync(string name);
    Task<TLAGroup> AddTlaGroupAsync(TLAGroup tlaGroup);
    Task<TLAGroup> AddTlaAsync(string groupName, ThreeLetterAbbreviation tla);
    Task<TLAGroup> AcceptTlaAsync(string groupName, string tlaName);
}