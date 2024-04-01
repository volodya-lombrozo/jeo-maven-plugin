package org.eolang.jeo.annotations;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Annotations application.
 * @since 0.3
 */
@JeoAnnotation(
    name = "example",
    value = 496,
    required = true,
    color = MyEnum.GREEN,
    tags = {"tag1", "tag2"},
    ints = {1, 2, 3},
    longs = {10L, 20L, 30L},
    doubles = {1.0, 2.0, 3.0},
    floats = {1.0f, 2.0f, 3.0f},
    classes = {AnnotationsApplication.class},
    nested = @NestedAnnotation(name = "single-nested"),
    nestedArray = {
        @NestedAnnotation(name = "nested1"),
        @NestedAnnotation(name = "nested2")
    }
)
public class AnnotationsApplication {

    /**
     * Main method.
     * @param args Command line arguments
     * @todo #531:90min Check default values for annotation properties.
     *  We still encounter some problems with annotation processing.
     *  Especially with Autowired annotation from Spring Framework.
     *  It's relatively simple annotation, but it's not processed correctly.
     */
    public static void main(String[] args) {
        Class<AnnotationsApplication> clazz = AnnotationsApplication.class;
        if (clazz.isAnnotationPresent(JeoAnnotation.class)) {
            JeoAnnotation annotation = clazz.getAnnotation(JeoAnnotation.class);
            String name = annotation.name();
            int value = annotation.value();
            MyEnum color = annotation.color();
            String[] tags = annotation.tags();
            int[] ints = annotation.ints();
            long[] longs = annotation.longs();
            double[] doubles = annotation.doubles();
            float[] floats = annotation.floats();
            if (!name.equals("example")) {
                throw new IllegalStateException("name is not 'example'");
            }
            if (value != 496) {
                throw new IllegalStateException("value is not 496");
            }
            if (!annotation.required()) {
                throw new IllegalStateException("required is not true");
            }
            if (color != MyEnum.GREEN) {
                throw new IllegalStateException("color is not GREEN");
            }
            if (!Arrays.equals(tags, new String[]{"tag1", "tag2"})) {
                throw new IllegalStateException("tags are not ['tag1', 'tag2']");
            }
            if (!Arrays.equals(ints, new int[]{1, 2, 3})) {
                throw new IllegalStateException("ints are not [1, 2, 3]");
            }
            if (!Arrays.equals(longs, new long[]{10L, 20L, 30L})) {
                throw new IllegalStateException("longs are not [10L, 20L, 30L]");
            }
            if (!Arrays.equals(doubles, new double[]{1.0, 2.0, 3.0})) {
                throw new IllegalStateException("doubles are not [1.0, 2.0, 3.0]");
            }
            if (!Arrays.equals(floats, new float[]{1.0f, 2.0f, 3.0f})) {
                throw new IllegalStateException("floats are not [1.0f, 2.0f, 3.0f]");
            }
            if (!Arrays.equals(
                annotation.classes(), new Class<?>[]{AnnotationsApplication.class})) {
                throw new IllegalStateException("classes are not [AnnotationsApplication.class]");
            }
            if (annotation.nested() == null) {
                throw new IllegalStateException("nested is null");
            }
            if (annotation.nestedArray().length != 2) {
                throw new IllegalStateException("nestedArray length is not 2");
            }
            final boolean methodAnnotation = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(JeoMethodAnnotation.class))
                .map(method -> method.getAnnotation(JeoMethodAnnotation.class))
                .allMatch(JeoMethodAnnotation::required);
            if (!methodAnnotation) {
                throw new IllegalStateException("Method annotation is not present");
            }
            System.out.println("Annotations test passed successfully!");
        } else {
            throw new IllegalStateException(
                "JeoAnnotation not present on class AnnotationsApplication"
            );
        }
    }

    @JeoMethodAnnotation(required = true)
    public static void annotatedMethod() {
        // This method is annotated with JeoMethodAnnotation
    }

    @JeoMethodAnnotation
    public static void annotatedMethodWithDefaultValue() {
        // This method is annotated with JeoMethodAnnotation
    }
}
