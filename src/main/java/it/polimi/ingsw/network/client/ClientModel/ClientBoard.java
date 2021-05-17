package it.polimi.ingsw.network.client.ClientModel;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.network.Utilities;
import it.polimi.ingsw.network.client.ClientModel.CLI.Color;
import it.polimi.ingsw.network.client.ClientModel.CLI.Resource;

import java.util.*;

public class ClientBoard {
    private ClientFaithPath faithPath;
    private ClientDeposits deposits;
    private Map<Integer, Stack<DevelopmentCard>> slots=new HashMap<>();
    private Integer totalSlotPoints;
    private Integer totalCardsBought;
    private List<LeaderCard> leadersInHand=new ArrayList<>();
    private List<LeaderCard> leadersInBoard=new ArrayList<>();

    private List<Production> activeProductions=new ArrayList<>();
    private Production baseProduction;
    private List<Production> extraProductions=new ArrayList<>();
    private List<ResourceDiscount> discounts=new ArrayList<>();

    public void addExtraProd(ResourceType res){
        Production p=new Production();
        try {
            p.addOutput(ResourceType.UNKNOWN,1);
            p.addInput(res,1);
            p.addOutput(ResourceType.RED,1);
        } catch (IllegalResourceException e) {
            e.printStackTrace();
        }
        extraProductions.add(p);
    }
    public void toggleDiscount(ResourceType res){
        for(ResourceDiscount rd :discounts){
            if(rd.getRes()==res) rd.toggle();
        }
    }

    public void addDiscount(ResourceType res){
        ResourceDiscount rd=new ResourceDiscount(res);
        discounts.add(rd);
    }
    public List<ResourceDiscount> getActiveDiscounts(){
        List<ResourceDiscount> list=new ArrayList<>();
        for(ResourceDiscount rd : discounts) {
            if (rd.isActivated()) list.add(rd);
        }
        return list;
    }
    public List<Production> getExtraProductions() {
        return extraProductions;
    }

    public ClientBoard(){
        deposits=new ClientDeposits();
        faithPath=new ClientFaithPath();
        slots.put(1,new Stack<>());
        slots.put(2,new Stack<>());
        slots.put(3,new Stack<>());
        totalSlotPoints=0;
        totalCardsBought=0;
        baseProduction=new Production();
        try {
            baseProduction.addInput(ResourceType.UNKNOWN,2);
            baseProduction.addOutput(ResourceType.UNKNOWN,1);
        } catch (IllegalResourceException e) {
            e.printStackTrace();
        }

    }

    public Production getBaseProduction() {
        return baseProduction;
    }

    public void setActiveProductions(List<Production> activeProductions) {
        this.activeProductions = activeProductions;
    }

    public void setLeadersInHand(List<LeaderCard> leadersInHand) {
        this.leadersInHand = leadersInHand;
    }

    public List<Production> getActiveProductions() {
        return activeProductions;
    }

    public List<LeaderCard> getLeadersInBoard() {
        return leadersInBoard;
    }

    public List<LeaderCard> getLeadersInHand() {
        return leadersInHand;
    }

    public String toString(){
        String stringBoard= faithPath.toString();
        stringBoard+=deposits.toString();
        stringBoard+=stringifySlots();
        stringBoard+=stringifyLeaders();
        stringBoard+=stringifyBaseProduction();
        return stringBoard;
    }
    public void push(Integer slot,DevelopmentCard d){
        if(slot!=null && d!=null && slots.get(slot)!=null){
            slots.get(slot).push(d);
            totalSlotPoints+=d.getPoints();
            totalCardsBought++;
        }
    }
    public DevelopmentCard pop(Integer slot){
        if(slot!=null && slots.get(slot)!=null && slots.get(slot).size()>0){
            return slots.get(slot).pop();
        }
        return null;
    }
    public String stringifyActiveDiscounts(){
        StringBuilder sb=new StringBuilder();
        sb.append(Color.ANSI_PURPLE.escape()+"Active discounts: "+Color.RESET);
        for(ResourceDiscount rd: discounts){
            if(rd.isActivated()) sb.append("-").append(rd.getQuantity()).append(Utilities.resourceTypeToResource(rd.getRes()).label).append(" ");
        }
        return sb.toString();
    }
    public String stringifyProductions(){
        StringBuilder sb=new StringBuilder();
        sb.append(stringifyBaseProduction());
        sb.append("═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
        sb.append(Color.ANSI_PURPLE.escape()).append("EXTRA PRODUCTION: "+Color.RESET);
        for(Production p : extraProductions){
            sb.append("[").append(Utilities.stringify(p)).append("]");
        }
        sb.append("\n");
        return sb.toString();
    }
    public String stringifyBaseProduction(){
        StringBuilder sb=new StringBuilder();
        sb.append("\n═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
        sb.append(Color.ANSI_PURPLE.escape()).append("BASE PRODUCTION: ").append(Color.RESET).append(Utilities.stringify(baseProduction));
        sb.append("\n");
        return sb.toString();
    }
    public String stringifyLeaders(){
        StringBuilder sb=new StringBuilder();
        sb.append("═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
        sb.append(Color.ANSI_PURPLE.escape()+"LEADERS IN HAND: "+Color.RESET);
        for(LeaderCard ld : leadersInHand){
            sb.append(Utilities.stringify(ld));
            sb.append("\n                 ");
        }
        sb.append("\n");
        sb.append(Color.ANSI_PURPLE.escape()+"LEADERS IN BOARD: "+Color.RESET);
        for(LeaderCard ld : leadersInBoard){
            sb.append(Utilities.stringify(ld));
            sb.append("\n                  ");
        }
        sb.append("\n");
        return sb.toString();
    }

    public Map<Integer, Stack<DevelopmentCard>> getSlots() {
        return slots;
    }

    public String recapSlots(){
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<4;i++){
            sb.append(Color.ANSI_GREEN.escape()+"LEVEL "+i+": "+Color.RESET);
            for(int j=1;j<4;j++){
                for(DevelopmentCard d : slots.get(j)){
                    if(d.getLevel()==i)
                        sb.append(Utilities.modelColorToClientColor(d.getColor()).escape()+"■"+Color.RESET);
                }
            }
            sb.append(" | ");
        }
        sb.append("\n");
        return sb.toString();
    }
    public String stringifySlots(){
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<4;i++){
            sb.append(Color.ANSI_PURPLE.escape()+"SLOT "+i+": "+Color.RESET);
            if(slots.get(i)!=null && slots.get(i).size()>0){
                sb.append(Utilities.stringify(slots.get(i).get(slots.get(i).size()-1)));
            }else sb.append("[ ]");
            sb.append("\n");
        }
        sb.append(Color.ANSI_PURPLE.escape()+"TOTAL SLOT POINTS: "+Color.ANSI_YELLOW.escape()+totalSlotPoints+Color.RESET+" | "+
                Color.ANSI_PURPLE.escape()+"TOTAL CARDS BOUGHT: "+Color.RESET+totalCardsBought+"\n");
        sb.append(Color.ANSI_PURPLE.escape()+"RECAP: "+Color.RESET+recapSlots());
        return sb.toString();
    }
    public ClientDeposits getDeposits() {
        return deposits;
    }

    public ClientFaithPath getFaithPath() {
        return faithPath;
    }
}
