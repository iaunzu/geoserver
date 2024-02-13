/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.util.ServiceLoader;
import java.util.regex.Pattern;
import org.geotools.api.feature.Feature;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;

public class SVGSymbolFactoryProvider {

    private SVGSymbolFactoryProvider() {}

    private static ServiceLoader<SVGSymbolFactory> factories;

    public static SVGSymbolFactory findFactory(String symbolType) {
        return findFactory(symbolType, false);
    }

    public static SVGSymbolFactory findFactory(String symbolType, boolean strict) {
        for (SVGSymbolFactory f : loadFactories()) {
            if (f.getTypePattern().matcher(symbolType).matches()) {
                return f;
            }
        }
        if (strict) {
            throw new IllegalArgumentException("Cannot find service for " + symbolType);
        }
        return new DummySymbolFactory();
    }

    private static ServiceLoader<SVGSymbolFactory> loadFactories() {
        if (factories == null) {
            factories = ServiceLoader.load(SVGSymbolFactory.class);
        }
        return factories;
    }

    static class DummySymbolFactory implements SVGSymbolFactory {
        @Override
        public Pattern getTypePattern() {
            return Pattern.compile("");
        }

        @Override
        public SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature) {
            return null;
        }
    }
}
