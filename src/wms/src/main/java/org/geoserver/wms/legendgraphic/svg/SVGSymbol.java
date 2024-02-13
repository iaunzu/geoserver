/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.awt.Dimension;
import org.apache.batik.svggen.SVGGraphics2D;

public interface SVGSymbol {

    Dimension getCanvasSize();

    void paint(SVGGraphics2D svgGenerator);
}
