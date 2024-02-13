/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic.svg;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.geotools.api.feature.Feature;
import org.geotools.api.style.Symbolizer;
import org.w3c.dom.Document;

public class SVGSymbolizerBuilder {

    private List<SVGSymbol> symbols;
    private Feature feature;

    public SVGSymbolizerBuilder() {
        this(null);
    }

    public SVGSymbolizerBuilder(Feature feature) {
        this.symbols = new ArrayList<>();
        this.feature = feature;
    }

    public boolean isEmpty() {
        return symbols.isEmpty();
    }

    public SVGSymbolizerBuilder addAll(List<Symbolizer> symbolizers) {
        if (CollectionUtils.isNotEmpty(symbolizers)) {
            for (Symbolizer symbolizer : symbolizers) {
                add(symbolizer);
            }
        }
        return this;
    }

    public SVGSymbolizerBuilder add(Symbolizer symbolizer) {
        SVGSymbols.createSVGSymbol(symbolizer, feature).ifPresent(this.symbols::add);
        return this;
    }

    public byte[] build() {
        if (CollectionUtils.isEmpty(symbols)) {
            return new byte[0];
        }
        SVGGraphics2D svgGenerator = newSVGGraphics2D();
        for (SVGSymbol symbol : symbols) {
            expandSVGCanvas(svgGenerator, symbol);
        }
        for (SVGSymbol symbol : symbols) {
            symbol.paint(svgGenerator);
        }
        return write(svgGenerator);
    }

    protected void expandSVGCanvas(SVGGraphics2D svgGenerator, SVGSymbol symbol) {
        Dimension canvasSize = symbol.getCanvasSize();
        Dimension svgCanvasSize = svgGenerator.getSVGCanvasSize();
        if (svgCanvasSize == null) {
            svgGenerator.setSVGCanvasSize(canvasSize);
        } else {
            svgCanvasSize.width = Math.max(svgCanvasSize.width, canvasSize.width);
            svgCanvasSize.height = Math.max(svgCanvasSize.height, canvasSize.height);
        }
    }

    @Override
    public String toString() {
        return new String(this.build(), StandardCharsets.UTF_8);
    }

    protected byte[] write(SVGGraphics2D svgGenerator) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                Writer writer = new OutputStreamWriter(os)) {
            svgGenerator.stream(writer);
            return os.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    protected SVGGraphics2D newSVGGraphics2D() {
        Document doc =
                SVGDOMImplementation.getDOMImplementation()
                        .createDocument(
                                SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG, null);
        SVGGeneratorContext ctxt = SVGGeneratorContext.createDefault(doc);
        ctxt.setComment(null);
        return new SVGGraphics2D(ctxt, true);
    }
}
