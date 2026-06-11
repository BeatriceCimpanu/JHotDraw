package org.jhotdraw.draw.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState.Resolution;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.figure.Figure;

public class ThenTheDrawing extends Stage<ThenTheDrawing> {

    @ExpectedScenarioState
    DefaultDrawing drawing;
    @ExpectedScenarioState(resolution = Resolution.NAME)
    Figure bottomFigure;
    @ExpectedScenarioState(resolution = Resolution.NAME)
    Figure middleFigure;
    @ExpectedScenarioState(resolution = Resolution.NAME)
    Figure topFigure;

    public ThenTheDrawing the_bottom_figure_is_the_topmost_figure() {
        assertThat(drawing.getChildren()).last().isSameAs(bottomFigure);
        return self();
    }

    public ThenTheDrawing the_top_figure_is_the_bottommost_figure() {
        assertThat(drawing.getChildren()).first().isSameAs(topFigure);
        return self();
    }

    public ThenTheDrawing the_drawing_still_contains_the_same_three_figures() {
        assertThat(drawing.getChildren())
                .containsExactlyInAnyOrder(bottomFigure, middleFigure, topFigure);
        return self();
    }

    public ThenTheDrawing the_other_figures_keep_their_relative_order() {
        assertThat(drawing.getChildren()).containsSubsequence(bottomFigure, middleFigure);
        return self();
    }

    public ThenTheDrawing the_stacking_order_is_as_it_was_before() {
        assertThat(drawing.getChildren())
                .containsExactly(bottomFigure, middleFigure, topFigure);
        return self();
    }
}