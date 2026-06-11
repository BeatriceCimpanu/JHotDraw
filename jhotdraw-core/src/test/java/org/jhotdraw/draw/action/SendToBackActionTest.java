package org.jhotdraw.draw.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Before;
import org.junit.Test;
public class SendToBackActionTest {

    private DefaultDrawing drawing;
    private DrawingView view;
    private Figure back;
    private Figure middle;
    private Figure front;

    @Before
    public void setUp() {
        drawing = new DefaultDrawing();
        back = new RectangleFigure(0, 0, 10, 10);
        middle = new RectangleFigure(20, 0, 10, 10);
        front = new RectangleFigure(40, 0, 10, 10);
        drawing.add(back);
        drawing.add(middle);
        drawing.add(front);
        view = mock(DrawingView.class);
        when(view.getDrawing()).thenReturn(drawing);
    }

    @Test
    public void sendToBackMovesTopFigureToBack() {
        SendToBackAction.sendToBack(view, Collections.singletonList(front));
        assertEquals(Arrays.asList(front, back, middle), drawing.getChildren());
    }

    @Test
    public void sendToBackKeepsFigureAlreadyAtBack() {
        SendToBackAction.sendToBack(view, Collections.singletonList(back));
        assertEquals(Arrays.asList(back, middle, front), drawing.getChildren());
    }

    @Test
    public void sendToBackIgnoresFigureNotInDrawing() {
        Figure stranger = new RectangleFigure(60, 0, 10, 10);
        SendToBackAction.sendToBack(view, Collections.singletonList(stranger));
        assertEquals(Arrays.asList(back, middle, front), drawing.getChildren());
    }

    @Test
    public void sendToBackWithEmptySelectionChangesNothing() {
        SendToBackAction.sendToBack(view, Collections.<Figure>emptyList());
        assertEquals(Arrays.asList(back, middle, front), drawing.getChildren());
    }

    @Test
    public void sendToBackMovesMultipleFiguresInGivenOrder() {
        SendToBackAction.sendToBack(view, Arrays.asList(middle, front));
        assertEquals(Arrays.asList(front, middle, back), drawing.getChildren());
    }
}