package org.jhotdraw.draw.action;

import static org.mockito.Mockito.mock;
import org.mockito.Mockito;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState.Resolution;
import java.util.Collections;
import javax.swing.undo.UndoManager;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

public class WhenTheUser extends Stage<WhenTheUser> {

    @ExpectedScenarioState
    DefaultDrawing drawing;
    @ExpectedScenarioState
    DrawingView view;
    @ExpectedScenarioState(resolution = Resolution.NAME)
    Figure bottomFigure;
    @ExpectedScenarioState(resolution = Resolution.NAME)
    Figure topFigure;
    @ProvidedScenarioState
    UndoManager undoManager;

    public WhenTheUser the_user_brings_the_bottom_figure_to_front() {
        BringToFrontAction.bringToFront(view, Collections.singletonList(bottomFigure));
        return self();
    }

    public WhenTheUser the_user_sends_the_top_figure_to_back() {
        SendToBackAction.sendToBack(view, Collections.singletonList(topFigure));
        return self();
    }

    public WhenTheUser the_user_has_brought_the_bottom_figure_to_front_with_the_action() {
        undoManager = new UndoManager();
        drawing.addUndoableEditListener(undoManager);
        DrawingEditor editor = mock(DrawingEditor.class);
        Mockito.when(editor.getActiveView()).thenReturn(view);
        Mockito.when(view.getSelectedFigures()).thenReturn(Collections.singleton(bottomFigure));
        new BringToFrontAction(editor).actionPerformed(null);
        return self();
    }

    public WhenTheUser the_user_performs_undo() {
        undoManager.undo();
        return self();
    }
}