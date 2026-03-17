/*
 * Minimal AbsoluteLayout implementation compatible with NetBeans GUI builder.
 */
package org.netbeans.lib.awtextra;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbsoluteLayout implements LayoutManager2, Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Component, AbsoluteConstraints> constraints = new HashMap<>();

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof AbsoluteConstraints) {
            this.constraints.put(comp, (AbsoluteConstraints) constraints);
        } else if (constraints == null) {
            this.constraints.put(comp, new AbsoluteConstraints(0, 0, -1, -1));
        } else {
            throw new IllegalArgumentException("Constraints must be AbsoluteConstraints");
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, new AbsoluteConstraints(0, 0, -1, -1));
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        constraints.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            int width = 0;
            int height = 0;
            for (Component comp : parent.getComponents()) {
                if (!comp.isVisible()) {
                    continue;
                }
                AbsoluteConstraints c = constraints.get(comp);
                if (c == null) {
                    Dimension pref = comp.getPreferredSize();
                    width = Math.max(width, pref.width);
                    height = Math.max(height, pref.height);
                } else {
                    Dimension pref = comp.getPreferredSize();
                    int w = c.width >= 0 ? c.width : pref.width;
                    int h = c.height >= 0 ? c.height : pref.height;
                    width = Math.max(width, c.x + w);
                    height = Math.max(height, c.y + h);
                }
            }
            return new Dimension(width, height);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            int width = 0;
            int height = 0;
            for (Component comp : parent.getComponents()) {
                if (!comp.isVisible()) {
                    continue;
                }
                AbsoluteConstraints c = constraints.get(comp);
                Dimension min = comp.getMinimumSize();
                if (c == null) {
                    width = Math.max(width, min.width);
                    height = Math.max(height, min.height);
                } else {
                    int w = c.width >= 0 ? c.width : min.width;
                    int h = c.height >= 0 ? c.height : min.height;
                    width = Math.max(width, c.x + w);
                    height = Math.max(height, c.y + h);
                }
            }
            return new Dimension(width, height);
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            for (Component comp : parent.getComponents()) {
                AbsoluteConstraints c = constraints.get(comp);
                if (c == null) {
                    continue;
                }
                Dimension pref = comp.getPreferredSize();
                int w = c.width >= 0 ? c.width : pref.width;
                int h = c.height >= 0 ? c.height : pref.height;
                comp.setBounds(new Rectangle(c.x, c.y, w, h));
            }
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        // No cached data to clear.
    }
}
