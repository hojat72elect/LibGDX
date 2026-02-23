package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.utils.random.ConstantDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantLongDistribution;
import com.badlogic.gdx.ai.utils.random.DoubleDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.ai.utils.random.GaussianDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.GaussianFloatDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.ai.utils.random.LongDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularFloatDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularLongDistribution;
import com.badlogic.gdx.ai.utils.random.UniformDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.UniformFloatDistribution;
import com.badlogic.gdx.ai.utils.random.UniformIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.UniformLongDistribution;
import com.badlogic.gdx.utils.ObjectMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Unit tests for DistributionAdapters
 */
public class DistributionAdaptersTest {

    private DistributionAdapters adapters;

    @Before
    public void setUp() {
        adapters = new DistributionAdapters();
    }

    @After
    public void tearDown() {
        adapters = null;
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull("DistributionAdapters should be created", adapters);
        try {
            Field mapField = DistributionAdapters.class.getDeclaredField("map");
            mapField.setAccessible(true);
            ObjectMap<?, ?> map = (ObjectMap<?, ?>) mapField.get(adapters);
            Assert.assertNotNull("Map should be initialized", map);

            Field typeMapField = DistributionAdapters.class.getDeclaredField("typeMap");
            typeMapField.setAccessible(true);
            ObjectMap<?, ?> typeMap = (ObjectMap<?, ?>) typeMapField.get(adapters);
            Assert.assertNotNull("TypeMap should be initialized", typeMap);
        } catch (Exception e) {
            Assert.fail("Failed to access private fields: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorPopulatesAdapters() {
        // Test that constructor populates adapters from static ADAPTERS
        try {
            Field mapField = DistributionAdapters.class.getDeclaredField("map");
            mapField.setAccessible(true);
            ObjectMap<?, ?> map = (ObjectMap<?, ?>) mapField.get(adapters);
            Assert.assertTrue("Map should contain adapters", map.size > 0);

            Field typeMapField = DistributionAdapters.class.getDeclaredField("typeMap");
            typeMapField.setAccessible(true);
            ObjectMap<?, ?> typeMap = (ObjectMap<?, ?>) typeMapField.get(adapters);
            Assert.assertTrue("TypeMap should contain adapters", typeMap.size > 0);
        } catch (Exception e) {
            Assert.fail("Failed to access private fields: " + e.getMessage());
        }
    }

    @Test
    public void testConstantDoubleDistribution() {
        DoubleDistribution result = adapters.toDistribution("constant,5.5", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantDoubleDistribution", result instanceof ConstantDoubleDistribution);
        Assert.assertEquals("Constant double should have correct value", 5.5, result.nextDouble(), 0.001);
    }

    @Test
    public void testConstantFloatDistribution() {
        FloatDistribution result = adapters.toDistribution("constant,3.14f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantFloatDistribution", result instanceof ConstantFloatDistribution);
        Assert.assertEquals("Constant float should have correct value", 3.14f, result.nextFloat(), 0.001f);
    }

    @Test
    public void testConstantIntegerDistribution() {
        IntegerDistribution result = adapters.toDistribution("constant,42", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantIntegerDistribution", result instanceof ConstantIntegerDistribution);
        Assert.assertEquals("Constant integer should have correct value", 42, result.nextInt());
    }

    @Test
    public void testConstantLongDistribution() {
        LongDistribution result = adapters.toDistribution("constant,1000000000", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantLongDistribution", result instanceof ConstantLongDistribution);
        Assert.assertEquals("Constant long should have correct value", 1000000000L, result.nextLong());
    }

    @Test
    public void testGaussianDoubleDistribution() {
        DoubleDistribution result = adapters.toDistribution("gaussian,0.0,1.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be GaussianDoubleDistribution", result instanceof GaussianDoubleDistribution);
        GaussianDoubleDistribution gaussian = (GaussianDoubleDistribution) result;
        Assert.assertEquals("Gaussian double should have correct mean", 0.0, gaussian.getMean(), 0.001);
        Assert.assertEquals("Gaussian double should have correct standard deviation", 1.0, gaussian.getStandardDeviation(), 0.001);
    }

    @Test
    public void testGaussianFloatDistribution() {
        FloatDistribution result = adapters.toDistribution("gaussian,5.0f,2.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be GaussianFloatDistribution", result instanceof GaussianFloatDistribution);
        GaussianFloatDistribution gaussian = (GaussianFloatDistribution) result;
        Assert.assertEquals("Gaussian float should have correct mean", 5.0f, gaussian.getMean(), 0.001f);
        Assert.assertEquals("Gaussian float should have correct standard deviation", 2.0f, gaussian.getStandardDeviation(), 0.001f);
    }

    @Test
    public void testTriangularDoubleDistributionOneArg() {
        DoubleDistribution result = adapters.toDistribution("triangular,10.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularDoubleDistribution", result instanceof TriangularDoubleDistribution);
        TriangularDoubleDistribution triangular = (TriangularDoubleDistribution) result;
        Assert.assertEquals("Triangular double should have correct low", -10.0, triangular.getLow(), 0.001);
        Assert.assertEquals("Triangular double should have correct high", 10.0, triangular.getHigh(), 0.001);
        Assert.assertEquals("Triangular double should have correct mode", 0.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testTriangularDoubleDistributionTwoArgs() {
        DoubleDistribution result = adapters.toDistribution("triangular,0.0,20.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularDoubleDistribution", result instanceof TriangularDoubleDistribution);
        TriangularDoubleDistribution triangular = (TriangularDoubleDistribution) result;
        Assert.assertEquals("Triangular double should have correct low", 0.0, triangular.getLow(), 0.001);
        Assert.assertEquals("Triangular double should have correct high", 20.0, triangular.getHigh(), 0.001);
        Assert.assertEquals("Triangular double should have correct mode", 10.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testTriangularDoubleDistributionThreeArgs() {
        DoubleDistribution result = adapters.toDistribution("triangular,0.0,20.0,15.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularDoubleDistribution", result instanceof TriangularDoubleDistribution);
        TriangularDoubleDistribution triangular = (TriangularDoubleDistribution) result;
        Assert.assertEquals("Triangular double should have correct low", 0.0, triangular.getLow(), 0.001);
        Assert.assertEquals("Triangular double should have correct high", 20.0, triangular.getHigh(), 0.001);
        Assert.assertEquals("Triangular double should have correct mode", 15.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testTriangularFloatDistributionOneArg() {
        FloatDistribution result = adapters.toDistribution("triangular,5.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularFloatDistribution", result instanceof TriangularFloatDistribution);
        TriangularFloatDistribution triangular = (TriangularFloatDistribution) result;
        Assert.assertEquals("Triangular float should have correct low", -5.0f, triangular.getLow(), 0.001f);
        Assert.assertEquals("Triangular float should have correct high", 5.0f, triangular.getHigh(), 0.001f);
        Assert.assertEquals("Triangular float should have correct mode", 0.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularFloatDistributionTwoArgs() {
        FloatDistribution result = adapters.toDistribution("triangular,0.0f,10.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularFloatDistribution", result instanceof TriangularFloatDistribution);
        TriangularFloatDistribution triangular = (TriangularFloatDistribution) result;
        Assert.assertEquals("Triangular float should have correct low", 0.0f, triangular.getLow(), 0.001f);
        Assert.assertEquals("Triangular float should have correct high", 10.0f, triangular.getHigh(), 0.001f);
        Assert.assertEquals("Triangular float should have correct mode", 5.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularFloatDistributionThreeArgs() {
        FloatDistribution result = adapters.toDistribution("triangular,0.0f,10.0f,7.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularFloatDistribution", result instanceof TriangularFloatDistribution);
        TriangularFloatDistribution triangular = (TriangularFloatDistribution) result;
        Assert.assertEquals("Triangular float should have correct low", 0.0f, triangular.getLow(), 0.001f);
        Assert.assertEquals("Triangular float should have correct high", 10.0f, triangular.getHigh(), 0.001f);
        Assert.assertEquals("Triangular float should have correct mode", 7.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularIntegerDistributionOneArg() {
        IntegerDistribution result = adapters.toDistribution("triangular,5", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularIntegerDistribution", result instanceof TriangularIntegerDistribution);
        TriangularIntegerDistribution triangular = (TriangularIntegerDistribution) result;
        Assert.assertEquals("Triangular integer should have correct low", -5, triangular.getLow());
        Assert.assertEquals("Triangular integer should have correct high", 5, triangular.getHigh());
        Assert.assertEquals("Triangular integer should have correct mode", 0.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularIntegerDistributionTwoArgs() {
        IntegerDistribution result = adapters.toDistribution("triangular,0,10", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularIntegerDistribution", result instanceof TriangularIntegerDistribution);
        TriangularIntegerDistribution triangular = (TriangularIntegerDistribution) result;
        Assert.assertEquals("Triangular integer should have correct low", 0, triangular.getLow());
        Assert.assertEquals("Triangular integer should have correct high", 10, triangular.getHigh());
        Assert.assertEquals("Triangular integer should have correct mode", 5.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularIntegerDistributionThreeArgs() {
        IntegerDistribution result = adapters.toDistribution("triangular,0,10,7.0", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularIntegerDistribution", result instanceof TriangularIntegerDistribution);
        TriangularIntegerDistribution triangular = (TriangularIntegerDistribution) result;
        Assert.assertEquals("Triangular integer should have correct low", 0, triangular.getLow());
        Assert.assertEquals("Triangular integer should have correct high", 10, triangular.getHigh());
        Assert.assertEquals("Triangular integer should have correct mode", 7.0f, triangular.getMode(), 0.001f);
    }

    @Test
    public void testTriangularLongDistributionOneArg() {
        LongDistribution result = adapters.toDistribution("triangular,1000", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularLongDistribution", result instanceof TriangularLongDistribution);
        TriangularLongDistribution triangular = (TriangularLongDistribution) result;
        Assert.assertEquals("Triangular long should have correct low", -1000L, triangular.getLow());
        Assert.assertEquals("Triangular long should have correct high", 1000L, triangular.getHigh());
        Assert.assertEquals("Triangular long should have correct mode", 0.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testTriangularLongDistributionTwoArgs() {
        LongDistribution result = adapters.toDistribution("triangular,0,2000", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularLongDistribution", result instanceof TriangularLongDistribution);
        TriangularLongDistribution triangular = (TriangularLongDistribution) result;
        Assert.assertEquals("Triangular long should have correct low", 0L, triangular.getLow());
        Assert.assertEquals("Triangular long should have correct high", 2000L, triangular.getHigh());
        Assert.assertEquals("Triangular long should have correct mode", 1000.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testTriangularLongDistributionThreeArgs() {
        LongDistribution result = adapters.toDistribution("triangular,0,2000,1500.0", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be TriangularLongDistribution", result instanceof TriangularLongDistribution);
        TriangularLongDistribution triangular = (TriangularLongDistribution) result;
        Assert.assertEquals("Triangular long should have correct low", 0L, triangular.getLow());
        Assert.assertEquals("Triangular long should have correct high", 2000L, triangular.getHigh());
        Assert.assertEquals("Triangular long should have correct mode", 1500.0, triangular.getMode(), 0.001);
    }

    @Test
    public void testUniformDoubleDistributionOneArg() {
        DoubleDistribution result = adapters.toDistribution("uniform,5.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformDoubleDistribution", result instanceof UniformDoubleDistribution);
        UniformDoubleDistribution uniform = (UniformDoubleDistribution) result;
        Assert.assertEquals("Uniform double should have correct low", 0.0, uniform.getLow(), 0.001);
        Assert.assertEquals("Uniform double should have correct high", 5.0, uniform.getHigh(), 0.001);
    }

    @Test
    public void testUniformDoubleDistributionTwoArgs() {
        DoubleDistribution result = adapters.toDistribution("uniform,0.0,10.0", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformDoubleDistribution", result instanceof UniformDoubleDistribution);
        UniformDoubleDistribution uniform = (UniformDoubleDistribution) result;
        Assert.assertEquals("Uniform double should have correct low", 0.0, uniform.getLow(), 0.001);
        Assert.assertEquals("Uniform double should have correct high", 10.0, uniform.getHigh(), 0.001);
    }

    @Test
    public void testUniformFloatDistributionOneArg() {
        FloatDistribution result = adapters.toDistribution("uniform,3.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformFloatDistribution", result instanceof UniformFloatDistribution);
        UniformFloatDistribution uniform = (UniformFloatDistribution) result;
        Assert.assertEquals("Uniform float should have correct low", 0.0f, uniform.getLow(), 0.001f);
        Assert.assertEquals("Uniform float should have correct high", 3.0f, uniform.getHigh(), 0.001f);
    }

    @Test
    public void testUniformFloatDistributionTwoArgs() {
        FloatDistribution result = adapters.toDistribution("uniform,0.0f,5.0f", FloatDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformFloatDistribution", result instanceof UniformFloatDistribution);
        UniformFloatDistribution uniform = (UniformFloatDistribution) result;
        Assert.assertEquals("Uniform float should have correct low", 0.0f, uniform.getLow(), 0.001f);
        Assert.assertEquals("Uniform float should have correct high", 5.0f, uniform.getHigh(), 0.001f);
    }

    @Test
    public void testUniformIntegerDistributionOneArg() {
        IntegerDistribution result = adapters.toDistribution("uniform,7", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformIntegerDistribution", result instanceof UniformIntegerDistribution);
        UniformIntegerDistribution uniform = (UniformIntegerDistribution) result;
        Assert.assertEquals("Uniform integer should have correct low", 0, uniform.getLow());
        Assert.assertEquals("Uniform integer should have correct high", 7, uniform.getHigh());
    }

    @Test
    public void testUniformIntegerDistributionTwoArgs() {
        IntegerDistribution result = adapters.toDistribution("uniform,1,10", IntegerDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformIntegerDistribution", result instanceof UniformIntegerDistribution);
        UniformIntegerDistribution uniform = (UniformIntegerDistribution) result;
        Assert.assertEquals("Uniform integer should have correct low", 1, uniform.getLow());
        Assert.assertEquals("Uniform integer should have correct high", 10, uniform.getHigh());
    }

    @Test
    public void testUniformLongDistributionOneArg() {
        LongDistribution result = adapters.toDistribution("uniform,1000", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformLongDistribution", result instanceof UniformLongDistribution);
        UniformLongDistribution uniform = (UniformLongDistribution) result;
        Assert.assertEquals("Uniform long should have correct low", 0L, uniform.getLow());
        Assert.assertEquals("Uniform long should have correct high", 1000L, uniform.getHigh());
    }

    @Test
    public void testUniformLongDistributionTwoArgs() {
        LongDistribution result = adapters.toDistribution("uniform,100,1000", LongDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be UniformLongDistribution", result instanceof UniformLongDistribution);
        UniformLongDistribution uniform = (UniformLongDistribution) result;
        Assert.assertEquals("Uniform long should have correct low", 100L, uniform.getLow());
        Assert.assertEquals("Uniform long should have correct high", 1000L, uniform.getHigh());
    }

    @Test
    public void testToStringConstantDouble() {
        ConstantDoubleDistribution dist = new ConstantDoubleDistribution(5.5);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format constant double correctly", "constant,5.5", result);
    }

    @Test
    public void testToStringConstantFloat() {
        ConstantFloatDistribution dist = new ConstantFloatDistribution(3.14f);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format constant float correctly", "constant,3.14", result);
    }

    @Test
    public void testToStringConstantInteger() {
        ConstantIntegerDistribution dist = new ConstantIntegerDistribution(42);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format constant integer correctly", "constant,42", result);
    }

    @Test
    public void testToStringConstantLong() {
        ConstantLongDistribution dist = new ConstantLongDistribution(1000000L);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format constant long correctly", "constant,1000000", result);
    }

    @Test
    public void testToStringGaussianDouble() {
        GaussianDoubleDistribution dist = new GaussianDoubleDistribution(0.0, 1.0);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format gaussian double correctly", "gaussian,0.0,1.0", result);
    }

    @Test
    public void testToStringGaussianFloat() {
        GaussianFloatDistribution dist = new GaussianFloatDistribution(5.0f, 2.0f);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format gaussian float correctly", "gaussian,5.0,2.0", result);
    }

    @Test
    public void testToStringTriangularDouble() {
        TriangularDoubleDistribution dist = new TriangularDoubleDistribution(0.0, 20.0, 15.0);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format triangular double correctly", "triangular,0.0,20.0,15.0", result);
    }

    @Test
    public void testToStringUniformDouble() {
        UniformDoubleDistribution dist = new UniformDoubleDistribution(0.0, 10.0);
        String result = adapters.toString(dist);
        Assert.assertEquals("toString should format uniform double correctly", "uniform,0.0,10.0", result);
    }

    @Test
    public void testAdd() {
        // Create a test adapter
        DistributionAdapters.Adapter<DoubleDistribution> testAdapter = new DistributionAdapters.DoubleAdapter<DoubleDistribution>("test") {
            @Override
            public DoubleDistribution toDistribution(String[] args) {
                return new ConstantDoubleDistribution(1.0);
            }

            @Override
            public String[] toParameters(DoubleDistribution distribution) {
                return new String[]{"1.0"};
            }
        };

        // Add the adapter
        adapters.add(DoubleDistribution.class, testAdapter);

        // Test that we can use the adapter by trying to create a distribution
        try {
            DoubleDistribution result = adapters.toDistribution("test,1.0", DoubleDistribution.class);
            Assert.assertNotNull("Should be able to create distribution with added adapter", result);
            Assert.assertTrue("Should be ConstantDoubleDistribution", result instanceof ConstantDoubleDistribution);
        } catch (Exception e) {
            Assert.fail("Should be able to use added adapter: " + e.getMessage());
        }
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testToDistributionMissingType() {
        adapters.toDistribution("", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testToDistributionInvalidType() {
        adapters.toDistribution("invalid,5.0", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testConstantDoubleTooManyArgs() {
        adapters.toDistribution("constant,5.0,extra", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testConstantFloatTooManyArgs() {
        adapters.toDistribution("constant,3.14f,extra", FloatDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testConstantIntegerTooManyArgs() {
        adapters.toDistribution("constant,42,extra", IntegerDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testConstantLongTooManyArgs() {
        adapters.toDistribution("constant,1000,extra", LongDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testGaussianDoubleTooManyArgs() {
        adapters.toDistribution("gaussian,0.0,1.0,extra", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testGaussianDoubleTooFewArgs() {
        adapters.toDistribution("gaussian,0.0", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testTriangularDoubleTooManyArgs() {
        adapters.toDistribution("triangular,0.0,20.0,15.0,extra", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testUniformDoubleTooManyArgs() {
        adapters.toDistribution("uniform,0.0,10.0,extra", DoubleDistribution.class);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testUniformDoubleTooFewArgs() {
        adapters.toDistribution("uniform", DoubleDistribution.class);
    }

    @Test
    public void testParseDoubleValid() {
        double result = DistributionAdapters.Adapter.parseDouble("5.5");
        Assert.assertEquals("Should parse double correctly", 5.5, result, 0.001);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testParseDoubleInvalid() {
        DistributionAdapters.Adapter.parseDouble("invalid");
    }

    @Test
    public void testParseFloatValid() {
        float result = DistributionAdapters.Adapter.parseFloat("3.14f");
        Assert.assertEquals("Should parse float correctly", 3.14f, result, 0.001f);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testParseFloatInvalid() {
        DistributionAdapters.Adapter.parseFloat("invalid");
    }

    @Test
    public void testParseIntegerValid() {
        int result = DistributionAdapters.Adapter.parseInteger("42");
        Assert.assertEquals("Should parse integer correctly", 42, result);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testParseIntegerInvalid() {
        DistributionAdapters.Adapter.parseInteger("invalid");
    }

    @Test
    public void testParseLongValid() {
        long result = DistributionAdapters.Adapter.parseLong("1000000");
        Assert.assertEquals("Should parse long correctly", 1000000L, result);
    }

    @Test(expected = DistributionAdapters.DistributionFormatException.class)
    public void testParseLongInvalid() {
        DistributionAdapters.Adapter.parseLong("invalid");
    }

    @Test
    public void testDistributionFormatExceptionDefaultConstructor() {
        DistributionAdapters.DistributionFormatException exception = new DistributionAdapters.DistributionFormatException();
        Assert.assertNull("Message should be null", exception.getMessage());
        Assert.assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testDistributionFormatExceptionMessageConstructor() {
        String message = "Test message";
        DistributionAdapters.DistributionFormatException exception = new DistributionAdapters.DistributionFormatException(message);
        Assert.assertEquals("Message should be set", message, exception.getMessage());
        Assert.assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testDistributionFormatExceptionMessageAndCauseConstructor() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause");
        DistributionAdapters.DistributionFormatException exception = new DistributionAdapters.DistributionFormatException(message, cause);
        Assert.assertEquals("Message should be set", message, exception.getMessage());
        Assert.assertEquals("Cause should be set", cause, exception.getCause());
    }

    @Test
    public void testDistributionFormatExceptionCauseConstructor() {
        Throwable cause = new RuntimeException("Cause");
        DistributionAdapters.DistributionFormatException exception = new DistributionAdapters.DistributionFormatException(cause);
        Assert.assertTrue("Message should contain cause toString", exception.getMessage().contains("Cause"));
        Assert.assertEquals("Cause should be set", cause, exception.getCause());
    }

    @Test
    public void testDoubleAdapter() {
        TestDoubleAdapter adapter = new TestDoubleAdapter("test");
        Assert.assertEquals("Category should be set", "test", adapter.category);
        Assert.assertEquals("Type should be set", DoubleDistribution.class, adapter.type);
    }

    @Test
    public void testFloatAdapter() {
        TestFloatAdapter adapter = new TestFloatAdapter("test");
        Assert.assertEquals("Category should be set", "test", adapter.category);
        Assert.assertEquals("Type should be set", FloatDistribution.class, adapter.type);
    }

    @Test
    public void testIntegerAdapter() {
        TestIntegerAdapter adapter = new TestIntegerAdapter("test");
        Assert.assertEquals("Category should be set", "test", adapter.category);
        Assert.assertEquals("Type should be set", IntegerDistribution.class, adapter.type);
    }

    @Test
    public void testLongAdapter() {
        TestLongAdapter adapter = new TestLongAdapter("test");
        Assert.assertEquals("Category should be set", "test", adapter.category);
        Assert.assertEquals("Type should be set", LongDistribution.class, adapter.type);
    }

    @Test
    public void testToDistributionWithWhitespace() {
        DoubleDistribution result = adapters.toDistribution(" constant , 5.5 ", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantDoubleDistribution", result instanceof ConstantDoubleDistribution);
        Assert.assertEquals("Should handle whitespace correctly", 5.5, result.nextDouble(), 0.001);
    }

    @Test
    public void testToDistributionWithTabs() {
        DoubleDistribution result = adapters.toDistribution("constant\t5.5", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be ConstantDoubleDistribution", result instanceof ConstantDoubleDistribution);
        Assert.assertEquals("Should handle tabs correctly", 5.5, result.nextDouble(), 0.001);
    }

    @Test
    public void testToDistributionWithMixedWhitespace() {
        DoubleDistribution result = adapters.toDistribution(" gaussian \t 0.0 , 1.0 ", DoubleDistribution.class);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be GaussianDoubleDistribution", result instanceof GaussianDoubleDistribution);
        GaussianDoubleDistribution gaussian = (GaussianDoubleDistribution) result;
        Assert.assertEquals("Should handle mixed whitespace correctly", 0.0, gaussian.getMean(), 0.001);
        Assert.assertEquals("Should handle mixed whitespace correctly", 1.0, gaussian.getStandardDeviation(), 0.001);
    }

    private static class TestDoubleAdapter extends DistributionAdapters.DoubleAdapter<ConstantDoubleDistribution> {
        public TestDoubleAdapter(String category) {
            super(category);
        }

        @Override
        public ConstantDoubleDistribution toDistribution(String[] args) {
            return new ConstantDoubleDistribution(1.0);
        }

        @Override
        public String[] toParameters(ConstantDoubleDistribution distribution) {
            return new String[]{"1.0"};
        }
    }

    private static class TestFloatAdapter extends DistributionAdapters.FloatAdapter<ConstantFloatDistribution> {
        public TestFloatAdapter(String category) {
            super(category);
        }

        @Override
        public ConstantFloatDistribution toDistribution(String[] args) {
            return new ConstantFloatDistribution(1.0f);
        }

        @Override
        public String[] toParameters(ConstantFloatDistribution distribution) {
            return new String[]{"1.0"};
        }
    }

    private static class TestIntegerAdapter extends DistributionAdapters.IntegerAdapter<ConstantIntegerDistribution> {
        public TestIntegerAdapter(String category) {
            super(category);
        }

        @Override
        public ConstantIntegerDistribution toDistribution(String[] args) {
            return new ConstantIntegerDistribution(1);
        }

        @Override
        public String[] toParameters(ConstantIntegerDistribution distribution) {
            return new String[]{"1"};
        }
    }

    private static class TestLongAdapter extends DistributionAdapters.LongAdapter<ConstantLongDistribution> {
        public TestLongAdapter(String category) {
            super(category);
        }

        @Override
        public ConstantLongDistribution toDistribution(String[] args) {
            return new ConstantLongDistribution(1L);
        }

        @Override
        public String[] toParameters(ConstantLongDistribution distribution) {
            return new String[]{"1"};
        }
    }
}
