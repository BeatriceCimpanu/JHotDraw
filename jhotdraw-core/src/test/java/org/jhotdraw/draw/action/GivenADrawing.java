package org.jhotdraw.draw.action;

import static org.mockito.Mockito.mock;
import org.mockito.Mockito;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import com.tngtech.jgiven.annotation.ScenarioState.Resolution;

public class GivenADrawing extends Stage<GivenADrawing> {

    @ProvidedScenarioState
    DefaultDrawing drawing;
    @ProvidedScenarioState
    DrawingView view;
    @ProvidedScenarioState(resolution = Resolution.NAME)
    Figure bottomFigure;
    @ProvidedScenarioState(resolution = Resolution.NAME)
    Figure middleFigure;
    @ProvidedScenarioState(resolution = Resolution.NAME)
    Figure topFigure;
    public GivenADrawing a_drawing_with_three_overlapping_figures() {
        drawing = new DefaultDrawing();
        bottomFigure = new RectangleFigure(0, 0, 10, 10);
        middleFigure = new RectangleFigure(5, 5, 10, 10);
        topFigure = new RectangleFigure(10, 10, 10, 10);
        drawing.add(bottomFigure);
        drawing.add(middleFigure);
        drawing.add(topFigure);
        view = mock(DrawingView.class);
        Mockito.when(view.getDrawing()).thenReturn(drawing);       
        return self();
    }
}