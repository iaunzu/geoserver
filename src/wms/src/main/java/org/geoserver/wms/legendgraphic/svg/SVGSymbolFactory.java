/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.geotools.api.feature.Feature;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;

public interface SVGSymbolFactory {

    Pattern getTypePattern();

    @Nullable
    SVGSymbol createSymbol(Mark mark, Graphic graphic, Feature feature);
}
