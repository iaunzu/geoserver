/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg.ttf;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.geoserver.wms.legendgraphic.svg.SVGSymbol;
import org.geoserver.wms.legendgraphic.svg.SVGSymbolFactory;
import org.geoserver.wms.legendgraphic.svg.SymbolizerUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;
import org.geotools.filter.ConstantExpression;

public class TTFSVGSymbolFactory implements SVGSymbolFactory {

    @Override
    public Pattern getTypePattern() {
        return Pattern.compile("^ttf://.+#\\d+$");
    }

    @Override
    public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
        String wkn = mark.getWellKnownName().evaluate(feature, String.class);
        String codePoint = StringUtils.substringAfterLast(wkn, "#");
        int character = NumberUtils.toInt(StringUtils.removeStart(codePoint, "0x"));
        if (character == 0) {
            return null;
        }
        String fontFamilyName = StringUtils.substringBetween(wkn, "ttf://", "#");
        Expression text = ConstantExpression.constant(Character.toString(character));
        Expression fontFamily = ConstantExpression.constant(fontFamilyName);
        Expression fontSize = graphic.getSize();
        Expression color = SymbolizerUtils.getColorExpression(mark);
        return TTFSVGSymbol.create(text, fontFamily, fontSize, color, feature);
    }
}
