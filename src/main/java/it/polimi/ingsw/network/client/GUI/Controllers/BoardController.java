package it.polimi.ingsw.network.client.GUI.Controllers;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.network.client.CLI.enums.Resource;
import it.polimi.ingsw.network.client.ClientModel.*;
import it.polimi.ingsw.network.client.CLI.enums.ClientPopeFavorState;
import it.polimi.ingsw.network.client.CLI.enums.Resource;
import it.polimi.ingsw.network.client.GUI.GUI;
import it.polimi.ingsw.network.messages.commands.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class BoardController extends ControllerGUI {
    List<String> leaderID = new ArrayList<>();
    Boolean clickedBox = false;
    Boolean clickedMatrix = false;
    Boolean clickedMarket = false;
    ResourceType movedRes;
    Integer row;
    Integer column;
    Character line;
    Integer pos;
    Boolean setup = true;
    Integer lCard;
    Image img;

    public BoardController(){

    }
    @Override
    public void initializeElements(){
        List<ImageView> slot1=new ArrayList<>();
        slot1.add(resSlot1);
        List<ImageView> slot2=new ArrayList<>();
        slot2.add(resSlot21);
        slot2.add(resSlot22);
        List<ImageView> slot3=new ArrayList<>();
        slot3.add(resSlot31);
        slot3.add(resSlot32);
        slot3.add(resSlot33);
        warehouse.add(slot1);
        warehouse.add(slot2);
        warehouse.add(slot3);

        faithPath.add(faithPath0);
        faithPath.add(faithPath1);
        faithPath.add(faithPath2);
        faithPath.add(faithPath3);
        faithPath.add(faithPath4);
        faithPath.add(faithPath5);
        faithPath.add(faithPath6);
        faithPath.add(faithPath7);
        faithPath.add(faithPath8);
        faithPath.add(faithPath9);
        faithPath.add(faithPath10);
        faithPath.add(faithPath11);
        faithPath.add(faithPath12);
        faithPath.add(faithPath13);
        faithPath.add(faithPath14);
        faithPath.add(faithPath15);
        faithPath.add(faithPath16);
        faithPath.add(faithPath17);
        faithPath.add(faithPath18);
        faithPath.add(faithPath19);
        faithPath.add(faithPath20);
        faithPath.add(faithPath21);
        faithPath.add(faithPath22);
        faithPath.add(faithPath23);
        faithPath.add(faithPath24);

        blackCross.add(blackCross0);
        blackCross.add(blackCross1);
        blackCross.add(blackCross2);
        blackCross.add(blackCross3);
        blackCross.add(blackCross4);
        blackCross.add(blackCross5);
        blackCross.add(blackCross6);
        blackCross.add(blackCross7);
        blackCross.add(blackCross8);
        blackCross.add(blackCross9);
        blackCross.add(blackCross10);
        blackCross.add(blackCross11);
        blackCross.add(blackCross12);
        blackCross.add(blackCross13);
        blackCross.add(blackCross14);
        blackCross.add(blackCross15);
        blackCross.add(blackCross16);
        blackCross.add(blackCross17);
        blackCross.add(blackCross18);
        blackCross.add(blackCross19);
        blackCross.add(blackCross20);
        blackCross.add(blackCross21);
        blackCross.add(blackCross22);
        blackCross.add(blackCross23);
        blackCross.add(blackCross24);

        marbles.add(marble00);
        marbles.add(marble01);
        marbles.add(marble02);
        marbles.add(marble03);
        marbles.add(marble10);
        marbles.add(marble11);
        marbles.add(marble12);
        marbles.add(marble13);
        marbles.add(marble20);
        marbles.add(marble21);
        marbles.add(marble22);
        marbles.add(marble23);
        marbles.add(pendingMarble);

        imageViewMatrix[0][0]=green1;
        imageViewMatrix[1][0]=green2;
        imageViewMatrix[2][0]=green3;
        imageViewMatrix[0][1]=blue1;
        imageViewMatrix[1][1]=blue2;
        imageViewMatrix[2][1]=blue3;
        imageViewMatrix[0][2]=yellow1;
        imageViewMatrix[1][2]=yellow2;
        imageViewMatrix[2][2]=yellow3;
        imageViewMatrix[0][3]=purple1;
        imageViewMatrix[1][3]=purple2;
        imageViewMatrix[2][3]=purple3;

        popes.add(pope1);
        popes.add(pope2);
        popes.add(pope3);

    }
    List<List<ImageView>> warehouse=new ArrayList<>();
    List<ImageView> faithPath=new ArrayList<>();
    List<ImageView> blackCross=new ArrayList<>();
    List<ImageView> marbles=new ArrayList<>();
    List<ImageView> popes=new ArrayList<>();
    ImageView[][] imageViewMatrix=new ImageView[3][4];

    @FXML private AnchorPane hiddenPanel;
    @FXML private Label pickedCoins;
    @FXML private Label pickedServants;
    @FXML private Label pickedShields;
    @FXML private Label pickedStones;
    @FXML private Label strongboxCoins;
    @FXML private Label strongboxServants;
    @FXML private Label strongboxShields;
    @FXML private Label strongboxStones;
    @FXML private Rectangle leader1;
    @FXML private Rectangle leader2;
    @FXML private ImageView leaderCard1;
    @FXML private ImageView leaderCard2;
    @FXML private AnchorPane hiddenCardMatrix;
    @FXML private AnchorPane hiddenResMarket;
    @FXML private ImageView resSlot1;
    @FXML private ImageView resSlot21;
    @FXML private ImageView resSlot22;
    @FXML private ImageView resSlot31;
    @FXML private ImageView resSlot32;
    @FXML private ImageView resSlot33;
    @FXML private ImageView pendingCoin;
    @FXML private ImageView pendingServant;
    @FXML private ImageView pendingShield;
    @FXML private ImageView pendingStone;
    @FXML private ImageView green1;
    @FXML private ImageView green2;
    @FXML private ImageView green3;
    @FXML private ImageView blue1;
    @FXML private ImageView blue2;
    @FXML private ImageView blue3;
    @FXML private ImageView yellow1;
    @FXML private ImageView yellow2;
    @FXML private ImageView yellow3;
    @FXML private ImageView purple1;
    @FXML private ImageView purple2;
    @FXML private ImageView purple3;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private ImageView disc1;
    @FXML private ImageView disc2;
    @FXML private ImageView disc3;
    @FXML private ImageView marble00;
    @FXML private ImageView marble01;
    @FXML private ImageView marble02;
    @FXML private ImageView marble03;
    @FXML private ImageView marble10;
    @FXML private ImageView marble11;
    @FXML private ImageView marble12;
    @FXML private ImageView marble13;
    @FXML private ImageView marble20;
    @FXML private ImageView marble21;
    @FXML private ImageView marble22;
    @FXML private ImageView marble23;
    @FXML private ImageView pendingMarble;
    @FXML private ImageView p1Board;
    @FXML private ImageView p2Board;
    @FXML private ImageView p3Board;
    @FXML private ImageView active1;
    @FXML private ImageView active2;
    @FXML private ImageView active3;
    @FXML private ImageView faithPath0;
    @FXML private ImageView faithPath1;
    @FXML private ImageView faithPath2;
    @FXML private ImageView faithPath3;
    @FXML private ImageView faithPath4;
    @FXML private ImageView faithPath5;
    @FXML private ImageView faithPath6;
    @FXML private ImageView faithPath7;
    @FXML private ImageView faithPath8;
    @FXML private ImageView faithPath9;
    @FXML private ImageView faithPath10;
    @FXML private ImageView faithPath11;
    @FXML private ImageView faithPath12;
    @FXML private ImageView faithPath13;
    @FXML private ImageView faithPath14;
    @FXML private ImageView faithPath15;
    @FXML private ImageView faithPath16;
    @FXML private ImageView faithPath17;
    @FXML private ImageView faithPath18;
    @FXML private ImageView faithPath19;
    @FXML private ImageView faithPath20;
    @FXML private ImageView faithPath21;
    @FXML private ImageView faithPath22;
    @FXML private ImageView faithPath23;
    @FXML private ImageView faithPath24;
    @FXML private ImageView blackCross0;
    @FXML private ImageView blackCross1;
    @FXML private ImageView blackCross2;
    @FXML private ImageView blackCross3;
    @FXML private ImageView blackCross4;
    @FXML private ImageView blackCross5;
    @FXML private ImageView blackCross6;
    @FXML private ImageView blackCross7;
    @FXML private ImageView blackCross8;
    @FXML private ImageView blackCross9;
    @FXML private ImageView blackCross10;
    @FXML private ImageView blackCross11;
    @FXML private ImageView blackCross12;
    @FXML private ImageView blackCross13;
    @FXML private ImageView blackCross14;
    @FXML private ImageView blackCross15;
    @FXML private ImageView blackCross16;
    @FXML private ImageView blackCross17;
    @FXML private ImageView blackCross18;
    @FXML private ImageView blackCross19;
    @FXML private ImageView blackCross20;
    @FXML private ImageView blackCross21;
    @FXML private ImageView blackCross22;
    @FXML private ImageView blackCross23;
    @FXML private ImageView blackCross24;
    @FXML private ImageView pope1;
    @FXML private ImageView pope2;
    @FXML private ImageView pope3;
    @FXML private ImageView unknownInput1;
    @FXML private ImageView unknownInput2;
    @FXML private ImageView unknownOutput;

    /**
     * hides and shows the strongbox Panel
     */
    public void openStrongbox() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.7), hiddenPanel);
        if(!clickedBox){
            tt.setFromX(0);
            tt.setToX(273);
            clickedBox = true;
        }
        else{
            tt.setFromX(273);
            tt.setToX(0);
            clickedBox = false;
        }
        tt.play();
    }

    /**
     * hides and shows the Card Market Panel
     */
    public void showCardMarket() {
        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(0.5), hiddenCardMatrix);
        if(!clickedMatrix){
            if(clickedMarket) showResourceMarket();
            tt2.setFromX(0);
            tt2.setToX(-653);
            clickedMatrix = true;
        }
        else{
            tt2.setFromX(-653);
            tt2.setToX(0);
            clickedMatrix = false;
        }
        tt2.play();
    }

    /**
     * hides and shows the Resource Market Panel
     */
    public void showResourceMarket() {
        TranslateTransition tt3 = new TranslateTransition(Duration.seconds(0.5), hiddenResMarket);
        if(!clickedMarket){
            if(clickedMatrix) showCardMarket();
            tt3.setFromY(0);
            tt3.setToY(670);
            clickedMarket = true;
        }
        else{
            tt3.setFromY(670);
            tt3.setToY(0);
            clickedMarket = false;
        }
        tt3.play();
    }

    /**
     * useful to manage the open/closed panels
     * @param mouseEvent on left mouse click calls the function that opens/closes the Resource Market
     */
    public void manageResMarket(MouseEvent mouseEvent){
        showResourceMarket();
    }

    /**
     * useful to manage the open/closed panels
     * @param mouseEvent on left mouse click calls the function that opens/closes the Card Market
     */
    public void manageCardMarket(MouseEvent mouseEvent){
        showCardMarket();
    }
    /**
     * function that updates every resource in the strongbox with it's value
     */
    public void updateStrongbox() {
        Map<Resource, Integer> strongbox=gui.getClientModel().getMyBoard().getDeposits().getStrongbox();
        strongboxCoins.setText(strongbox.get(Resource.COIN).toString());
        strongboxServants.setText(strongbox.get(Resource.SERVANT).toString());
        strongboxShields.setText(strongbox.get(Resource.SHIELD).toString());
        strongboxStones.setText(strongbox.get(Resource.STONE).toString());
    }

    /**
     * function that updates the value of the resource currently picked
     */
    public void updatePickedRes(){
        pickedCoins.setText(gui.getClientModel().getCurrentBoard().getDeposits().getHandResources().get(Resource.COIN).toString());
        pickedServants.setText(gui.getClientModel().getCurrentBoard().getDeposits().getHandResources().get(Resource.SERVANT).toString());
        pickedShields.setText(gui.getClientModel().getCurrentBoard().getDeposits().getHandResources().get(Resource.SHIELD).toString());
        pickedStones.setText(gui.getClientModel().getCurrentBoard().getDeposits().getHandResources().get(Resource.STONE).toString());
    }

    public void updateLCards(){
        if(gui.getClientModel().getMyBoard().getLeadersInHand().size()==2){
            leaderCard1.setImage(new Image("/images/leader_cards/" + gui.getClientModel().getMyBoard().getLeadersInHand().get(0).getName() + ".png"));
            leaderCard2.setImage(new Image("/images/leader_cards/" + gui.getClientModel().getMyBoard().getLeadersInHand().get(1).getName() + ".png"));
        }
        Map<Integer,String> disc=gui.getClientModel().getMyBoard().getDiscardedCards();
        Map<Integer,String> played=gui.getClientModel().getMyBoard().getPlayedCards();
        if(disc.get(0)!=null) {
            leaderCard1.setImage(new Image("/images/back LCard.png"));
            leader1.setFill(Color.RED);
        }
        if(disc.get(1)!=null){
            leaderCard2.setImage(new Image("/images/back LCard.png"));
            leader2.setFill(Color.RED);
        }
        if(played.get(0)!=null){
            leader1.setFill(Color.GREEN);
        }
        if(played.get(1)!=null){
            leader2.setFill(Color.GREEN);
        }

    }
    /**
     *updates the LCard zone, filling the rectangle with green if active, setting the back if discarded
     */
    public void updateLCards2() {
        if (gui.getClientModel().getGamePhase().equals(GamePhase.SETUP)) {
            leaderID.add(gui.getClientModel().getMyBoard().getLeadersInHand().get(0).getName());
            leaderID.add(gui.getClientModel().getMyBoard().getLeadersInHand().get(1).getName());
            leaderCard1.setImage(new Image("/images/leader_cards/" + gui.getClientModel().getMyBoard().getLeadersInHand().get(0).getName() + ".png"));
            leaderCard2.setImage(new Image("/images/leader_cards/" + gui.getClientModel().getMyBoard().getLeadersInHand().get(1).getName() + ".png"));
            setup = false;

        }
        //board vuota, 1 o 2 scartate
        if(gui.getClientModel().getMyBoard().getLeadersInBoard().isEmpty() && gui.getClientModel().getMyBoard().getLeadersInHand().size() < 2){
            if(gui.getClientModel().getMyBoard().getLeadersInHand().size() == 0){
                leaderCard1.setImage(new Image("/images/back LCard.png"));
                leaderCard2.setImage(new Image("/images/back LCard.png"));
            }
            else if(gui.getClientModel().getMyBoard().getLeadersInHand().contains(leaderID.get(0))) leaderCard2.setImage(new Image("/images/back LCard.png"));
            else leaderCard1.setImage(new Image("/images/back LCard.png"));
        }
        //almeno 1 in board, l'altra in mano o scartata
        else if(gui.getClientModel().getMyBoard().getLeadersInBoard().size() == 1){
            //carta in gioco è la prima e non ci sono carte in mano (seconda scartata)
            if(gui.getClientModel().getMyBoard().getLeadersInBoard().get(0).getName().equals(leaderID.get(0))) {
                leader1.setFill(Color.GREEN);
                if(gui.getClientModel().getMyBoard().getLeadersInHand().size() == 0)
                    leaderCard2.setImage(new Image("/images/back LCard.png"));
            }
            //carta in gioco è la seconda e non ci sono carte in mano (prima scartata)
            else {
                leader2.setFill(Color.GREEN);
                if(gui.getClientModel().getMyBoard().getLeadersInHand().size() == 0)
                    leaderCard1.setImage(new Image("/images/back LCard.png"));
            }
        }
        //entrambe in board
        else if(gui.getClientModel().getMyBoard().getLeadersInBoard().size() == 2){
            leader1.setFill(Color.GREEN);
            leader1.setFill(Color.GREEN);
        }
        else return;
    }

    /**
     * function that will update the result of the drag & drop event in the warehouse
     */
    public void updateWarehouse(){
        ClientDeposits clientDeposits=gui.getClientModel().getMyBoard().getDeposits();
        List<Shelf> shelves=clientDeposits.getShelves();
        for(int i=0;i<shelves.size();i++){
            List<ImageView> slotImageViews=warehouse.get(i);
            for(int j=0;j<slotImageViews.size();j++){
                if(shelves.get(i).getSpaces()[j]==Resource.EMPTY)
                    slotImageViews.get(j).setImage(null);
                else{
                    slotImageViews.get(j).setImage(new Image("/images/resources/" +shelves.get(i).getSpaces()[j].toString().toLowerCase()+ ".png"));
                }
            }
        }
    }

    /**
     * visualize the board passed like parameter
     * @param nickname the username passed
     */
    public void updateBoard(String nickname){
        gui.changeScene(GUI.OTHERBOARD);
        OtherBoardController obc=(OtherBoardController)gui.getBuildedControllers().get(GUI.OTHERBOARD);
        obc.visualizeBoard(nickname);
    }

    /**
     * shows which resources you got from the resource market
     */
    public void updatePending(List<Resource> pending){}

    /**
     * based on the client card this function shows the top card of each pile, obscures the card if pile's empty
     */
    public void updateCardMatrix(){
        Stack<DevelopmentCard>[][] cards = gui.getClientModel().getCardMatrix().getCards();
        for(int r=0;r<3;r++){
            for(int c=0;c<4;c++){
                if(cards[r][c].size()==0) imageViewMatrix[r][c].setOpacity(0);
                else imageViewMatrix[r][c].setImage(new Image("/images/development_cards/"+cards[r][c].peek().getName()+".png"));
            }
        }
    }

    /**
     * based on the marble's list, the function shows the marble's market
     */
    public void updateResMarket(){
        List<ResourceType> marbles = gui.getClientModel().getMarket().getMarbles();
        for(int i=0;i<marbles.size();i++)
            this.marbles.get(i).setImage(new Image("/images/marbles/"+setColor(marbles.get(i))+".png"));
    }

    /**
     * this function is needed to find the correct resource path based on the resource
     * @param resourceType this helps to find which resource is in a precise list place
     * @return the string of the resource in the passed parameter
     */
    public String setColor(ResourceType resourceType){
        if(resourceType.equals(ResourceType.RED)) return "red";
        else if(resourceType.equals(ResourceType.BLUE)) return "blue";
        else if(resourceType.equals(ResourceType.YELLOW)) return "yellow";
        else if(resourceType.equals(ResourceType.VIOLET)) return "purple";
        else if(resourceType.equals(ResourceType.GREY)) return  "grey";
        else return "white";
    }

    /**
     * updates the red cross image based on the value of the player's faithpath position
     */
    public void updateFaithPath(){
        for(ImageView i : faithPath) i.setOpacity(0);
        int position=gui.getClientModel().getMyBoard().getFaithPath().getPosition();
        faithPath.get(position).setOpacity(100);
    }

    /**
     * updates the black cross image based on the value of Lorenzo's position
     */
    public void updateBlackCross(){
        for(ImageView i : blackCross)i.setOpacity(0);
        Integer position=gui.getClientModel().getMyBoard().getFaithPath().getLorenzoPosition();
        if(position!=null) blackCross.get(position).setOpacity(100);
    }

    /**
     * based on the value got by the pope favor map's Client model load a new image if active or remove image if discarded
     */
    //anche se non ci sarebbe bisogno di controllare i pope favor dopo averli cambiati
    public void updatePopeFavor(){
        for(int i=0;i<popes.size();i++) {
            if (gui.getClientModel().getMyBoard().getFaithPath().getPopeFavor().get(i+1).equals(ClientPopeFavorState.ACTIVE))
                popes.get(i).setImage(new Image("/images/faithpath/pope_favor1_front.png"));
            else if (gui.getClientModel().getMyBoard().getFaithPath().getPopeFavor().get(i+1).equals(ClientPopeFavorState.DISCARDED))
                popes.get(i).setOpacity(0);
        }
    }

//---------------------------------out messages--------------------------------------------------------------

    /**
     * the click event on a Resource image sends a message of a picked resource
     * @param mouseEvent left mouse click on a specific resource
     */
    public void clickedRes(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().toString().equals("ImageView[id=pickStone, styleClass=image-view]")) {
            gui.send(new StrongboxPickUpCommand(ResourceType.GREY));
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=pickServant, styleClass=image-view]")) {
            gui.send(new StrongboxPickUpCommand(ResourceType.VIOLET));
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=pickShield, styleClass=image-view]")) {
            gui.send(new StrongboxPickUpCommand(ResourceType.BLUE));
        }
        else {
            gui.send(new StrongboxPickUpCommand(ResourceType.YELLOW));
        }
    }

    /**
     * the button's action event sends a message to start all the toggled productions
     * @param event left mouse click on the button
     */
    public void produce(ActionEvent event) {
        gui.send(new ActivateProductionsCommand());
    }

    /**
     * the play button sends a message to play the relative sent card
     * @param event on click calls the play leader button
     */
    public void playLeader(ActionEvent event) {
        Integer lCard;
        Node node=(Node)event.getSource();
        Integer played=gui.getClientModel().getMyBoard().getPlayedCards().size();
        if(node.getId().equals("playL1")) lCard = 0;
        else lCard = 1;

        if(gui.getClientModel().getMyBoard().getPlayedCards().get(lCard)!=null) {
            ComunicationController.showError(gui.getCurrentScene(), "Already played!");
            return;
        }
        if(played==1 && lCard==1 && node.getId().equals("playL2"))
            lCard=0;
        gui.send(new PlayLeaderCommand(lCard));
    }

    /**
     * the discard button sends a message to discard the relative sent card
     * @param event on click calls the discard leader button
     */
    public void discardLeader(ActionEvent event) {
        Integer lCard;
        Node node=(Node)event.getSource();
        Integer discarded=gui.getClientModel().getMyBoard().getDiscardedCards().size();
        if(node.getId().equals("discardL1")) lCard = 0;
        else lCard = 1;

        if(gui.getClientModel().getMyBoard().getDiscardedCards().get(lCard)!=null) {
            ComunicationController.showError(gui.getCurrentScene(), "Already discarded!");
            return;
        }

        if(discarded==1 && lCard==1 && node.getId().equals("discardL2"))
            lCard=0;
        gui.send(new DiscardLeaderCommand(lCard));
    }

    /**
     * this method allows the warehouse to accept an image dropped in
     * @param dragEvent detect the drop event
     */
    public void acceptDrag(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage()){
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    /**
     * based on the drag event target, which is an empty Imageview that will be updated in future, i'll assign a value to a
     * integer slot. This slot identifies the warehouse position of the dropped pending resource.
     * @param dragEvent this event identifies the end of a drag & drop event
     */
    public void placeWarehouse(DragEvent dragEvent){
//        draggedRes = dragEvent.getDragboard().getImage();
        Integer slot;
        if(dragEvent.getTarget().toString().equals("ImageView[id=resSlot1, styleClass=image-view]")) slot = 1;
        else if(dragEvent.getTarget().toString().equals("ImageView[id=resSlot21, styleClass=image-view]")
                || dragEvent.getTarget().toString().equals("ImageView[id=resSlot22, styleClass=image-view]" )) slot = 2;
        else slot = 3;
        gui.send(new DepositResourceCommand(movedRes, slot));
    }

    /**
     * the function, based on where the drag event starts saves on the clipboard the relative image (to be later placed in the warehouse)
     * and assigns to a global variable, useful to the deposit message, the kind of resource i'm try to deposit
     * @param mouseEvent detects the drag event
     */
    //based on how we would like to structure the updateWarehouse i don't have to save the Resource(?)
    public void movePendingRes(MouseEvent mouseEvent){
        ClipboardContent cb = new ClipboardContent();
        ImageView source;
        if(mouseEvent.getSource().toString().equals("ImageView[id=pendingCoin, styleClass=image-view]")) {
            movedRes = ResourceType.YELLOW;
            source = pendingCoin;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=pendingServant, styleClass=image-view]")) {
            movedRes = ResourceType.VIOLET;
            source = pendingServant;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=pendingShield, styleClass=image-view]")) {
            movedRes = ResourceType.BLUE;
            source = pendingShield;
        }
        else {
            movedRes = ResourceType.GREY;
            source = pendingStone;
        }
        Dragboard db = source.startDragAndDrop(TransferMode.COPY);
        cb.putImage(source.getImage());
        db.setContent(cb);
        mouseEvent.consume();
    }

    /**
     * communicates to the server that the user wants to pass the turn
     * @param event left click of the pass button
     */
    public void pass(ActionEvent event) {
        gui.send(new PassCommand());
    }

    /**
     * function that based on the source of the dragging event assigns from which row and column the card is taken from
     * @param mouseEvent drag the card from the card matrix
     */
    //don't think the drag e drop works
    public void moveDCard(MouseEvent mouseEvent) {
        ClipboardContent cb = new ClipboardContent();
        ImageView source;
        if (mouseEvent.getSource().toString().equals("ImageView[id=green1, styleClass=image-view]")) {
            source = green1;
            row = 1;
            column = 1;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=green2, styleClass=image-view]")) {
            source = green2;
            row = 2;
            column = 1;
        } else if(mouseEvent.getSource().toString().equals("ImageView[id=green3, styleClass=image-view]")) {
            source = green3;
            row = 3;
            column = 1;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=blue1, styleClass=image-view]")) {
            source = blue1;
            row = 1;
            column = 2;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=blue2, styleClass=image-view]")) {
            source = blue2;
            row = 2;
            column = 2;
        } else if(mouseEvent.getSource().toString().equals("ImageView[id=blue3, styleClass=image-view]")) {
            source = blue3;
            row = 3;
            column = 2;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=yellow1, styleClass=image-view]")) {
            source = yellow1;
            row = 1;
            column = 3;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=yellow2, styleClass=image-view]")) {
            source = yellow2;
            row = 2;
            column = 3;
        } else if(mouseEvent.getSource().toString().equals("ImageView[id=yellow3, styleClass=image-view]")) {
            source = yellow3;
            row = 3;
            column = 3;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=purple1, styleClass=image-view]")) {
            source = purple1;
            row = 1;
            column = 4;
        } else if (mouseEvent.getSource().toString().equals("ImageView[id=purple2, styleClass=image-view]")) {
            source = purple2;
            row = 2;
            column = 4;
        } else {
            source = purple3;
            row = 3;
            column = 4;
        }
        Dragboard db = source.startDragAndDrop(TransferMode.COPY);
        cb.putImage(source.getImage());
        db.setContent(cb);
        mouseEvent.consume();
        showCardMarket();
    }

    /**
     *
     * @param dragEvent
     */
    public void placeSlot(DragEvent dragEvent) {
        Integer dCardSlot;
        if(dragEvent.getTarget().toString().equals("ImageView[id=slot11, styleClass=image-view]") ||
                dragEvent.getTarget().toString().equals("ImageView[id=slot12, styleClass=image-view]") ||
                dragEvent.getTarget().toString().equals("ImageView[id=slot13, styleClass=image-view]")) dCardSlot = 1;
        else if(dragEvent.getTarget().toString().equals("ImageView[id=slot21, styleClass=image-view]") ||
                dragEvent.getTarget().toString().equals("ImageView[id=slot22, styleClass=image-view]") ||
                dragEvent.getTarget().toString().equals("ImageView[id=slot23, styleClass=image-view]")) dCardSlot = 2;
        else dCardSlot = 3;
        gui.send(new BuyCardCommand(row, column, dCardSlot));
    }

    /**
     * based on which of the 1 to 3 player's images clicked will be called a function to show the respective player's board
     * @param mouseEvent the left mouse click on a player's image in the top left corner
     */
    public void playerBoard(MouseEvent mouseEvent) {
        if (mouseEvent.getSource().toString().equals("ImageView[id=p1Board, styleClass=image-view]")) updateBoard(player1Name.getText());
        else if (mouseEvent.getSource().toString().equals("ImageView[id=p2Board, styleClass=image-view]")) updateBoard(player2Name.getText());
        else updateBoard(player3Name.getText());
    }

    public void getResources(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().toString().equals("ImageView[id=colonna1, styleClass=image-view]")){
            line = 'c';
            pos = 1;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=colonna2, styleClass=image-view]")){
            line = 'c';
            pos = 2;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=colonna3, styleClass=image-view]")){
            line = 'c';
            pos = 3;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=colonna4, styleClass=image-view]")){
            line = 'c';
            pos = 4;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=riga1, styleClass=image-view]")){
            line = 'r';
            pos = 1;
        }
        else if(mouseEvent.getSource().toString().equals("ImageView[id=riga2, styleClass=image-view]")){
            line = 'r';
            pos = 2;
        }
        else{
            line = 'r';
            pos = 3;
        }
        gui.send(new BuyFromMarketCommand(line, pos));
    }

    //because the deposit of resources is a drag e drop, it will be also with the discard
    public void discardResources(MouseEvent mouseEvent) {
//        gui.getOut().println(gui.getGson().toJson(new DiscardResourceCommand()));
    }

    /**
     * sets in the top left corner images and names based on the other players in the lobby
     */
    public void setIcons(){
        List<String> players = gui.getClientModel().getPlayersInOrder();
        List<String> disconnected=gui.getClientModel().getDisconnectedPlayers();
        players.remove(gui.getClientModel().getNickname());
        if(gui.getClientModel().getCurrentNickname().equals(gui.getClientModel().getNickname())) {
            //do something
        }
        if(players.size()==1){
            player1Name.setText(players.get(0));
            disc1.setVisible(disconnected.contains(players.get(0)));
            p1Board.setOpacity(100);
            p1Board.setCursor(Cursor.HAND);
            if(gui.getClientModel().getCurrentNickname().equals(player1Name.getText()))
                active1.setOpacity(100);
            else active1.setOpacity(0);
        }
        else if(players.size()==2){
            player1Name.setText(players.get(0));
            disc1.setVisible(disconnected.contains(players.get(0)));
            disc2.setVisible(disconnected.contains(players.get(1)));
            p1Board.setOpacity(100);
            p1Board.setCursor(Cursor.HAND);
            player2Name.setText(players.get(1));
            p2Board.setOpacity(100);
            p2Board.setCursor(Cursor.HAND);
            if(gui.getClientModel().getCurrentNickname().equals(player1Name.getText()))
                active1.setOpacity(100);
            else active1.setOpacity(0);
            if(gui.getClientModel().getCurrentNickname().equals(player2Name.getText()))
                active2.setOpacity(100);
            else active2.setOpacity(0);;
        }
        else if(players.size()==3){
            player1Name.setText(players.get(0));
            disc1.setVisible(disconnected.contains(players.get(0)));
            disc2.setVisible(disconnected.contains(players.get(1)));
            disc3.setVisible(disconnected.contains(players.get(2)));
            p1Board.setOpacity(100);
            p1Board.setCursor(Cursor.HAND);
            player2Name.setText(players.get(1));
            p2Board.setOpacity(100);
            p2Board.setCursor(Cursor.HAND);
            player3Name.setText(players.get(2));
            p3Board.setOpacity(100);
            p3Board.setCursor(Cursor.HAND);
            if(gui.getClientModel().getCurrentNickname().equals(player1Name.getText()))
                active1.setOpacity(100);
            else active1.setOpacity(0);
            if(gui.getClientModel().getCurrentNickname().equals(player2Name.getText()))
                active2.setOpacity(100);
            else active2.setOpacity(0);
            if(gui.getClientModel().getCurrentNickname().equals(player3Name.getText()))
                active3.setOpacity(100);
            else active3.setOpacity(0);
        }
        else return;
    }

    /**
     * toggles the production based on which anchorpane is selected
     * @param mouseEvent left click mouse in the slot(Anchorpane)
     */
    public void toggleProduction(MouseEvent mouseEvent) {
        int toggled;
        if(mouseEvent.getSource().toString().equals("a")) toggled = 1;
        else if(mouseEvent.getSource().toString().equals("b")) toggled = 2;
        else if(mouseEvent.getSource().toString().equals("c")) toggled = 3;
        else toggled = 0;
        gui.send(new ToggleProductionCommand(toggled));
    }

    public void substituteUnknown(MouseEvent mouseEvent) {

    }
}
