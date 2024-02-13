/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.standards;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.regex.Pattern;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;
import org.geoserver.wms.legendgraphic.svg.SVGSymbolFactory;
import org.geoserver.wms.legendgraphic.svg.SymbolizerUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;

public class StarSVGSymbolFactory implements SVGSymbolFactory {

    @Override
    public Pattern getTypePattern() {
        return Pattern.compile(Pattern.quote("star"));
    }

    @Override
    public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
        Expression fontSize = graphic.getSize();
        Expression color = SymbolizerUtils.getColorExpression(mark);
        return new StarSVGSymbol(fontSize, color, feature);
    }
}

class StarSVGSymbol extends StandardSVGSymbol {

    public StarSVGSymbol(Expression fontSize, Expression color, Feature feature) {
        super(
                SymbolizerUtils.toDoubleSize(fontSize, feature),
                SymbolizerUtils.toColor(color, feature));
    }

    @Override
    protected Shape createSimpleShape(double shiftx, double shifty, double w, double h) {
        return createStar(shiftx + (w / 2), shifty + (h / 2), w * 0.2, w * 0.5, 5, -Math.PI / 10);
    }

    protected static Shape createStar(
            double centerX,
            double centerY,
            double innerRadius,
            double outerRadius,
            int numRays,
            double startAngleRad) {
        Path2D path = new Path2D.Double();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++) {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double radius = (i % 2) == 0 ? outerRadius : innerRadius;
            double relX = Math.cos(angleRad) * radius;
            double relY = Math.sin(angleRad) * radius;
            if (i == 0) {
                path.moveTo(centerX + relX, centerY + relY);
            } else {
                path.lineTo(centerX + relX, centerY + relY);
            }
        }
        path.closePath();
        return path;
    }
}
