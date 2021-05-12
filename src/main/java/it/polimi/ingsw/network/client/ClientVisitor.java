package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.NewTurnMessage;
import it.polimi.ingsw.network.messages.updates.DepositUpdate;

public interface ClientVisitor {
    void visit(DisconnectionMessage message, Client client);
    void visit(GenericMessage message, Client client);
    void visit(LobbyInfoMessage message, Client client);
    void visit(PingRequest message, Client client);
    void visit(PlayerNumberRequest message, Client client);
    void visit(RegisterRequest message, Client client);
    void visit(StartGameMessage message, Client client);
    void visit(NewTurnMessage message, Client client);
    void visit(BonusResourceMessage message, Client client);
    void visit(PendingResourcesMessage message, Client client);
    void visit(ReconnectMessage message, Client client);

    void visit(DepositUpdate update, Client client);
}
