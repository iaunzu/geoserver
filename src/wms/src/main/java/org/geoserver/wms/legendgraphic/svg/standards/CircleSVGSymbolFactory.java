/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.standards;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.regex.Pattern;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;
import org.geoserver.wms.legendgraphic.svg.SVGSymbolFactory;
import org.geoserver.wms.legendgraphic.svg.SymbolizerUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;

public class CircleSVGSymbolFactory implements SVGSymbolFactory {

    @Override
    public Pattern getTypePattern() {
        return Pattern.compile(Pattern.quote("circle"));
    }

    @Override
    public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
        Expression fontSize = graphic.getSize();
        Expression color = SymbolizerUtils.getColorExpression(mark);
        return new CircleSVGSymbol(fontSize, color, feature);
    }
}

class CircleSVGSymbol extends StandardSVGSymbol {

    public CircleSVGSymbol(Expression fontSize, Expression color, Feature feature) {
        super(
                SymbolizerUtils.toDoubleSize(fontSize, feature),
                SymbolizerUtils.toColor(color, feature));
    }

    @Override
    protected Shape createSimpleShape(double shiftx, double shifty, double w, double h) {
        return new Ellipse2D.Double(shiftx, shifty, w, h);
    }
}
