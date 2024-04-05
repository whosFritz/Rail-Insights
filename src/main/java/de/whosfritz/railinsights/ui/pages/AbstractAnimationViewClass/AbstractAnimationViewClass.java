package de.whosfritz.railinsights.ui.pages.AbstractAnimationViewClass;

import de.mekaso.vaadin.addon.compani.viewtransitions.AnimatedView;
import de.mekaso.vaadin.addon.compani.viewtransitions.ViewInTransition;
import de.mekaso.vaadin.addon.compani.viewtransitions.ViewOutTransition;

public interface AbstractAnimationViewClass extends AnimatedView {
    @Override
    public default ViewInTransition enterWith() {
        return ViewInTransition.MoveFromLeft;
    }

    @Override
    public default ViewOutTransition exitWith() {
        return ViewOutTransition.MoveToRight;
    }
}
