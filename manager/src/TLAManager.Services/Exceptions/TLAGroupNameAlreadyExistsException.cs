namespace TLAManager.Services.Exceptions;

public class TLAGroupNameAlreadyExistsException(string name)
    : Exception($"A TLA group with name '{name}' already exists!");