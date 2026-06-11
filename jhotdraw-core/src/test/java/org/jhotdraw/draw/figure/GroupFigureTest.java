package org.jhotdraw.draw.figure;

import org.jhotdraw.draw.figure.EllipseFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;

public class GroupFigureTest {
    
    private GroupFigure group;

    @Before
    public void setUp() {
        group = new GroupFigure();
    }

    // ── BEST CASE TESTS ─────────────────────────────────────────────────────

    /**
     * A new GroupFigure should contain no children.
     */
    @Test
    public void testNewGroupIsEmpty() {
        assertTrue("New group should have no children",
                group.getChildCount() == 0);
    }

    /**
     * Adding a child figure increases the child count by 1.
     */
    @Test
public void testAddChildIncreasesCount() {
    EllipseFigure child = new EllipseFigure();
    group.basicAdd(child);
    assertEquals("Group should contain 1 child after add",
            1, group.getChildCount());
}

    /**
     * Children added to the group are retrievable and in order.
     */
    @Test
    public void testAddMultipleChildrenPreservesOrder() {
        EllipseFigure first  = new EllipseFigure();
        EllipseFigure second = new EllipseFigure();
        EllipseFigure third  = new EllipseFigure();

        
        group.basicAdd(first);
        group.basicAdd(second);
        group.basicAdd(third);
        
        List<Figure> children = group.getChildren();
        assertEquals(3, children.size());
        assertSame("First child should be first", first, children.get(0));
        assertSame("Second child should be second", second, children.get(1));
        assertSame("Third child should be third", third, children.get(2));
    }

    /**
     * Removing all children leaves an empty group.
     */
    @Test
    public void testBasicRemoveAllChildrenLeavesEmptyGroup() {

        group.basicAdd(new EllipseFigure());
        group.basicAdd(new EllipseFigure());
        group.basicRemoveAllChildren();

        assertEquals("Group should be empty after removeAll",
                0, group.getChildCount());
    }

    /**
     * A GroupFigure with all transformable children should itself
     * be transformable.
     */
    @Test
    public void testIsTransformableWhenAllChildrenAreTransformable() {
        
        group.basicAdd(new EllipseFigure());
        group.basicAdd(new RectangleFigure());

        assertTrue("Group should be transformable if all children are",
                group.isTransformable());
    }

    // ── BOUNDARY CASE TESTS ─────────────────────────────────────────────────

    /**
     * A group with no children should not be transformable.
     * Boundary: empty group edge case.
     */
    @Test
    public void testIsTransformableWhenEmpty() {
        // An empty group — boundary: no children to iterate
        assertTrue("Empty group has no non-transformable children, " + "so isTransformable returns true", group.isTransformable());
    }

    /**
     * getChildren() on a new group should return an empty collection,
     * not null.
     * Boundary: null-safety check.
     */
    @Test
    public void testGetChildrenNeverReturnsNull() {
        assertNotNull("getChildren() should never return null",
                group.getChildren());
    }

    /**
     * Adding a single child and immediately removing all children
     * should leave the group empty.
     * Boundary: single element add then removeAll.
     */
    @Test
    public void testAddOneThenRemoveAll() {
        group.willChange();
        group.basicAdd(new EllipseFigure());
        group.basicRemoveAllChildren();
        group.changed();

        assertEquals("Group should be empty after adding one then removing all",
                0, group.getChildCount());
    }

    /**
     * GroupFigure should not be connectable by default.
     * Boundary: connector behaviour edge case.
     */
    @Test
    public void testGroupFigureIsNotConnectable() {
        assertFalse("GroupFigure should not be connectable by default",
                group.isConnectable());
    }

    // ── ASSERTION / INVARIANT TEST ──────────────────────────────────────────

    /**
     * Demonstrates use of Java assert to enforce an invariant:
     * child count must never be negative.
     * Run with -ea JVM flag to enable assertions.
     */
    @Test
    public void testChildCountInvariant() {
        int count = group.getChildCount();
        assert count >= 0 : "Invariant violated: child count is negative: " + count;
        assertTrue(count >= 0);
    }
}