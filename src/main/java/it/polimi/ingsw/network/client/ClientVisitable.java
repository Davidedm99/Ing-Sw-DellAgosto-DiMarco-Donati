package it.polimi.ingsw.network.client;

public interface ClientVisitable {
    void accept(ClientVisitor visitor, Client client);
}
