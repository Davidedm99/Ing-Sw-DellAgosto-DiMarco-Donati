package it.polimi.ingsw.network.messages.commands;

import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.model.exceptions.UnknownNotFoundException;
import it.polimi.ingsw.model.exceptions.WaitingReconnectionsException;
import it.polimi.ingsw.network.exceptions.IllegalCommandException;
import it.polimi.ingsw.network.exceptions.NotYourTurnException;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Controller;
import it.polimi.ingsw.network.server.ServerVisitor;

public class ProductionUnknownCommand implements Command{
    String target;
    ResourceType resourceType;
    int index;

    public ProductionUnknownCommand(String target, ResourceType resourceType, int index) {
        this.target = target;
        this.resourceType = resourceType;
        this.index = index;
    }

    @Override
    public void doAction(Controller c, String nickname) throws IllegalCommandException, NotYourTurnException, IllegalResourceException, UnknownNotFoundException, IllegalActionException, WaitingReconnectionsException {
        if(check()){
            if(target.equals("input")) c.substituteUnknownInInputBaseProduction(resourceType,nickname);
            else c.substituteUnknownInOutputBaseProduction(resourceType,nickname);
        }else throw new IllegalCommandException();
    }

    @Override
    public boolean check() {
        return (target.equals("input") || target.equals("output")) && index >= 0;
    }

    @Override
    public void accept(ServerVisitor visitor, ClientHandler clientHandler) {
        visitor.visit(this, clientHandler);
    }
}
