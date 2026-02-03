package com.crashinvaders.vfx.common.lml;

import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.AbstractLmlParser;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommonLmlParserBuilderTest {

    @Test
    public void testGetInstanceOfParser() {
        CommonLmlParserBuilder builder = new CommonLmlParserBuilder();
        LmlData lmlData = mock(LmlData.class);

        AbstractLmlParser parser = builder.getInstanceOfParser(lmlData);

        assertNotNull(parser);
        assertTrue(parser instanceof CommonLmlParser);
        assertEquals(lmlData, parser.getData());
    }

    @Test
    public void testConstructorWithLmlData() {
        LmlData lmlData = mock(LmlData.class);
        CommonLmlParserBuilder builder = new CommonLmlParserBuilder(lmlData);

        LmlParser parser = builder.build();

        assertNotNull(parser);
        assertTrue(parser instanceof CommonLmlParser);
        assertEquals(lmlData, ((CommonLmlParser) parser).getData());
    }
}
