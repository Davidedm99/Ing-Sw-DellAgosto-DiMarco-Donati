package it.polimi.ingsw.network.messages.updates;

import it.polimi.ingsw.network.client.CLI;
import it.polimi.ingsw.network.client.ClientModel.CLI.Resource;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientVisitor;

public class PickUpStrongboxUpdate implements Update {
    Resource resource;

    public PickUpStrongboxUpdate(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public void accept(ClientVisitor visitor, CLI client) {
        visitor.visit(this,client);
    }

    @Override
    public String getMessage() {
        return "picked up " + resource + " from the strongbox";
    }

    @Override
    public void update(ClientModel clientModel) {
        Resource res=clientModel.getCurrentBoard().getDeposits().removeResourceFromStrongbox(resource);
        clientModel.getCurrentBoard().getDeposits().putResourceInHand(res);
    }
}
