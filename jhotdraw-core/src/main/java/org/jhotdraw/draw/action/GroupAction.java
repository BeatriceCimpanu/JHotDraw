/*
 * @(#)GroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.GroupFigure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Action that groups or ungroups selected figures on the drawing canvas.
 *
 * <p>When used as a grouping action, combines all selected figures into a
 * single {@link CompositeFigure}. When used as an ungrouping action,
 * dissolves the selected composite figure back into its individual children.
 *
 * <p>Both operations are undoable and redoable.
 *
 * @author Werner Randelshofer
 */
public class GroupAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.groupSelection";
    private CompositeFigure prototype;
    /**
     * If this variable is true, this action groups figures.
     * If this variable is false, this action ungroups figures.
     */
    private boolean isGroupingAction;
    private static final String LABELS_BUNDLE = "org.jhotdraw.draw.Labels";
    /**
     * Creates a new instance.
     */
    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure(), true);
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
        this(editor, prototype, true);
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype, boolean isGroupingAction) {
        super(editor);
        this.prototype = prototype;
        this.isGroupingAction = isGroupingAction;
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle(LABELS_BUNDLE);
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(isGroupingAction ? canGroup() : canUngroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        return getView() != null && getView().getSelectionCount() > 1;
    }

    protected boolean canUngroup() {
        return getView() != null
                && getView().getSelectionCount() == 1
                && prototype != null
                && getView().getSelectedFigures().iterator().next().getClass().equals(
                        prototype.getClass());
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (isGroupingAction) {
        performGroup();
    } else {
        performUngroup();
    }
    }

    private void performGroup() {
    if (canGroup()) {
        final DrawingView view = getView();
        final LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
        final CompositeFigure group = (CompositeFigure) prototype.clone();
        UndoableEdit edit = new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                return ResourceBundleUtil.getBundle(LABELS_BUNDLE)
                        .getString("edit.groupSelection.text");
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                groupFigures(view, group, ungroupedFigures);
            }

            @Override
            public void undo() throws CannotUndoException {
                ungroupFigures(view, group);
                super.undo();
            }
        };
        groupFigures(view, group, ungroupedFigures);
        fireUndoableEditHappened(edit);
    }
}

private void performUngroup() {
    if (canUngroup()) {
        final DrawingView view = getView();
        final CompositeFigure group = (CompositeFigure) getView().getSelectedFigures().iterator().next();
        final LinkedList<Figure> ungroupedFigures = new LinkedList<>();
        UndoableEdit edit = new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                return ResourceBundleUtil.getBundle(LABELS_BUNDLE)
                        .getString("edit.ungroupSelection.text");
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                ungroupFigures(view, group);
            }

            @Override
            public void undo() throws CannotUndoException {
                groupFigures(view, group, ungroupedFigures);
                super.undo();
            }
        };
        ungroupedFigures.addAll(ungroupFigures(view, group));
        fireUndoableEditHappened(edit);
    }
}
/**
 * Ungroups the given composite figure, restoring its children as individual
 * figures in the drawing at the position previously occupied by the group.
 *
 * <p>This method is shared by the undo operation of grouping and by
 * {@link UngroupAction}.
 *
 * @param view  the current drawing view
 * @param group the composite figure to dissolve
 * @return the list of restored child figures
 */
    public Collection<Figure> ungroupFigures(DrawingView view, CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }
/**
 * Groups the given figures into the provided composite figure and inserts
 * it into the drawing at the position of the bottom-most selected figure.
 *
 * @param view    the current drawing view (used for selection management)
 * @param group   the composite figure that will contain the grouped figures
 * @param figures the figures to be grouped (will be removed from the drawing)
 */
    public void groupFigures(DrawingView view, CompositeFigure group, Collection<Figure> figures) {
        Collection<Figure> sorted = view.getDrawing().sort(figures);
        int index = view.getDrawing().indexOf(sorted.iterator().next());
        
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(index, group);
        
        group.willChange();
        for (Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();

        view.addToSelection(group);
    }
}
