package it.polimi.ingsw.network.messages.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerVisitor;

public class DiscardResourceCommand implements Command{
    ResourceType resourceType;

    public DiscardResourceCommand(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean doAction(Controller c, String nickname) {
        Game game = c.getGame();
        if (check() && game.getCurrentNickname().equals(nickname)) {
            try {
                game.discardResource(resourceType);
            } catch (IllegalActionException | IndexOutOfBoundsException | NullPointerException | IllegalResourceException | DepositableResourceException e) {
                return false;
            }
            return true;
        } else return false;
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