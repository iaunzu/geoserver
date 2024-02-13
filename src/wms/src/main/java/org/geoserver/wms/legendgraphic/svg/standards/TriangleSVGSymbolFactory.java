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

public class TriangleSVGSymbolFactory implements SVGSymbolFactory {

    @Override
    public Pattern getTypePattern() {
        return Pattern.compile(Pattern.quote("triangle"));
    }

    @Override
    public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
        Expression fontSize = graphic.getSize();
        Expression color = SymbolizerUtils.getColorExpression(mark);
        return new TriangleSVGSymbol(fontSize, color, feature);
    }
}

class TriangleSVGSymbol extends StandardSVGSymbol {

    public TriangleSVGSymbol(Expression fontSize, Expression color, Feature feature) {
        super(
                SymbolizerUtils.toDoubleSize(fontSize, feature),
                SymbolizerUtils.toColor(color, feature));
    }

    @Override
    protected Shape createSimpleShape(double shiftx, double shifty, double w, double h) {
        double tHeight = w * (Math.sqrt(3D / 4));
        double margin = (h - tHeight) / 2;
        Path2D path = new Path2D.Double();
        path.moveTo(shiftx, shifty + h - margin);
        path.lineTo(shiftx + w, shifty + h - margin);
        path.lineTo(shiftx + (w / 2), shifty + margin);
        path.closePath();
        return path;
    }
}
