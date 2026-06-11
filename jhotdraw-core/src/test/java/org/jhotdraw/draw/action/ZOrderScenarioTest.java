package org.jhotdraw.draw.action;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class ZOrderScenarioTest extends ScenarioTest<GivenADrawing, WhenTheUser, ThenTheDrawing> {

    @Test
    public void bring_a_figure_to_the_front() {
        given().a_drawing_with_three_overlapping_figures();
        when().the_user_brings_the_bottom_figure_to_front();
        then().the_bottom_figure_is_the_topmost_figure()
                .and().the_drawing_still_contains_the_same_three_figures();
    }

    @Test
    public void send_a_figure_to_the_back() {
        given().a_drawing_with_three_overlapping_figures();
        when().the_user_sends_the_top_figure_to_back();
        then().the_top_figure_is_the_bottommost_figure()
                .and().the_other_figures_keep_their_relative_order();
    }

    @Test
    public void undo_a_z_order_change() {
        given().a_drawing_with_three_overlapping_figures();
        when().the_user_has_brought_the_bottom_figure_to_front_with_the_action()
                .and().the_user_performs_undo();
        then().the_stacking_order_is_as_it_was_before();
    }
}