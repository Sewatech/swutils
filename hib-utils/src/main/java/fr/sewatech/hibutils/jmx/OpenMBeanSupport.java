package fr.sewatech.hibutils.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.openmbean.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class OpenMBeanSupport {
    private static final Logger logger = LoggerFactory.getLogger(HibernateStatistics.class);

    private static final SimpleType[] SUPPORTED_SIMPLE_TYPES = {SimpleType.LONG, SimpleType.BOOLEAN, SimpleType.STRING};
    private static final String NAME_KEY = "_";

    CompositeData toCompositeData(Object source, String name) {
        try {
            Map<String, OpenType> properties = getProperties(source);

            CompositeType compositeType = toCompositeType(source, properties);
            int size = properties.size();
            Map<String, Object> values = getPropertyValues(source, properties.keySet().toArray(new String[size]));
            values.putIfAbsent(NAME_KEY, name);
            return new CompositeDataSupport(
                    compositeType,
                    values
            );
        } catch (OpenDataException e) {
            throw new StatisticsException(e);
        }
    }

    TabularData toTabularData(Function<String, CompositeData> keyToComposite, String[] keys, String name) {

        List<CompositeData> list = Arrays.stream(keys)
                .map(keyToComposite)
                .collect(Collectors.toList());

        if (keys.length == 0 || list.isEmpty()) {
            return null;
        }

        try {
            TabularType tabularType = new TabularType(name, name, list.get(0).getCompositeType(), new String[]{NAME_KEY});

            return list.stream()
                    .collect(getTabularDataCollector(tabularType));

        } catch (OpenDataException e) {
            logger.warn("Problem", e);
            throw new StatisticsException(e);
        }

    }

    private CompositeType toCompositeType(Object statistics, Map<String, OpenType> properties) throws OpenDataException {
        String name = statistics.getClass().getSimpleName();
        properties.putIfAbsent(NAME_KEY, SimpleType.STRING);
        int size = properties.size();
        String[] itemNames = properties.keySet().toArray(new String[size]);
        return new CompositeType(name, name, itemNames, itemNames, properties.values().toArray(new OpenType[size]));
    }

    private Collector<CompositeData, TabularData, TabularData> getTabularDataCollector(TabularType tabularType) {
        return Collector.of(
                () -> new TabularDataSupport(tabularType),
                (tabularData, value) -> tabularData.put(value),
                (left, right) -> {
                    left.putAll(right.values().toArray(new CompositeData[0]));
                    return left;
                });
    }

    private Map<String, OpenType> getProperties(Object obj) {
        Map<String, OpenType> result = new HashMap<>();

        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (isGetter(method)) {
                String prop = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4, methodName.length());

                try {
                    Object propertyValue = method.invoke(obj);
                    for (SimpleType simpleType : SUPPORTED_SIMPLE_TYPES) {
                        if (simpleType.isValue(propertyValue)) {
                            result.put(prop, simpleType);
                            break;
                        } else {
                            ArrayType arrayType = ArrayType.getArrayType(simpleType);
                            if (arrayType.isValue(propertyValue)) {
                                result.put(prop, arrayType);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Problem when invoking " + methodName);
                }
            }
        }

        return result;
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") && (method.getName().length() > 3);
    }

    private Map<String, Object> getPropertyValues(Object obj, String[] properties) {
        Map<String, Object> result = new HashMap<>();

        for (String property : properties) {
            try {
                String methodName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1, property.length());
                Method method = obj.getClass().getMethod(methodName);
                result.put(property, method.invoke(obj));
            } catch (Exception e) {
                if (!property.equals(NAME_KEY)) {
                    logger.warn("Problem when reading property " + property);
                }
            }
        }
        return result;
    }

}
