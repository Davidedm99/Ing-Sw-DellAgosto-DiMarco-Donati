package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.exceptions.IllegalResourceException;
import it.polimi.ingsw.model.exceptions.ResourcesNotAvailableException;
import it.polimi.ingsw.model.exceptions.UnknownFoundException;
import it.polimi.ingsw.model.exceptions.UnknownNotFoundException;

import java.lang.reflect.Array;
import java.util.*;

public class Production {
    @Expose
    private Map<ResourceType,Integer> input;
    @Expose
    private Map<ResourceType,Integer> output;
    @Expose
    private List<ResourceType> inputHistory;
    @Expose
    private List<ResourceType> outputHistory;
    @Expose
    private boolean selected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(input, that.input) &&
                Objects.equals(output, that.output) &&
                Objects.equals(inputHistory, that.inputHistory) &&
                Objects.equals(outputHistory, that.outputHistory);
    }


    @Override
    public int hashCode() {
        return Objects.hash(input, output, inputHistory, outputHistory);
    }

    public Production(Production copy){
        input=new HashMap<>(copy.input);
        output=new HashMap<>(copy.output);
        inputHistory=new ArrayList<>(copy.inputHistory);
        outputHistory=new ArrayList<>(copy.outputHistory);
        selected=copy.selected;
    }


    /**
     * constructor that creates an empty production
     * selected is set to false
     */
    public Production(){
        input = new HashMap<>();
        output = new HashMap<>();
        inputHistory = new ArrayList<>();
        outputHistory = new ArrayList<>();
        selected = false;
    }

    /**
     * constructor that creates a production given an input and an output map
     * selected is set to false
     * @param input map that represents the input of the production
     * @param output map that represents the output of the production
     */
    public Production(Map<ResourceType,Integer> input, Map<ResourceType, Integer> output){
        this.input = input;
        this.output = output;
        inputHistory = new ArrayList<>();
        outputHistory = new ArrayList<>();
        selected = false;
    }

    /**
     * input get
     * @return the input of the production
     */
    public Map<ResourceType, Integer> getInput() {
        return input;
    }

    /**
     * output get
     * @return the output of the production
     */
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    /**
     * selected get
     * @return true if the production is selected
     */
    public boolean checkSelected(){
        return selected;
    }

    /**
     * if the attribute selected is true, it becomes false
     * if the attribute selected is false, it becomes true ONLY if the production is valid (doesn't have UNKNOWN resource type in or out)
     * @throws UnknownFoundException if you are trying to select a production not valid
     */
    public void toggleSelected() throws UnknownFoundException {
        if (selected)
            selected = false;
        else if (checkValidity()) {
                selected = true;
        } else throw new UnknownFoundException();
    }

    /**
     * adds to the input map a resource type and the corresponding quantity
     * @param resourceType type of input to be added
     * @param quantity quantity of the specified resource
     */
    public void addInput(ResourceType resourceType, Integer quantity) throws IllegalResourceException {
        if(resourceType.equals(ResourceType.EMPTY) || resourceType.equals(ResourceType.WHITE))
            throw new IllegalResourceException();
        input.merge(resourceType, quantity, Integer::sum);
    }

    /**
     * adds to the output map a resource type and the corresponding quantity
     * @param resourceType type of output to be added
     * @param quantity quantity of the specified resource
     */
    public void addOutput(ResourceType resourceType, Integer quantity) throws IllegalResourceException {
        if(resourceType.equals(ResourceType.EMPTY) || resourceType.equals(ResourceType.WHITE))
            throw new IllegalResourceException();
        output.merge(resourceType, quantity, Integer::sum);
    }

    /**
     * checks whether the production has any UNKNOWN resource type in the input or output map
     * @return false if the production has any UNKNOWN resource type in the input or output map, true otherwise
     */
    public boolean checkValidity(){
        for (Map.Entry<ResourceType, Integer> entry : input.entrySet()) {
            if (entry.getValue() > 0 && entry.getKey() == ResourceType.UNKNOWN) return false;
        }
        for (Map.Entry<ResourceType, Integer> entry : output.entrySet()) {
            if (entry.getValue() > 0 && entry.getKey() == ResourceType.UNKNOWN) return false;
        }
        return true;
    }

    /**
     * calls replaceUnknown on the input map and input history list
     * @param resourceType resource type unknown will be changed into
     * @throws UnknownNotFoundException thrown if the map doesn't contain unknowns
     */
    public void replaceUnknownInput(ResourceType resourceType) throws UnknownNotFoundException, IllegalResourceException {
        if(resourceType.equals(ResourceType.EMPTY) || resourceType.equals(ResourceType.WHITE))
            throw new IllegalResourceException();
        Utilities.replaceUnknown(input, inputHistory, resourceType);
    }

    /**
     * calls replaceUnknown on the output map and output history list
     * @param resourceType resource type unknown will be changed into
     * @throws UnknownNotFoundException thrown if the map doesn't contain unknowns
     */
    public void replaceUnknownOutput(ResourceType resourceType) throws UnknownNotFoundException, IllegalResourceException {
        if(resourceType.equals(ResourceType.EMPTY) || resourceType.equals(ResourceType.WHITE))
            throw new IllegalResourceException();
        Utilities.replaceUnknown(output, outputHistory, resourceType);
    }

    /**
     * calls revertUnknown on the input and output maps passing inputHistory and outputHistory as the backups
     */
    public void resetProduction() {
        Utilities.revertUnknown(input, inputHistory);
        Utilities.revertUnknown(output, outputHistory);
    }

//    /**
//     * removes from the input map the quantity of the resource
//     * @param resourceType is the type of the resource
//     * @param quantity of the specified resource
//     * @throws ResourcesNotAvailableException when the resource is not available
//     */
//    protected void removeInput(ResourceType resourceType, Integer quantity) throws ResourcesNotAvailableException {
//        if (input.containsKey(resourceType) && input.get(resourceType)>=quantity) {
//            input.replace(resourceType,input.get(resourceType)-quantity);
//        } else throw new ResourcesNotAvailableException();
//    }
//
//    /**
//     * removes from the output map the quantity of the resource
//     * @param resourceType is the type of the resource
//     * @param quantity of the specified resource
//     * @throws ResourcesNotAvailableException when the resource is not available
//     */
//    protected void removeOutput(ResourceType resourceType, Integer quantity) throws ResourcesNotAvailableException {
//        if (output.containsKey(resourceType) && output.get(resourceType)>=quantity) {
//            output.replace(resourceType,output.get(resourceType)-quantity);
//        } else throw new ResourcesNotAvailableException();
//    }
}
