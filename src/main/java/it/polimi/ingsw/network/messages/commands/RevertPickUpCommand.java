package it.polimi.ingsw.network.messages.commands;

import it.polimi.ingsw.model.exceptions.FullSpaceException;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.model.exceptions.WaitingReconnectionsException;
import it.polimi.ingsw.network.exceptions.NotYourTurnException;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Controller;
import it.polimi.ingsw.network.server.ServerVisitor;

public class RevertPickUpCommand implements Command{

    @Override
    public void doAction(Controller c, String nickname) throws NotYourTurnException, IllegalResourceException, IllegalActionException, FullSpaceException, WaitingReconnectionsException {
        c.revertPickUp(nickname);
    }

    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void accept(ServerVisitor visitor, ClientHandler clientHandler) {
        visitor.visit(this, clientHandler);
    }
}
