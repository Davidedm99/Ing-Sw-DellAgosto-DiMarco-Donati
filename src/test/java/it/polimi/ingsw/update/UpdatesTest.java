package it.polimi.ingsw.update;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.network.Utilities;
import it.polimi.ingsw.network.client.ClientModel.CLI.Resource;
import it.polimi.ingsw.network.client.ClientModel.ClientBoard;
import it.polimi.ingsw.network.client.ClientModel.ClientDeposit;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientModel.Shelf;
import it.polimi.ingsw.network.messages.updates.SlotUpdate;
import it.polimi.ingsw.network.messages.updates.ToggleProductionUpdate;
import it.polimi.ingsw.network.messages.updates.WarehouseUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdatesTest {
    ClientModel cm=new ClientModel();

    @BeforeEach
    public void setup(){
        cm.setCurrentNickname("rick");
        cm.setNickname("rick");
        cm.putBoard("rick",new ClientBoard());
        cm.setPlayersOrder(new ArrayList<>());
        cm.getCardMatrix().setDCard(Utilities.initializeCardMatrix(cm.getDevelopmentCards()));
        cm.getMarket().initializeMarbles();
        System.out.println(cm);
    }
    @Test
    public void TestSlotUpdate(){
        //get of the top development card in the stack 2,3
        DevelopmentCard dc=cm.getCardMatrix().getdCard()[2][3].get(cm.getCardMatrix().getdCard()[2][3].size()-1);
        SlotUpdate su=new SlotUpdate(1,2,3);
        su.update(cm);
        assertEquals(dc,cm.getCurrentBoard().getSlots().get(1).get(cm.getCurrentBoard().getSlots().get(1).size()-1));
        assertEquals(3,cm.getCardMatrix().getdCard()[2][3].size());
        System.out.println(cm);
    }
    @Test
    public void TestWarehouseUpdate(){
        cm.getCurrentBoard().getDeposits().addShelf(new Shelf(2,4));
        List<Resource> list1=new ArrayList<>();
        List<Resource> list2=new ArrayList<>();
        List<Resource> list3=new ArrayList<>();
        List<Resource> list4=new ArrayList<>();
        list1.add(Resource.COIN);
        list2.add(Resource.STONE);
        list3.add(Resource.SHIELD);
        list3.add(Resource.SHIELD);
        list3.add(Resource.SHIELD);
        list4.add(Resource.SERVANT);

        ClientDeposit cd1=new ClientDeposit(1,list1);
        ClientDeposit cd2=new ClientDeposit(2,list2);
        ClientDeposit cd3=new ClientDeposit(3,list3);
        ClientDeposit cd4=new ClientDeposit(4,list4);

        List<ClientDeposit> clientDeposits=new ArrayList<>();
        clientDeposits.add(cd1);
        clientDeposits.add(cd2);
        clientDeposits.add(cd3);
        clientDeposits.add(cd4);

        WarehouseUpdate whu=new WarehouseUpdate(clientDeposits);
        whu.update(cm);
        System.out.println(cm);
    }
    @Test
    public void TestToggleProductionUpdate() throws IllegalResourceException {
        Production p1=new Production();
        p1.addInput(ResourceType.UNKNOWN,1);
        p1.addInput(ResourceType.GREY,1);
        p1.addOutput(ResourceType.RED,2);
        p1.addOutput(ResourceType.YELLOW,1);

        Production p2=new Production();
        p2.addInput(ResourceType.BLUE,3);
        p2.addOutput(ResourceType.UNKNOWN,2);

        List<Production> activeProductions=new ArrayList<>();
        activeProductions.add(p1);
        activeProductions.add(p2);

        ToggleProductionUpdate tpu=new ToggleProductionUpdate(activeProductions);
        tpu.update(cm);
        System.out.println("Active Productions: ");
        for(Production p : cm.getCurrentBoard().getActiveProductions()){
            System.out.print("["+Utilities.stringify(p)+"]");
        }
    }
}
