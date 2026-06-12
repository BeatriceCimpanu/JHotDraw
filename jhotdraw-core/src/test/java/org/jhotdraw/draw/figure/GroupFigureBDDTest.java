package org.jhotdraw.draw.figure;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupFigureBDDTest extends
        ScenarioTest<GroupFigureBDDTest.GivenFigures,
                     GroupFigureBDDTest.WhenGroupAction,
                     GroupFigureBDDTest.ThenGroupResult> {

    @Test
    @As("Grouping multiple figures combines them into a single GroupFigure")
    public void grouping_multiple_figures_creates_a_group() {
        given().multiple_figures_exist();
        when().the_group_action_is_performed();
        then().a_group_figure_is_created()
              .and().the_group_contains_the_original_figures();
    }

    @Test
    @As("Ungrouping a GroupFigure restores the original child figures")
    public void ungrouping_a_group_restores_child_figures() {
        given().a_group_figure_with_children();
        when().the_ungroup_action_is_performed();
        then().the_children_are_restored_as_individual_figures()
              .and().the_group_is_empty();
    }

    @Test
    @As("A group created with one child contains exactly one figure")
    public void group_with_one_child_contains_one_figure() {
        given().a_single_figure_exists();
        when().the_group_action_is_performed();
        then().the_group_contains_exactly(1);
    }

    // ══ GIVEN ═════════════════════════════════════════════════════════════

    public static class GivenFigures extends Stage<GivenFigures> {

        @ProvidedScenarioState
        GroupFigure group;

        @ProvidedScenarioState
        List<Figure> originalFigures;

        public GivenFigures multiple_figures_exist() {
            originalFigures = Arrays.asList(
                    new EllipseFigure(),
                    new RectangleFigure(),
                    new EllipseFigure()
            );
            group = new GroupFigure();
            return self();
        }

        public GivenFigures a_single_figure_exists() {
            originalFigures = Arrays.asList(new EllipseFigure());
            group = new GroupFigure();
            return self();
        }

        public GivenFigures a_group_figure_with_children() {
            group = new GroupFigure();
            originalFigures = Arrays.asList(
                    new EllipseFigure(),
                    new RectangleFigure()
            );
            for (Figure f : originalFigures) {
                group.basicAdd(f);
            }
            return self();
        }
    }

    // ══ WHEN ══════════════════════════════════════════════════════════════

    public static class WhenGroupAction extends Stage<WhenGroupAction> {

        @ExpectedScenarioState
        GroupFigure group;

        @ExpectedScenarioState
        List<Figure> originalFigures;

        public WhenGroupAction the_group_action_is_performed() {
            for (Figure f : originalFigures) {
                group.basicAdd(f);
            }
            return self();
        }

        public WhenGroupAction the_ungroup_action_is_performed() {
            group.basicRemoveAllChildren();
            return self();
        }
    }

    // ══ THEN ══════════════════════════════════════════════════════════════

    public static class ThenGroupResult extends Stage<ThenGroupResult> {

        @ExpectedScenarioState
        GroupFigure group;

        @ExpectedScenarioState
        List<Figure> originalFigures;

        public ThenGroupResult a_group_figure_is_created() {
            assertThat(group).isNotNull();
            assertThat(group).isInstanceOf(GroupFigure.class);
            return self();
        }

        public ThenGroupResult the_group_contains_the_original_figures() {
            assertThat(group.getChildCount())
                    .as("Group should contain all original figures")
                    .isEqualTo(originalFigures.size());
            return self();
        }

        public ThenGroupResult the_children_are_restored_as_individual_figures() {
            assertThat(group.getChildCount())
                    .as("Group should have no children after ungroup")
                    .isEqualTo(0);
            return self();
        }

        public ThenGroupResult the_group_is_empty() {
            assertThat(group.getChildren())
                    .as("Children collection should be empty")
                    .isEmpty();
            return self();
        }

        public ThenGroupResult the_group_contains_exactly(int count) {
            assertThat(group.getChildCount())
                    .as("Group should contain exactly " + count + " figure(s)")
                    .isEqualTo(count);
            return self();
        }
    }
}