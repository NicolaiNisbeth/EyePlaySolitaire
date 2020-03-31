package gui.util;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


/**
 * A container, allowing you to add a percentage Margin
 * to an arbitrary Node object.
 *
 * The margin is calculated in percentage, such that an
 * x% margin value (left, right, top, bottom) takes up
 * x percentage of the total space allowed for the
 * container.
 * Example: If the container has a width of 150px, a
 * 10% right margin adds a 15px margin to the right.
 *
 * */
public class MarginContainer extends GridPane {
    /*  Implementation notes:
        The margin is implemented as a GridPane, as it allows
        us to set each row and column to take up a percentage
        amount of space.
        Thus the grid exists of 3 rows and 3 columns, and the
        center cell (coordinates (1,1)) is the Node cell.
     */

    protected Node node;

    private double marginLeft = 0;
    private double marginRight = 0;
    private double marginTop = 0;
    private double marginBottom = 0;

    /** Create a container with 0 margin and no container node. */
    public MarginContainer(){
        getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints());
        getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), new RowConstraints());

        updateMargin();
    }


    /**
     * Set the current node of the container. If a Node
     * is already contained, it will be removed.
     */
    public void setNode(Node node){
        this.node = node;
        getChildren().clear();
        add(node, 1,1);
    }

    /**
     * Removes the currently contained Node.
     */
    public void removeNode(){
        getChildren().clear();
    }


    /**
     * Sets the left margin in decimal percentage values
     * (0.1 = 10%).
     */
    public void setMarginLeft(double marginLeft) {
        this.marginLeft = marginLeft;
        updateMargin();
    }

    /**
     * Sets the right margin in decimal percentage values
     * (0.1 = 10%).
     */
    public void setMarginRight(double marginRight) {
        this.marginRight = marginRight;
        updateMargin();
    }

    /**
     * Sets the top margin in decimal percentage values
     * (0.1 = 10%).
     */
    public void setMarginTop(double marginTop) {
        this.marginTop = marginTop;
        updateMargin();
    }

    /**
     * Sets the bottom margin in decimal percentage values
     * (0.1 = 10%).
     */
    public void setMarginBottom(double marginBottom) {
        this.marginBottom = marginBottom;
        updateMargin();
    }

    /**
     * Sets all the margin to the same decimal percentage
     * value (0.1 -> 10%).
     */
    public void setMargin(double margin){
        marginLeft = margin;
        marginRight = margin;
        marginTop = margin;
        marginBottom = margin;
        updateMargin();
    }

    // Update the grid cell contrains to the current margin values
    private void updateMargin(){
        getColumnConstraints().get(0).setPercentWidth(marginLeft *100);
        getColumnConstraints().get(1).setPercentWidth( 100 - (marginRight + marginLeft)*100 );
        getColumnConstraints().get(2).setPercentWidth(marginRight *100);

        getRowConstraints().get(0).setPercentHeight(marginTop *100);
        getRowConstraints().get(1).setPercentHeight( 100 - (marginTop + marginBottom)*100 );
        getRowConstraints().get(2).setPercentHeight(marginBottom *100);
    }
}
