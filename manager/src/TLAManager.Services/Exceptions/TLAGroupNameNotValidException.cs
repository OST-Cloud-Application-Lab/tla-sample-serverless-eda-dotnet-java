namespace TLAManager.Services.Exceptions;

public class TLAGroupNameNotValidException(string name) : Exception($"'{name}' is not a valid TLA group name!");