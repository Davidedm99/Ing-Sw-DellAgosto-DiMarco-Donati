package it.polimi.ingsw.network.client.ClientModel;

import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.network.Utilities;
import it.polimi.ingsw.network.client.CLI.enums.Resource;

import java.util.List;

public class ClientDeposit {
    private final List<Resource> resources;
    private final Integer id;

    public ClientDeposit(Integer id,List<Resource> resources){
        this.id=id;
        this.resources=resources;
    }

    public Integer getId() {
        return id;
    }

    public List<Resource> getResources() {
        return resources;
    }

}
