package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.interfaces.Requirement;

import java.util.Map;
import java.util.Stack;

public class LevelCardRequirement implements Requirement {
    @Expose
    private final Color color;
    @Expose
    private final Integer quantity;
    @Expose
    private final Integer level;

    protected LevelCardRequirement(Color color, Integer quantity, Integer level) {
        this.color = color;
        this.quantity = quantity;
        this.level = level;
    }

    /**
     *
     * @return the requirement color
     */
    public Color getColor() {
        return color;
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
     * @return the requirement level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * checks the development cards on the board looking for a specific color, level and quantity
     * @param board board that needs to be checked
     * @return true if the board has at least "quantity" of development cards of color "color" and level "level", false otherwise
     */
    @Override
    public boolean check(Board board) {
        int counter = 0;
        Map<Integer, Stack<DevelopmentCard>> slots = board.getSlots();

        for(Integer integer : slots.keySet()) {
            for(DevelopmentCard developmentCard : slots.get(integer)) {
                if (developmentCard.getColor().equals(color) && developmentCard.getLevel().equals(level))
                    counter++;
            }
        }

        return quantity <= counter;
    }
}
