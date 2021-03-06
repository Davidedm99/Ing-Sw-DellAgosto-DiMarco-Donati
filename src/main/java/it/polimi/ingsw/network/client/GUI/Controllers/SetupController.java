package it.polimi.ingsw.network.client.GUI.Controllers;

import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.network.client.CLI.enums.Resource;
import it.polimi.ingsw.network.client.ClientModel.ClientDeposits;
import it.polimi.ingsw.network.client.ClientModel.Shelf;
import it.polimi.ingsw.network.messages.commands.ChooseBonusResourceCommand;
import it.polimi.ingsw.network.messages.commands.ChooseLeadersCommand;
import it.polimi.ingsw.network.messages.commands.PassCommand;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetupController extends ControllerGUI{
    List<String> chosenL = new ArrayList<>();
    List<Rectangle> selected = new ArrayList<>();
    Boolean replace = false;
    Integer id;
    ResourceType res;
    List<String> leaderCards;

    @Override
    public void reset(){
        chosenL=new ArrayList<>();
        selected=new ArrayList<>();
        replace=false;
        id=null;
        res=null;
        leaderCards=new ArrayList<>();
        res11.setImage(null);
        res21.setImage(null);
        res22.setImage(null);
        res31.setImage(null);
        res32.setImage(null);
        res33.setImage(null);
        l1Cover.setFill(Color.valueOf("#b0b3b5"));
        l2Cover.setFill(Color.valueOf("#b0b3b5"));
        l3Cover.setFill(Color.valueOf("#b0b3b5"));
        l4Cover.setFill(Color.valueOf("#b0b3b5"));
        selectedCoin.setOpacity(0);
        selectedServant.setOpacity(0);
        selectedShield.setOpacity(0);
        selectedStone.setOpacity(0);
        initializeElements();
    }
    @Override
    public void initializeElements(){
        leadersImageViews.add(leaderCard1);
        leadersImageViews.add(leaderCard2);
        leadersImageViews.add(leaderCard3);
        leadersImageViews.add(leaderCard4);

        List<ImageView> slot1=new ArrayList<>();
        slot1.add(res11);
        List<ImageView> slot2=new ArrayList<>();
        slot2.add(res21);
        slot2.add(res22);
        List<ImageView> slot3=new ArrayList<>();
        slot3.add(res31);
        slot3.add(res32);
        slot3.add(res33);
        warehouse.add(slot1);
        warehouse.add(slot2);
        warehouse.add(slot3);
    }

    public SetupController(){
    }
    List<ImageView> leadersImageViews=new ArrayList<>();
    List<List<ImageView>> warehouse=new ArrayList<>();

    @FXML private ImageView leaderCard1;
    @FXML private ImageView leaderCard2;
    @FXML private ImageView leaderCard3;
    @FXML private ImageView leaderCard4;
    @FXML private Rectangle l1Cover;
    @FXML private Rectangle l2Cover;
    @FXML private Rectangle l4Cover;
    @FXML private Rectangle l3Cover;
    @FXML private ImageView selectedServant;
    @FXML private ImageView selectedCoin;
    @FXML private ImageView selectedShield;
    @FXML private ImageView selectedStone;
    @FXML private ImageView res11;
    @FXML private ImageView res21;
    @FXML private ImageView res22;
    @FXML private ImageView res31;
    @FXML private ImageView res32;
    @FXML private ImageView res33;

    /**
     * shows the 4 random leader that the player has to choose from
     */
    public void updateLeader(){
        leaderCards = gui.getClientModel().getSetupPhaseLeaderCards();
        for(int i=0;i<leaderCards.size();i++)
            leadersImageViews.get(i).setImage(new Image("/images/leader_cards/"+leaderCards.get(i)+".png"));
    }

    /**
     * based on the clicked card, sets it to green and notes to a list which is clicked
     * @param mouseEvent click on the leader card
     */
    public void colorLeader(MouseEvent mouseEvent) {
        if (selected.size()==2) replace = true;
        else replace = false;
        if(mouseEvent.getSource().toString().equals("ImageView[id=leaderCard1, styleClass=image-view]")){
            checkList(l1Cover);
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=leaderCard2, styleClass=image-view]")){
            checkList(l2Cover);
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=leaderCard3, styleClass=image-view]")){
            checkList(l3Cover);
        }
        else {
            checkList(l4Cover);
        }
        colorSelected();
    }

    /**
     * adds the colored card to the list of the 2 chosen card
     * @param r the shape on scene builder that is under the Leader card
     */
    public void checkList(Rectangle r){
        if(selected.contains(r)) selected.remove(r);
        else if(replace && !selected.contains(r)){
            selected.remove(0);
            selected.add(r);
        }
        else selected.add(r);
    }

    /**
     * sets the rectangle to the relative color, chosen = green else grey
     */
    public void colorSelected(){
        if(selected.contains(l1Cover)) l1Cover.setFill(Color.GREEN);
        else l1Cover.setFill(Color.valueOf("#b0b3b5"));
        if(selected.contains(l2Cover)) l2Cover.setFill(Color.GREEN);
        else l2Cover.setFill(Color.valueOf("#b0b3b5"));
        if(selected.contains(l3Cover)) l3Cover.setFill(Color.GREEN);
        else l3Cover.setFill(Color.valueOf("#b0b3b5"));
        if(selected.contains(l4Cover)) l4Cover.setFill(Color.GREEN);
        else l4Cover.setFill(Color.valueOf("#b0b3b5"));
    }

    /**
     * the click on the chose leader button checks if the requirement are met and sends the command with the relative
     * chosen cards
     * @param mouseEvent click on the chose leader button
     * @throws IOException
     */
    public void chooseLeaders(MouseEvent mouseEvent) throws IOException {
        if(selected.size()<2)
            ComunicationController.showError(gui.getCurrentScene(), "You have to choose 2 leader cards!");
        else gui.send(new ChooseLeadersCommand(leaderList()));
    }

    /**
     * the click on pass ends the setup phase
     * @param mouseEvent click on the pass button
     */
    public void passTurn(MouseEvent mouseEvent) {
        gui.send(new PassCommand());
    }

    /**
     * function that returns the List of chosen leaders
     * @return
     */
    public List leaderList(){
        leaderCards = gui.getClientModel().getSetupPhaseLeaderCards();
        for(Rectangle r : selected){
            if(r.equals(l1Cover)) chosenL.add(leaderCards.get(0));
            else if(r.equals(l2Cover)) chosenL.add(leaderCards.get(1));
            else if(r.equals(l3Cover)) chosenL.add(leaderCards.get(2));
            else chosenL.add(leaderCards.get(3));
        }
        return chosenL;
    }

    /**
     * some players have to choose a bonus resource that has to be placed in the warehouse, this function updates
     * the warehouse with the chosen resource
     */
    public void updateWarehouse(){
        ClientDeposits clientDeposits=gui.getClientModel().getMyBoard().getDeposits();
        List<Shelf> shelves=clientDeposits.getShelves();
        for(int i=0;i<shelves.size();i++){
            if(i<3) {
                List<ImageView> slotImageViews = warehouse.get(i);
                for (int j = 0; j < slotImageViews.size(); j++) {
                    if (shelves.get(i).getSpaces()[j] == Resource.EMPTY)
                        slotImageViews.get(j).setImage(null);
                    else {
                        slotImageViews.get(j).setImage(new Image("/images/resources/" + shelves.get(i).getSpaces()[j].toString().toLowerCase() + ".png"));
                    }
                }
            }
        }
    }

    /**
     * the click on a warehouse shelf sends a command containing where to place that resource
     * @param mouseEvent click on the shelf
     */
    public void placeResource(MouseEvent mouseEvent) {
        Node node=(Node)mouseEvent.getSource();
        if(node.getId().equals("warehouse1")) id = 1;
        else if(node.getId().equals("warehouse2")) id = 2;
        else id = 3;
        selectedCoin.setOpacity(0);
        selectedShield.setOpacity(0);
        selectedServant.setOpacity(0);
        selectedStone.setOpacity(0);
        gui.send(new ChooseBonusResourceCommand(res, id));
    }

    /**
     * a click on a resource sets it to green, indicating that it has been selected
     * @param mouseEvent click on a resource
     */
    public void selectedRes(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().toString().equals("ImageView[id=selectedCoin, styleClass=image-view]")) {
            res = ResourceType.YELLOW;
            if (selectedCoin.getOpacity() == 100) selectedCoin.setOpacity(0);
            else {
                selectedCoin.setOpacity(100);
                selectedShield.setOpacity(0);
                selectedServant.setOpacity(0);
                selectedStone.setOpacity(0);
            }
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=selectedShield, styleClass=image-view]")) {
            res = ResourceType.BLUE;
            if (selectedShield.getOpacity() == 100) selectedShield.setOpacity(0);
            else {
                selectedCoin.setOpacity(0);
                selectedShield.setOpacity(100);
                selectedServant.setOpacity(0);
                selectedStone.setOpacity(0);
            }
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=selectedServant, styleClass=image-view]")) {
            res = ResourceType.VIOLET;
            if (selectedServant.getOpacity() == 100) selectedServant.setOpacity(0);
            else {
                selectedCoin.setOpacity(0);
                selectedShield.setOpacity(0);
                selectedServant.setOpacity(100);
                selectedStone.setOpacity(0);
            }
        }
        else {
            res = ResourceType.GREY;
            if (selectedStone.getOpacity() == 100) selectedStone.setOpacity(0);
            else {
                selectedCoin.setOpacity(0);
                selectedShield.setOpacity(0);
                selectedServant.setOpacity(0);
                selectedStone.setOpacity(100);
            }
        }
        mouseEvent.consume();
    }

}
