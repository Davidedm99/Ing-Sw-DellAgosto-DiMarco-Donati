package it.polimi.ingsw.network.messages.updates;

import it.polimi.ingsw.model.Result;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.client.CLI;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientVisitor;

public class EndGameResultUpdate implements Update{
    private Result result;

    public EndGameResultUpdate(Result result){
        this.result=result;
    }

    @Override
    public void update(ClientModel clientModel) {
        clientModel.setGamePhase(GamePhase.ENDGAME);
    }

    @Override
    public String getMessage() {
        return null;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public void accept(ClientVisitor visitor, CLI client) {
        visitor.visit(this,client);
    }
}
