package it.polimi.ingsw.network.client.GUI;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Result;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Utilities;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientModel.ClientModel;
import it.polimi.ingsw.network.client.ClientVisitorHandler;
import it.polimi.ingsw.network.client.GUI.Controllers.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.updates.*;
import it.polimi.ingsw.network.server.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class GUI extends Application implements Client {
    private Stage stage;
    private Scene currentScene;
    private final Map<String, Scene> buildedScenes = new HashMap<>();
    private final Map<String, ControllerGUI> buildedControllers = new HashMap<>();
    public static final String LOGIN = "login.fxml";
    public static final String WAITING = "waiting_screen.fxml";
    public static final String NPLAYERS = "numbers.fxml";
    public static final String BOARD = "board.fxml";
    public static final String LOBBY = "lobby.fxml";
    public static final String SETUP = "setup_phase.fxml";
    public static final String OTHERBOARD = "otherPlayersBoard.fxml";
    public static final String RESULTS = "winning_scene.fxml";


    private String serverIP;
    private Integer serverPortNumber;
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private Gson gson;
    private final ClientVisitorHandler clientVisitorHandler;
    private ClientModel clientModel;

    public Map<String, ControllerGUI> getBuildedControllers() {
        return buildedControllers;
    }

    public GUI(){
        clientVisitorHandler = new ClientVisitorHandler();
        clientModel = new ClientModel();
        gson = Utilities.initializeGsonMessage();
    }

    @Override
    public void start(Stage stage){
        setup();
        this.stage = stage;
        run();

    }

    /**
     * a command is sent out using gson and json
     * @param m message passed
     */
    public void send(Message m){
        out.println(gson.toJson(m,Message.class));
    }

    /**
     * first scene of the game that sets the current scene, the size and the game icon
     */
    private void run() {
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/calamaio.png")));
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }

    private void resetScenes(){
        for (Map.Entry<String, ControllerGUI> entry : buildedControllers.entrySet()) {
            entry.getValue().reset();
        }
    }

    /**
     * as long as the client is connected, it handles the commands from the client to the server
     */
    private void handleMessages(){
        String jsonString="";
        while (!socket.isClosed()) {
            try {
                jsonString = in.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Error, disconnecting . . .");
                in.close();
                out.close();
                if(clientModel.getGamePhase()!=null && clientModel.getGamePhase().equals(GamePhase.ENDGAME))
                    Platform.runLater(new Thread(()->changeScene(RESULTS)));
                else {
                    Platform.runLater(new Thread(this::resetScenes));
                    Platform.runLater(new Thread(() -> changeScene(LOGIN)));

                }
                break;
            }
            try {
                ClientMessage message = gson.fromJson(jsonString, ClientMessage.class);
                Platform.runLater(new Thread(()->message.accept(clientVisitorHandler, this)));
            }catch (JsonSyntaxException e ){
                return;
            }
        }
    }
    @Override
    public void stop(){
        if(in!=null) in.close();
        if(out!=null) out.close();
        if(socket!=null) {
            try {
                socket.close();
            } catch (IOException ignored) { }
        }
    }

    /**
     * when a player hits the login button it sets a connection based on the ip and port
     * @param serverIP ip set in the login scene
     * @param serverPortNumber port number set in the login scene
     * @return true if the connection is successful, false otherwise
     */
    public boolean connect(String serverIP,Integer serverPortNumber){
        try {
            socket = new Socket(serverIP, serverPortNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            ComunicationController.showError(currentScene,"Server not available");
            LogIn s=(LogIn)buildedControllers.get(LOGIN);
            s.unlock();
            return false;
        }
        this.serverIP=serverIP;
        this.serverPortNumber=serverPortNumber;
        new Thread(this::handleMessages).start();
        return true;

    }

    /**
     * GUI setup where a map that contains all the scenes and all the controller is created, so it's easier to switch
     * among them, also sets the first scene as the login scene
     * */
    private void setup() {
        List<String> listFxml = new ArrayList<>(Arrays.asList(LOGIN,NPLAYERS,BOARD,LOBBY,WAITING,SETUP,OTHERBOARD,RESULTS));
        try{
            for(String path : listFxml){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                Parent content = loader.load();
                buildedScenes.put(path, new Scene(content));
                ControllerGUI controller = loader.getController();
                controller.setGUI(this);
                controller.initializeElements();
                buildedControllers.put(path, controller);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        currentScene = buildedScenes.get(LOGIN);
    }

    /**
     * this function change the scene based on the passed String
     */
    public void changeScene(String newScene) {
        currentScene = buildedScenes.get(newScene);
        //stage.setWidth(currentScene.getWidth());
        //stage.setHeight(currentScene.getHeight());
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * useful to the pop-ups
     * @return the current scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    public static void main(String[] args) { launch(args); }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public ClientModel getClientModel() {
        return clientModel;
    }

    @Override
    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public Gson getGson() {
        return gson;
    }

    @Override
    public Scanner getIn() {
        return in;
    }

    @Override
    public PrintWriter getOut() {
        return out;
    }

    @Override
    public void visualizeDisconnectionMessage(DisconnectionMessage message) {

    }

    @Override
    public void visualizeGeneralMessage(ClientMessage message) {

    }

    /**
     * function that tells the player what to do in the lobby scene
     * @param message update message
     */
    @Override
    public void visualizeLobbyInfoMessage(LobbyInfoMessage message) {
        Lobby lc = (Lobby)buildedControllers.get(LOBBY);
        Platform.runLater(new Thread(()->lc.setPlayers(message.getNickList(),message.getNum())));
        if(currentScene!=buildedScenes.get(LOBBY))
             Platform.runLater(new Thread(()->changeScene(LOBBY)));
    }

    /**
     * shows any update in the board slots
     */
    @Override
    public void visualizeSlotUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateCardMatrix();
        bc.updateSlots();
    }

    /**
     * if it's the current player turn to do the setup is sent to that scene else is shown a waiting scene
     */
    @Override
    public void visualizeStartGameUpdate() {
        refreshBoard();
        if(clientModel.getCurrentNickname().equals(clientModel.getNickname()))
            Platform.runLater(new Thread(()->changeScene(SETUP)));
        else Platform.runLater(new Thread(()->changeScene(WAITING)));
    }

    /**
     * when a player clicks on the pass button everything that has to be resetted is resetted and the game phase changes
     * @param previousGamePhase current game phase
     */
    @Override
    public void visualizeNewTurnUpdate(GamePhase previousGamePhase) {
        if(clientModel.getGamePhase().equals(GamePhase.SETUP)){
            if(clientModel.getCurrentNickname().equals(clientModel.getNickname()))
                Platform.runLater(new Thread(()->changeScene(SETUP)));
            else Platform.runLater(new Thread(()->changeScene(WAITING)));
        }else if(clientModel.getGamePhase().equals(GamePhase.ONGOING)) {
            if(clientModel.getCurrentNickname().equals(clientModel.getNickname())){
                ComunicationController.showInfo(currentScene,"It's your turn sir");
            }
            BoardController bc = (BoardController) buildedControllers.get(BOARD);
            bc.setIcons();
            bc.updateUnknown();
            bc.resetProductions();
            Platform.runLater(new Thread(() -> changeScene(BOARD)));
        }
    }

    /**
     * if the player has to choose a bonus resource the massage is shown
     * @param message number of bonus resources based on the player
     */
    @Override
    public void visualizeBonusResourceMessage(BonusResourceMessage message) {
        ComunicationController.showInfo(currentScene, message.getMessage());
    }

    /**
     * graphic update of the player's pending resources
     */
    @Override
    public void visualizePendingResourceUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updatePending();
    }

    /**
     * graphic update of the resources in the deposit
     * @param message with the content of the player's board
     */
    @Override
    public void visualizeDepositUpdate(DepositUpdate message) {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        if(clientModel.getNickname().equals(clientModel.getCurrentNickname())) {
            if(clientModel.getGamePhase()==GamePhase.SETUP)
            {
                SetupController s = (SetupController) buildedControllers.get(SETUP);
                s.updateWarehouse();
            }
            bc.updateWarehouse();
        }
    }

    /**
     * shows what Lozenzo did in his turn
     * @param message contains lorenzo's action (discard/advance in faith path...)
     * @param previousGamePhase current game phase
     */
    @Override
    public void visualizeLorenzoUpdate(LorenzoUpdate message, GamePhase previousGamePhase) {
        if(clientModel.getGamePhase().equals(GamePhase.ONGOING)) {
            BoardController bc = (BoardController) buildedControllers.get(BOARD);

            bc.updateBlackCross();
            bc.updateCardMatrix();
            bc.updateUnknown();
            bc.resetProductions();
            Platform.runLater(new Thread(() -> changeScene(BOARD)));
        }
        if(previousGamePhase==GamePhase.SETUP && clientModel.getGamePhase()==GamePhase.ONGOING){
            ComunicationController.showLorenzo(currentScene, "Welcome to Florence! May the best man win");
        }else {
            ComunicationController.showLorenzo(currentScene, message.getMessage());
        }
    }

    /**
     * graphic update of the pope favor cards
     */
    @Override
    public void visualizePopeFavorUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updatePopeFavor();
    }

    /**
     * graphic update of the moved resources in the warehouse
     */
    @Override
    public void visualizeMoveResourceUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateWarehouse();
    }

    /**
     * shows a pop up that contains an error
     * @param message the error encountered
     */
    @Override
    public void visualizeErrorMessage(ErrorMessage message) {
        ComunicationController.showError(currentScene, message.getMessage());
    }

    @Override
    public void visualizeLeadersInHandUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateLCards();
        bc.setupClickable();
    }

    @Override
    public void visualizeDepositsUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updatePickedRes();
        bc.updateWarehouse();
        bc.updateStrongbox();
        bc.updateUnknown();
        bc.updateDiscountsExtraProd();
    }

    @Override
    public void visualizeToggleProductionUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateToggledProduction();
        bc.updateDiscountsExtraProd();
    }

    @Override
    public void visualizeUnknownProductionUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateUnknown();
    }

    @Override
    public void visualizePickUpWarehouseUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateWarehouse();
        bc.updatePickedRes();
    }

    @Override
    public void visualizePickUpStrongboxUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateStrongbox();
        bc.updatePickedRes();
    }

    @Override
    public void visualizePlayLeaderUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateLCards();
        bc.setLeaderPower();
    }

    @Override
    public void visualizeToggleDiscountUpdate() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateDiscountsExtraProd();
    }

    @Override
    public void visualizeEndGameMessage() {
        ComunicationController.endGameNotify(currentScene);
    }

    @Override
    public void visualizeEndGameResultUpdate(Result gameResult) {
        EndGameController ec = (EndGameController) buildedControllers.get(RESULTS);
        ec.setValues(gameResult);
        Platform.runLater(new Thread(()->changeScene(RESULTS)));
    }

    @Override
    public void visualizeNumberOfPlayer(PlayerNumberRequest message) {
        Platform.runLater(new Thread(()->changeScene(NPLAYERS)));
    }

    @Override
    public void visualizeWait() {
        Platform.runLater(new Thread(()->changeScene(WAITING)));
    }

    @Override
    public void visualizeRegisterRequest() {
        if(currentScene==buildedScenes.get(LOGIN)){
            LogIn l=(LogIn)buildedControllers.get(LOGIN);
            l.register();
        }else{
            changeScene(LOGIN);
        }
    }

    @Override
    public void visualizeReconnection(ReconnectUpdate message) {
        if(clientModel.getGamePhase()==GamePhase.SETUP && clientModel.getCurrentNickname().equals(clientModel.getNickname())){
            changeScene(SETUP);
        }else if(clientModel.getGamePhase()==GamePhase.SETUP && !clientModel.getCurrentNickname().equals(clientModel.getNickname())){
            changeScene(WAITING);
        }
        else if(clientModel.getGamePhase()==GamePhase.ONGOING){
            changeScene(BOARD);
        }
        StringBuilder sb=new StringBuilder();
        sb.append("You just reconnected:\n");
        sb.append("Phase: ").append(clientModel.getGamePhase()).append("\n");
        sb.append("Turn: ").append(clientModel.getCurrentNickname()).append("\n");
        ComunicationController.showInfo(currentScene,sb.toString());

        refreshBoard();
    }
    public void refreshBoard(){
        BoardController l=(BoardController)buildedControllers.get(BOARD);
        l.updateBlackCross();
        l.updateFaithPath();
        l.updateCardMatrix();
        l.updateResMarket();
        l.updateWarehouse();
        l.updateLCards();
        l.setIcons();
        l.updatePopeFavor();
        l.updateStrongbox();
        l.updateSlots();
        l.updateUnknown();
        l.updatePickedRes();
        l.updateDiscountsExtraProd();
        l.updatePending();
        l.setupClickable();
        l.setLeaderPower();
        l.updateToggledProduction();
        if(clientModel.getGamePhase()==GamePhase.SETUP) {
            SetupController s = (SetupController) buildedControllers.get(SETUP);
            s.updateLeader();
            s.updateWarehouse();
        }
    }

    @Override
    public void visualizeDiscardLeaderUpdate() {
        BoardController l=(BoardController)buildedControllers.get(BOARD);
        l.updateLCards();
    }

    @Override
    public void visualizeFaithUpdate() {
        if(clientModel.getNickname().equals(clientModel.getCurrentNickname())){
            BoardController l=(BoardController)buildedControllers.get(BOARD);
            l.updateFaithPath();
        }
    }

    @Override
    public void visualizeMarketUpdate() {
        BoardController l=(BoardController)buildedControllers.get(BOARD);
        l.updateResMarket();
    }

    @Override
    public void visualizeDisconnectedMessage(String nickname) {
        if(currentScene!=buildedScenes.get(LOBBY)) {
            BoardController l=(BoardController)buildedControllers.get(BOARD);
            ComunicationController.showInfo(currentScene, "A player disconnected: " + clientModel.getDisconnectedPlayers());
            l.setIcons();
            if(currentScene!=buildedScenes.get(LOGIN) && clientModel.getDisconnectedPlayers().contains(clientModel.getNickname())){
                Platform.runLater(new Thread(this::resetScenes));
                Platform.runLater(new Thread(()->changeScene(LOGIN)));
                Platform.runLater(new Thread(()->{
                    out.close();
                    in.close();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }
        }
        else{
            Lobby lo=(Lobby) buildedControllers.get(LOBBY);
            lo.disconnectPlayer(nickname);
        }
    }

    @Override
    public void visualizeReconnectMessage(ReconnectMessage message) {
        if(clientModel.getCurrentNickname()!=null && !message.getReconnectedNickname().equals(clientModel.getNickname())){
            ComunicationController.showInfo(currentScene, message.getMessage());
            BoardController l = (BoardController) buildedControllers.get(BOARD);
            l.setIcons();
        }
    }

    @Override
    public void visualizeDiscardResource() {
        BoardController l = (BoardController) buildedControllers.get(BOARD);
        l.updateBlackCross();
        l.updatePending();
        l.updateFaithPath();
    }

    @Override
    public void visualizeRevert() {
        BoardController l = (BoardController) buildedControllers.get(BOARD);
        l.updatePickedRes();
    }

    @Override
    public void visualizeResetProductions() {
        BoardController bc = (BoardController) buildedControllers.get(BOARD);
        bc.updateUnknown();
        bc.updateDiscountsExtraProd();
    }
}
