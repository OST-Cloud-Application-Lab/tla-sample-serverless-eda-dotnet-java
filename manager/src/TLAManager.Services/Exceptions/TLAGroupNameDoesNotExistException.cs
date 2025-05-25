namespace TLAManager.Services.Exceptions;

public class TLAGroupNameDoesNotExistException(string name) : Exception($"A TLA group '{name}' does not exist!");