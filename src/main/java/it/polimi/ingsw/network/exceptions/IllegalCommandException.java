package it.polimi.ingsw.network.exceptions;

public class IllegalCommandException extends Exception{
    @Override
    public String getMessage() {
        return "Illegal command";
    }
}
