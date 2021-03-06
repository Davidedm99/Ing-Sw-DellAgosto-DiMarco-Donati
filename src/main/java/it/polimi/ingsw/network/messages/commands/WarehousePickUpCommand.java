package it.polimi.ingsw.network.messages.commands;

import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.exceptions.IllegalCommandException;
import it.polimi.ingsw.network.exceptions.NotYourTurnException;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Controller;
import it.polimi.ingsw.network.server.ServerVisitor;

public class WarehousePickUpCommand implements Command{
    int index;

    public WarehousePickUpCommand(int index) {
        this.index = index;
    }

    @Override
    public void doAction(Controller c, String nickname) throws IllegalCommandException, NotYourTurnException, IllegalActionException, ResourcesNotAvailableException, NonEmptyException, DepositNotExistingException, WaitingReconnectionsException {
       if(check())c.pickUpResourceFromWarehouse(index,nickname);
       else throw new IllegalCommandException();
    }

    @Override
    public boolean check() {
        return index >= 0;
    }

    @Override
    public void accept(ServerVisitor visitor, ClientHandler clientHandler) {
        visitor.visit(this, clientHandler);
    }
}
