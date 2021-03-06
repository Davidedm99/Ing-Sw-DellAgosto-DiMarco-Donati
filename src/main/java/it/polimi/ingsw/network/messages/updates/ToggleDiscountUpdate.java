package it.polimi.ingsw.network.messages.updates;

import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientVisitor;

public class ToggleDiscountUpdate implements Update {
    private final ResourceType resourceType;

    public ToggleDiscountUpdate(ResourceType resourceType){
        this.resourceType = resourceType;
    }

    @Override
    public void update(ClientModel clientModel) {
        clientModel.getCurrentBoard().toggleDiscount(resourceType);
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void accept(ClientVisitor visitor, Client client) {
        visitor.visit(this, client);
    }
}
