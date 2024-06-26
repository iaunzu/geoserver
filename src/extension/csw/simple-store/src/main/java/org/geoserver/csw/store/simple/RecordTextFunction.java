/* (c) 2014  - 2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.csw.store.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geoserver.csw.records.CSWRecordDescriptor;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.InternalFunction;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Collects the contents of all SimpleLiteral attributes into a collection
 *
 * @author Andrea Aime - GeoSolutions
 */
public class RecordTextFunction extends FunctionExpressionImpl implements InternalFunction {

    public static FunctionName NAME = new FunctionNameImpl("cswRecordText");

    public RecordTextFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        Feature feature = (Feature) object;

        List<Object> list = new ArrayList<>(feature.getProperties().size());
        for (Property p : feature.getProperties()) {
            if (p.getDescriptor().getType() == CSWRecordDescriptor.SIMPLE_LITERAL) {
                Object value = ((ComplexAttribute) p).getProperty("value").getValue();
                list.add(value);
            }
        }

        return list;
    }

    @Override
    public InternalFunction duplicate(Expression... parameters) {
        RecordTextFunction func = new RecordTextFunction();
        func.setParameters(Arrays.asList(parameters));
        return func;
    }
}
