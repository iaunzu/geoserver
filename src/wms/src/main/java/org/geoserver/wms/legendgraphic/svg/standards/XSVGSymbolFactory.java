/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.standards;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.regex.Pattern;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;
import org.geoserver.wms.legendgraphic.svg.SVGSymbolFactory;
import org.geoserver.wms.legendgraphic.svg.SymbolizerUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;

public class XSVGSymbolFactory implements SVGSymbolFactory {

    @Override
    public Pattern getTypePattern() {
        return Pattern.compile(Pattern.quote("x"));
    }

    @Override
    public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
        Expression fontSize = graphic.getSize();
        Expression color = SymbolizerUtils.getColorExpression(mark);
        return new XSVGSymbol(fontSize, color, feature);
    }
}

class XSVGSymbol extends StandardSVGSymbol {

    public XSVGSymbol(Expression fontSize, Expression color, Feature feature) {
        super(
                SymbolizerUtils.toDoubleSize(fontSize, feature),
                SymbolizerUtils.toColor(color, feature));
    }

    @Override
    protected Shape createSimpleShape(double shiftx, double shifty, double w, double h) {
        // bbox
        // [shiftx, shifty]		[topCenter]		[shiftx+w, shifty]
        // [middleLeft]							[middleRight]
        // [shiftx, shifty+h]	[bottomCenter]	[shiftx+w, shifty+h]

        double topCenterX = shiftx + (w / 2);
        double topCenterY = shifty;
        // double bottomCenterX = shiftx + (w / 2);
        // double bottomCenterY = shifty + h;
        double middleLeftX = shiftx;
        double middleLeftY = shifty + (h / 2);
        // double middleRightX = shiftx + w;
        // double middleRightY = shifty + (h / 2);

        double rectWidth = w * 0.3;
        Rectangle2D.Double horiz =
                new Rectangle2D.Double(middleLeftX, middleLeftY - (rectWidth / 2), w, rectWidth);
        Rectangle2D.Double vert =
                new Rectangle2D.Double(topCenterX - (rectWidth / 2), topCenterY, rectWidth, h);
        Path2D path = new Path2D.Double();
        path.append(horiz, false);
        path.append(vert, false);
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(45), topCenterX, middleLeftY);
        return transform.createTransformedShape(path);
    }
}
