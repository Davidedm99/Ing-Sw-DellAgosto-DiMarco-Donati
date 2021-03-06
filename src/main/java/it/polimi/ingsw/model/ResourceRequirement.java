package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.enums.ResourceType;
import it.polimi.ingsw.model.interfaces.Requirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceRequirement implements Requirement {

    @Expose
    private final Integer quantity;
    @Expose
    private final ResourceType resource;

    /**
     * base constructor
     * @param resource type of the requirement
     * @param quantity size of the requirement
     */
    protected ResourceRequirement(ResourceType resource, Integer quantity) {
        this.quantity = quantity;
        this.resource = resource;
    }

    /**
     *
     * @return the requirement quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     *
     * @return the requirement resource type
     */
    public ResourceType getResource() {
        return resource;
    }

    /**
     * checks all the resources the player has
     * @param board board that needs to be checked
     * @return true if the player has at least "quantity" "resource"
     */
    @Override
    public boolean check(Board board) {
        Map<ResourceType, Integer> totalResources = new HashMap<>(board.getStrongBox());
        Utilities.mergeResourceTypeMaps(totalResources, board.getWarehouse().getTotalResources());

        return quantity <= totalResources.get(resource);
    }
}
