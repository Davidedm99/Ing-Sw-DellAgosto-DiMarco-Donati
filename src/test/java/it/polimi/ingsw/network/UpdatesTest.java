package it.polimi.ingsw.network;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.Result;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.enums.TurnPhase;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.network.client.CLI.enums.ClientPopeFavorState;
import it.polimi.ingsw.network.client.CLI.enums.Resource;
import it.polimi.ingsw.network.client.ClientModel.ClientBoard;
import it.polimi.ingsw.network.client.ClientModel.ClientDeposit;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientModel.Shelf;
import it.polimi.ingsw.network.messages.updates.PendingResourcesUpdate;
import it.polimi.ingsw.network.messages.updates.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UpdatesTest {
    ClientModel cm=new ClientModel();

    @BeforeEach
    public void setup(){
        cm.setCurrentNickname("rick");
        cm.setNickname("rick");
        cm.putBoard("rick",new ClientBoard());
        cm.putBoard("dona",new ClientBoard());
        cm.setPlayersOrder(new ArrayList<>());
        cm.getCardMatrix().setCards(Utilities.initializeCardMatrix(cm.getDevelopmentCards()));
        cm.getMarket().initializeMarbles();
    }
    @Test
    public void TestSlotUpdate(){
        //get of the top development card in the stack 2,3
        DevelopmentCard dc=cm.getCardMatrix().getCards()[2][3].get(cm.getCardMatrix().getCards()[2][3].size()-1);
        SlotUpdate su=new SlotUpdate(1,2,3);
        su.update(cm);
        assertEquals(dc,cm.getCurrentBoard().getSlots().get(1).get(cm.getCurrentBoard().getSlots().get(1).size()-1));
        assertEquals(3,cm.getCardMatrix().getCards()[2][3].size());
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

        Map<Resource,Integer> strongbox=new HashMap<>();

        DepositsUpdate whu=new DepositsUpdate(clientDeposits,strongbox, TurnPhase.ENDTURN);
        whu.update(cm);
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
    }
    @Test
    public void TestPlayLeader(){
        cm.getCurrentBoard().getLeadersInHand().add(cm.getLeaderCard("5L"));
        PlayLeaderUpdate plu=new PlayLeaderUpdate(0,"5L");
        assertEquals(cm.getLeaderCard("5L"),cm.getCurrentBoard().getLeadersInHand().get(0));
        plu.update(cm);
        assertEquals(cm.getLeaderCard("5L"),cm.getCurrentBoard().getLeadersInBoard().get(0));
    }
    @Test
    public void TestUnknownProductions(){
        cm.getCurrentBoard().addExtraProd(ResourceType.YELLOW);
        cm.getCurrentBoard().addExtraProd(ResourceType.GREY);
        //base prod input ? -> violet
        UnknownProductionUpdate upu1=new UnknownProductionUpdate(-1,ResourceType.VIOLET,'i');
        upu1.update(cm);
        UnknownProductionUpdate upu2=new UnknownProductionUpdate(1,ResourceType.BLUE,'o');
        upu2.update(cm);
        assertEquals(1,cm.getCurrentBoard().getBaseProduction().getInput().get(ResourceType.VIOLET));
        assertEquals(1,cm.getCurrentBoard().getExtraProductions().get(1).getOutput().get(ResourceType.BLUE));
    }
    @Test
    public void TestStrongBoxUpdate(){
        cm.getCurrentBoard().getDeposits().putResourceInStrongbox(Resource.SHIELD);
        cm.getCurrentBoard().getDeposits().putResourceInStrongbox(Resource.COIN);

        Map<Resource,Integer> strongbox=new HashMap<>();
        strongbox.put(Resource.SHIELD,2);
        strongbox.put(Resource.COIN,3);
        strongbox.put(Resource.FAITH,1);
        strongbox.put(Resource.SERVANT,0);
        strongbox.put(Resource.STONE,0);
        DepositsUpdate sbu=new DepositsUpdate(new ArrayList<>(),strongbox,TurnPhase.ENDTURN);
        assertEquals(1,cm.getCurrentBoard().getDeposits().getStrongbox().get(Resource.SHIELD));
        assertEquals(1,cm.getCurrentBoard().getDeposits().getStrongbox().get(Resource.COIN));
        sbu.update(cm);
        assertEquals(2,cm.getCurrentBoard().getDeposits().getStrongbox().get(Resource.SHIELD));
        assertEquals(3,cm.getCurrentBoard().getDeposits().getStrongbox().get(Resource.COIN));
    }

    @Test
    public void TestToggleDiscountUpdate(){
        cm.getCurrentBoard().addDiscount(ResourceType.YELLOW);
        cm.getCurrentBoard().addDiscount(ResourceType.GREY);
        ToggleDiscountUpdate tdu1=new ToggleDiscountUpdate(ResourceType.YELLOW);
        tdu1.update(cm);
        assertEquals(1,cm.getCurrentBoard().getActiveDiscounts().get(ResourceType.YELLOW));

        ToggleDiscountUpdate tdu2=new ToggleDiscountUpdate(ResourceType.GREY);
        tdu2.update(cm);
        assertEquals(1,cm.getCurrentBoard().getActiveDiscounts().get(ResourceType.GREY));

        tdu1.update(cm);
        assertEquals(1,cm.getCurrentBoard().getActiveDiscounts().get(ResourceType.GREY));
    }

    @Test
    public void TestDiscardResource(){
        DiscardResourceUpdate dru=new DiscardResourceUpdate();
        //multiplayer
        dru.update(cm);
        assertEquals(0,cm.getCurrentBoard().getFaithPath().getPosition());
        assertEquals(1,cm.getBoards().get("dona").getFaithPath().getPosition());

        //singleplayer
        cm.getCurrentBoard().getFaithPath().setLorenzoPosition(0);
        dru.update(cm);
        assertEquals(0,cm.getCurrentBoard().getFaithPath().getPosition());
        assertEquals(1,cm.getCurrentBoard().getFaithPath().getLorenzoPosition());
    }

    @Test
    public void TestDiscardLeader(){
        cm.getCurrentBoard().getLeadersInHand().add(cm.getLeaderCard("10L"));
        DiscardLeaderUpdate dlu=new DiscardLeaderUpdate(0);
        dlu.update(cm);
        assertEquals(0,cm.getCurrentBoard().getLeadersInHand().size());
        assertEquals(1,cm.getCurrentBoard().getFaithPath().getPosition());

    }
    @Test
    public void TestUpdateFaithPath(){
        Map<String, Integer> faithPaths=new HashMap<>();
        faithPaths.put("rick",8);
        faithPaths.put("dona",10);
        Map<String,Map<Integer, ClientPopeFavorState>> popefavors=new HashMap<>();
        Map<Integer, ClientPopeFavorState> pfmap1=new HashMap<>();
        Map<Integer, ClientPopeFavorState> pfmap2=new HashMap<>();

        pfmap1.put(1,ClientPopeFavorState.ACTIVE);
        pfmap1.put(2,ClientPopeFavorState.DISCARDED);
        pfmap1.put(3,ClientPopeFavorState.UNACTIVE);

        pfmap2.put(1,ClientPopeFavorState.DISCARDED);
        pfmap2.put(2,ClientPopeFavorState.ACTIVE);
        pfmap2.put(3,ClientPopeFavorState.DISCARDED);

        popefavors.put("rick",pfmap1);
        popefavors.put("dona",pfmap2);

        PopeFavorUpdate fpu=new PopeFavorUpdate(faithPaths,popefavors,null);
        fpu.update(cm);
    }
    @Test
    public void TestPendingWithWhite(){
        List<ResourceType> pending=new ArrayList<>();
        pending.add(ResourceType.YELLOW);
        pending.add(ResourceType.UNKNOWN);
        PendingResourcesUpdate prm =new PendingResourcesUpdate(pending);

    }
    @Test
    public void TestResultEndGame(){
        Result result=new Result();
        result.addToResults("riki",24,3);
        result.addToResults("dona",20,1);
        List<String> winners=new ArrayList<>();
        winners.add("riki");
        result.setWinner(winners);

        EndGameResultUpdate egru=new EndGameResultUpdate(result);
        egru.update(cm);
        assertEquals(cm.getGamePhase(), GamePhase.ENDGAME);

    }

    @Test
    public void TestReconnect(){
        ClientModel cmnew=new ClientModel();
        cmnew.setNickname("rick");

        Map<String, Integer> faithPaths=new HashMap<>();
        faithPaths.put("rick",8);
        faithPaths.put("dona",10);
        Map<String,Map<Integer, ClientPopeFavorState>> popefavors=new HashMap<>();
        Map<Integer, ClientPopeFavorState> pfmap1=new HashMap<>();
        Map<Integer, ClientPopeFavorState> pfmap2=new HashMap<>();

        pfmap1.put(1,ClientPopeFavorState.ACTIVE);
        pfmap1.put(2,ClientPopeFavorState.DISCARDED);
        pfmap1.put(3,ClientPopeFavorState.UNACTIVE);

        pfmap2.put(1,ClientPopeFavorState.DISCARDED);
        pfmap2.put(2,ClientPopeFavorState.ACTIVE);
        pfmap2.put(3,ClientPopeFavorState.DISCARDED);

        popefavors.put("rick",pfmap1);
        popefavors.put("dona",pfmap2);
        //---
        Map<String,Map<Resource,Integer>> strongboxes=new HashMap<>();
        Map<Resource,Integer> s1=new HashMap<>();
        s1.put(Resource.COIN,12);
        s1.put(Resource.SHIELD,420);
        s1.put(Resource.STONE,69);
        s1.put(Resource.SERVANT,3);

        Map<Resource,Integer> s2=new HashMap<>();
        s2.put(Resource.COIN,1);
        s2.put(Resource.SHIELD,2);
        s2.put(Resource.STONE,3);
        s2.put(Resource.SERVANT,4);

        strongboxes.put("rick",s1);
        strongboxes.put("dona",s2);
        //---
        Map<String, List<ClientDeposit>> warehouses=new HashMap<>();
        List<Resource> list1=new ArrayList<>();
        List<Resource> list2=new ArrayList<>();
        List<Resource> list3=new ArrayList<>();
        list1.add(Resource.COIN);
        list2.add(Resource.STONE);
        list3.add(Resource.SHIELD);
        list3.add(Resource.SHIELD);
        list3.add(Resource.SHIELD);

        ClientDeposit cd1=new ClientDeposit(1,list1);
        ClientDeposit cd2=new ClientDeposit(2,list2);
        ClientDeposit cd3=new ClientDeposit(3,list3);

        List<ClientDeposit> clientDeposits1=new ArrayList<>();
        clientDeposits1.add(cd1);
        clientDeposits1.add(cd2);
        clientDeposits1.add(cd3);

        List<Resource> list11=new ArrayList<>();
        List<Resource> list22=new ArrayList<>();
        List<Resource> list33=new ArrayList<>();
        list1.add(Resource.SERVANT);
        list2.add(Resource.STONE);
        list2.add(Resource.STONE);
        list3.add(Resource.COIN);
        list3.add(Resource.COIN);

        ClientDeposit cd11=new ClientDeposit(1,list11);
        ClientDeposit cd22=new ClientDeposit(2,list22);
        ClientDeposit cd33=new ClientDeposit(3,list33);

        List<ClientDeposit> clientDeposits2=new ArrayList<>();
        clientDeposits2.add(cd1);
        clientDeposits2.add(cd2);
        clientDeposits2.add(cd3);

        warehouses.put("rick",clientDeposits1);
        warehouses.put("dona",clientDeposits2);
        //---
        List<ResourceType> marbles=new ArrayList<>();
        marbles.add(ResourceType.BLUE);
        marbles.add(ResourceType.BLUE);
        marbles.add(ResourceType.BLUE);
        marbles.add(ResourceType.BLUE);
        marbles.add(ResourceType.RED);
        marbles.add(ResourceType.RED);
        marbles.add(ResourceType.RED);
        marbles.add(ResourceType.YELLOW);
        marbles.add(ResourceType.YELLOW);
        marbles.add(ResourceType.YELLOW);
        marbles.add(ResourceType.YELLOW);
        marbles.add(ResourceType.GREY);
        marbles.add(ResourceType.WHITE);
        //---
        Stack<String>[][] cardMatrix=new Stack[3][4];
        for(int r=0;r<3;r++){
            for(int c=0;c<4;c++){
                cardMatrix[r][c]=new Stack<>();
            }
        }
        //---
        List<String> playerOrder=new ArrayList<>();
        playerOrder.add("rick");
        playerOrder.add("dona");
        //---
        String currentNickname="dona";
        //---
        Map<String,Map<Integer, Stack<String>>> slots=new HashMap<>();
        Map<Integer, Stack<String>> map1=new HashMap<>();
        Stack<String> st1=new Stack<>();
        Stack<String> st2=new Stack<>();
        Stack<String> st3=new Stack<>();
        st1.push("3D");
        st2.push("15D");
        st2.push("20D");
        map1.put(1,st1);
        map1.put(2,st2);
        map1.put(3,st3);

        Map<Integer, Stack<String>> map2=new HashMap<>();
        Stack<String> st11=new Stack<>();
        Stack<String> st22=new Stack<>();
        Stack<String> st33=new Stack<>();
        st11.push("1D");
        st22.push("10D");
        st33.push("26D");
        map2.put(1,st11);
        map2.put(2,st22);
        map2.put(3,st33);

        slots.put("rick",map1);
        slots.put("dona",map2);
        //---
        Map<String,List<String>> allLeadersInBoard=new HashMap<>();

        List<String> listl1=new ArrayList<>();
        listl1.add("3L");
        List<String> listl2=new ArrayList<>();
        listl2.add("13L");
        listl2.add("5L");

        allLeadersInBoard.put("rick",listl1);
        allLeadersInBoard.put("dona",listl2);
        //---
        List<String> myLeadersInHand=new ArrayList<>();
        myLeadersInHand.add("10L");
        //--------------------------------------------
        ReconnectUpdate ru=new ReconnectUpdate(faithPaths,popefavors,null,strongboxes,warehouses,marbles,cardMatrix,playerOrder,currentNickname,slots,allLeadersInBoard,myLeadersInHand,GamePhase.ONGOING,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new HashMap<>(),new HashMap<>(),new HashMap<>(),TurnPhase.STARTTURN,new HashMap<>(),new ArrayList<>(),new ArrayList<>());
        ru.update(cmnew);

        cmnew.getBoards().get("rick").toggleDiscount(ResourceType.GREY);
    }
}
