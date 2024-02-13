/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.awt.Color;
import java.awt.Font;
import java.util.Optional;
import org.geoserver.wms.icons.Icons;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Mark;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.filter.ConstantExpression;
import org.geotools.renderer.style.FontCache;

public class SymbolizerUtils {

    private SymbolizerUtils() {}

    public static Expression getColorExpression(Mark mark) {
        return Optional.ofNullable(mark.getFill())
                .map(Fill::getColor)
                .orElse(ConstantExpression.NULL);
    }

    public static Expression getColorExpression(TextSymbolizer symbolizer) {
        return Optional.ofNullable(symbolizer.getFill())
                .map(org.geotools.api.style.Fill::getColor)
                .orElse(ConstantExpression.NULL);
    }

    public static Expression getFontFamilyExpression(TextSymbolizer symbolizer) {
        return Optional.ofNullable(symbolizer.getFont())
                .map(org.geotools.api.style.Font::getFamily)
                .filter(f -> !f.isEmpty())
                .map(f -> f.get(0))
                .orElse(ConstantExpression.NULL);
    }

    public static Expression getFontSizeExpression(TextSymbolizer symbolizer) {
        return Optional.ofNullable(symbolizer.getFont())
                .map(org.geotools.api.style.Font::getSize)
                .orElse(ConstantExpression.NULL);
    }

    public static Color toColor(Expression color, Feature feature) {
        if (color != null) {
            String colorStr = color.evaluate(feature, String.class);
            if (colorStr != null) {
                try {
                    return Color.decode(colorStr);
                } catch (NumberFormatException e) {
                }
            }
        }
        return Color.BLACK;
    }

    public static double toDoubleSize(Expression fontSize, Feature feature) {
        if (fontSize == null) {
            return Icons.DEFAULT_SYMBOL_SIZE;
        }
        Double size = fontSize.evaluate(feature, Double.class);
        if (size == null) {
            return Icons.DEFAULT_SYMBOL_SIZE;
        }
        // defaultScale
        // https://docs.geoserver.org/latest/en/user/extensions/printing/configuration.html#legends-block
        return size / (72 * 0.28D / 25.4D);
    }

    public static Font toFont(Expression fontFamily, Feature feature) {
        if (fontFamily == null) {
            return null;
        }
        String font = fontFamily.evaluate(feature, String.class);
        if (font == null) {
            return null;
        }
        return FontCache.getDefaultInstance().getFont(font);
    }
}
