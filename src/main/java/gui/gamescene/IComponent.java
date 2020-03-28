package gui.gamescene;

import javafx.scene.Node;


/** A Component wraps the logic of creating some Node */
public interface IComponent {

    /**
     * Returns the Node which is may drawn in an arbitrary parent (JavaFX). The returned
     * Node just be designed such that it may be scaled.
     */
    Node getNode();

}
