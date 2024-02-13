/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.ttf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import org.apache.batik.svggen.SVGGraphics2D;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;
import org.geoserver.wms.legendgraphic.svg.SymbolizerUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;

public class TTFSVGSymbol implements SVGSymbol {

    private GlyphVector glyphVector;
    private Color color = Color.BLACK;
    private static final FontRenderContext DEFAULT_FRC =
            new FontRenderContext(new AffineTransform(), false, false);

    private TTFSVGSymbol(GlyphVector glyphVector, Color color) {
        this.glyphVector = glyphVector;
        this.color = color;
    }

    public static @Nullable TTFSVGSymbol create(
            Expression text,
            Expression fontFamily,
            Expression fontSize,
            Expression color,
            Feature feature) {
        java.awt.Font baseFont = SymbolizerUtils.toFont(fontFamily, feature);
        if (baseFont == null) {
            return null;
        }
        float fsize = (float) SymbolizerUtils.toDoubleSize(fontSize, feature);
        if (fsize != 0) {
            baseFont = baseFont.deriveFont(fsize);
        }
        String mText = text.evaluate(feature, String.class);
        if (mText == null) {
            return null;
        }
        String fixedText = fixText(mText, baseFont);
        if (fixedText.isEmpty()) {
            return null;
        }
        GlyphVector glyphVector = baseFont.createGlyphVector(DEFAULT_FRC, fixedText);
        Color glyphColor = SymbolizerUtils.toColor(color, feature);
        return new TTFSVGSymbol(glyphVector, glyphColor);
    }

    private static String fixText(String text, final java.awt.Font ffont) {
        return text.chars()
                .map(charFixer(ffont))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static IntUnaryOperator charFixer(java.awt.Font ffont) {
        return (int c) -> shouldShift(c, ffont, 0xF000) ? c + 0xF000 : c;
    }

    private static boolean shouldShift(int c, java.awt.Font ffont, int shift) {
        return !ffont.canDisplay(c) && ffont.canDisplay(c + shift);
    }

    @Override
    public Dimension getCanvasSize() {
        Rectangle2D visualBounds = glyphVector.getVisualBounds();
        Dimension dimension = new Dimension();
        dimension.setSize(visualBounds.getWidth(), visualBounds.getHeight());
        return dimension;
    }

    @Override
    public void paint(SVGGraphics2D svgGenerator) {
        Dimension canvasSize = svgGenerator.getSVGCanvasSize();
        double centerX = (double) canvasSize.width / 2;
        double centerY = (double) canvasSize.height / 2;

        Rectangle2D visualBounds = glyphVector.getVisualBounds();
        double glyphCenterX = visualBounds.getX() + visualBounds.getWidth() / 2;
        double glyphCenterY = visualBounds.getY() + visualBounds.getHeight() / 2;
        double x = centerX - glyphCenterX;
        double y = centerY - glyphCenterY;
        Color colorP = svgGenerator.getColor();
        svgGenerator.setColor(color);
        svgGenerator.drawGlyphVector(glyphVector, (float) x, (float) y);
        svgGenerator.setColor(colorP);
    }
}
