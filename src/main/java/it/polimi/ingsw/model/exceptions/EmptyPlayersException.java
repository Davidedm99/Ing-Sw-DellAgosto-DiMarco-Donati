package it.polimi.ingsw.model.exceptions;

public class EmptyPlayersException extends  Exception{
    public EmptyPlayersException() { super(); }
    public EmptyPlayersException(String message) { super(message); }
    @Override
    public String getMessage() {
        return "Empty players";
    }
}
