package gui.util;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class MarginContainer extends GridPane {

    protected Node node;

    private double marginLeft = 0;
    private double marginRight = 0;
    private double marginTop = 0;
    private double marginBottom = 0;

    public MarginContainer(){
        getColumnConstraints().addAll(
                new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints()
        );

        getRowConstraints().addAll(
                new RowConstraints(), new RowConstraints(), new RowConstraints()
        );

        updateMargin();
    }

    public void setNode(Node node){
        this.node = node;
        getChildren().clear();
        add(node, 1,1);
    }

    public void removeNode(){
        getChildren().clear();
    }


    public void setMarginLeft(double marginLeft) {
        this.marginLeft = marginLeft;
        updateMargin();
    }

    public void setMarginRight(double marginRight) {
        this.marginRight = marginRight;
        updateMargin();
    }

    public void setMarginTop(double marginTop) {
        this.marginTop = marginTop;
        updateMargin();
    }

    public void setMarginBottom(double marginBottom) {
        this.marginBottom = marginBottom;
        updateMargin();
    }

    public void setMargin(double margin){
        marginLeft = margin;
        marginRight = margin;
        marginTop = margin;
        marginBottom = margin;
        updateMargin();
    }

    private void updateMargin(){
        getColumnConstraints().get(0).setPercentWidth(marginLeft *100);
        getColumnConstraints().get(1).setPercentWidth( 100 - (marginRight + marginLeft)*100 );
        getColumnConstraints().get(2).setPercentWidth(marginRight *100);

        getRowConstraints().get(0).setPercentHeight(marginTop *100);
        getRowConstraints().get(1).setPercentHeight( 100 - (marginTop + marginBottom)*100 );
        getRowConstraints().get(2).setPercentHeight(marginBottom *100);
    }
}
