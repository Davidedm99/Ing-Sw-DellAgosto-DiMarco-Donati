package it.polimi.ingsw.model.exceptions;

public class RequirementNotMetException extends Exception{
    public RequirementNotMetException() { super(); }
    public RequirementNotMetException(String message) { super(message); }
}