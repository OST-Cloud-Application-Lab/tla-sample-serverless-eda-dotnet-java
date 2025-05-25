namespace TLAManager.Domain.Exceptions;

public class TLANameAlreadyExistsInGroupException(string message) : Exception(message);
