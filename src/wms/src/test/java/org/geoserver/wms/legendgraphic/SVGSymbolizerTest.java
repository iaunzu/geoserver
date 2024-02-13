package org.geoserver.wms.legendgraphic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.geoserver.wms.legendgraphic.svg.SVGSymbolizerBuilder;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Mark;
import org.geotools.api.style.Symbolizer;
import org.geotools.filter.ConstantExpression;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.FillImpl;
import org.geotools.styling.FontImpl;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.TextSymbolizerImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SVGSymbolizerTest {

    List<Symbolizer> symbolizers;

    @BeforeClass
    public static void init() throws FontFormatException, IOException {
        String fontPath = "/org/geoserver/wms/legendgraphic/Anarchist_Mustache.ttf";
        String fontFile = SVGSymbolizerTest.class.getResource(fontPath).getFile();
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFile));
        FontCache.getDefaultInstance().registerFont(font);
    }

    @Before
    public void setup() {
        symbolizers = new ArrayList<>();
    }

    @Test
    public void testMixedSymbolizer() {
        symbolizers.add(createPointSymbolizer("ttf://Anarchist Mustache#176", 48, "#000000"));
        symbolizers.add(createTextSymbolizer("[]", "Anarchist Mustache", 30, "#732600"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("mixed.svg")));
    }

    @Test
    public void testCircle() {
        symbolizers.add(createPointSymbolizer("circle", 30, "#00A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("circle.svg")));
    }

    @Test
    public void testSquare() {
        symbolizers.add(createPointSymbolizer("square", 30, "#A0A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("square.svg")));
    }

    @Test
    public void testCross() {
        symbolizers.add(createPointSymbolizer("cross", 30, "#A0A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("cross.svg")));
    }

    @Test
    public void testTriangle() {
        symbolizers.add(createPointSymbolizer("triangle", 30, "#A0A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("triangle.svg")));
    }

    @Test
    public void testStar() {
        symbolizers.add(createPointSymbolizer("star", 30, "#A0A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        System.out.println(new String(svg));
        assertThat(svg, is(readResult("star.svg")));
    }

    @Test
    public void testX() {
        symbolizers.add(createPointSymbolizer("x", 30, "#A0A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg, is(readResult("x.svg")));
    }

    @Test
    public void testUnsupported() {
        symbolizers.add(createPointSymbolizer("unsupported", 30, "#00A0A0"));
        SVGSymbolizerBuilder builder = new SVGSymbolizerBuilder();
        builder.addAll(symbolizers);
        byte[] svg = builder.build();
        assertThat(svg.length, is(0));
    }

    private Symbolizer createTextSymbolizer(
            String text, String fontFamily, int size, String color) {
        PublicTextSymbolizer symbolizer = new PublicTextSymbolizer();
        symbolizer.setLabel(ConstantExpression.constant(text));
        symbolizer.getFill().setColor(ConstantExpression.constant(color));
        PublicFont font = new PublicFont();
        font.setSize(ConstantExpression.constant(size));
        font.getFamily().add(ConstantExpression.constant(fontFamily));
        symbolizer.setFont(font);
        return symbolizer;
    }

    private Symbolizer createPointSymbolizer(String wkn, int size, String color) {
        PublicPointSymbolizer symbolizer = new PublicPointSymbolizer();
        GraphicImpl graphic = new GraphicImpl(null);
        Mark mark = new MarkImpl(wkn);
        Fill fill = new FillImpl(null);
        fill.setColor(ConstantExpression.constant(color));
        mark.setFill(fill);
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(ConstantExpression.constant(size));
        symbolizer.setGraphic(graphic);
        return symbolizer;
    }

    private byte[] readResult(String filename) {
        String resource = "/org/geoserver/wms/legendgraphic/results/" + filename;
        try (InputStream is = getClass().getResourceAsStream(resource)) {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            return null;
        }
    }

    static class PublicTextSymbolizer extends TextSymbolizerImpl {}

    static class PublicPointSymbolizer extends PointSymbolizerImpl {}

    static class PublicFont extends FontImpl {}
}
