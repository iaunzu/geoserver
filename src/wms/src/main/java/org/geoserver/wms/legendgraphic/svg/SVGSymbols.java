/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.util.Optional;
import org.geoserver.wms.legendgraphic.svg.ttf.TTFSVGSymbol;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;

public class SVGSymbols {

    private SVGSymbols() {}

    public static Optional<SVGSymbol> createSVGSymbol(Symbolizer symbolizer, Feature feature) {
        if (symbolizer instanceof PointSymbolizer) {
            return createSVGSymbol((PointSymbolizer) symbolizer, feature);
        } else if (symbolizer instanceof LineSymbolizer) {
            // return createSVGSymbol((LineSymbolizer) symbolizer);
        } else if (symbolizer instanceof PolygonSymbolizer) {
            // return createSVGSymbol((PolygonSymbolizer) symbolizer);
        } else if (symbolizer instanceof RasterSymbolizer) {
            // return createSVGSymbol((RasterSymbolizer) symbolizer);
        } else if (symbolizer instanceof TextSymbolizer) {
            return createSVGSymbol((TextSymbolizer) symbolizer, feature);
        } else {
            // LOGGER.warning("unknown symbolizer type " + symbolizer.getClass().getName());
        }
        return Optional.empty();
    }

    static Optional<SVGSymbol> createSVGSymbol(PointSymbolizer symbolizer, Feature feature) {
        return createSVGSymbol(symbolizer.getGraphic(), feature);
    }

    static Optional<SVGSymbol> createSVGSymbol(Graphic graphic, Feature feature) {
        for (GraphicalSymbol gSymbol : graphic.graphicalSymbols()) {
            if (gSymbol instanceof Mark) {
                Mark mark = (Mark) gSymbol;
                Expression wknExpr = mark.getWellKnownName();
                if (wknExpr != null) {
                    String wkn = wknExpr.evaluate(feature, String.class);
                    SVGSymbolFactory factory = SVGSymbolFactoryProvider.findFactory(wkn);
                    SVGSymbol svgSymbol = factory.createSymbol(mark, graphic, feature);
                    if (svgSymbol != null) {
                        return Optional.of(svgSymbol);
                    }
                }
            }
            // continue until a symbol can be represented
        }
        return Optional.empty();
    }

    static Optional<SVGSymbol> createSVGSymbol(TextSymbolizer symbolizer, Feature feature) {
        if (symbolizer.getGraphic() != null) {
            return createSVGSymbol(symbolizer.getGraphic(), feature);
        }
        Expression text = symbolizer.getLabel();
        Expression fontFamily = SymbolizerUtils.getFontFamilyExpression(symbolizer);
        Expression fontSize = SymbolizerUtils.getFontSizeExpression(symbolizer);
        Expression color = SymbolizerUtils.getColorExpression(symbolizer);
        return Optional.ofNullable(TTFSVGSymbol.create(text, fontFamily, fontSize, color, feature));
    }
}
