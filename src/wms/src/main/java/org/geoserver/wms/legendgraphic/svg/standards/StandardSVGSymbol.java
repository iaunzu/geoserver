/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.standards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import org.apache.batik.svggen.SVGGraphics2D;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;

public abstract class StandardSVGSymbol implements SVGSymbol {

    private double x;
    private double y;
    private double width;
    private double height;
    private Color color;

    protected abstract Shape createSimpleShape(double shiftx, double shifty, double w, double y);

    protected StandardSVGSymbol(double size, Color color) {
        this(0, 0, size, size, color);
    }

    protected StandardSVGSymbol(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public Dimension getCanvasSize() {
        Dimension dimension = new Dimension();
        dimension.setSize(width, height);
        return dimension;
    }

    @Override
    public void paint(SVGGraphics2D svgGenerator) {
        Dimension canvasSize = svgGenerator.getSVGCanvasSize();
        double centerX = (double) canvasSize.width / 2;
        double centerY = (double) canvasSize.height / 2;

        double symbolCenterX = x + width / 2;
        double symbolCenterY = y + height / 2;
        double shiftx = centerX - symbolCenterX;
        double shifty = centerY - symbolCenterY;

        Color colorP = svgGenerator.getColor();
        svgGenerator.setColor(color);
        svgGenerator.fill(createSimpleShape(shiftx, shifty, width, height));
        svgGenerator.setColor(colorP);
    }
}
