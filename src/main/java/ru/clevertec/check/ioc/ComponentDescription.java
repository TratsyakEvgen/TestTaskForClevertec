package ru.clevertec.check.ioc;


import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;


public record ComponentDescription(Type[] types, Class<?> clazz, String qualifier, Object object) {

    public static ComponentDescriptionBuilder builder() {
        return new ComponentDescriptionBuilder();
    }

    public static class ComponentDescriptionBuilder {
        private Type[] types;
        private Class<?> clazz;
        private String qualifier;
        private Object object;

        ComponentDescriptionBuilder() {
        }

        public ComponentDescriptionBuilder types(Type[] types) {
            this.types = types;
            return this;
        }

        public ComponentDescriptionBuilder clazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public ComponentDescriptionBuilder qualifier(String qualifier) {
            this.qualifier = qualifier;
            return this;
        }

        public ComponentDescriptionBuilder object(Object object) {
            this.object = object;
            return this;
        }

        public ComponentDescription build() {
            return new ComponentDescription(this.types, this.clazz, this.qualifier, this.object);
        }

    }


    @Override
    public boolean equals(Object object1) {
        if (this == object1) return true;
        if (object1 == null || getClass() != object1.getClass()) return false;
        ComponentDescription that = (ComponentDescription) object1;
        return Arrays.equals(types, that.types) && Objects.equals(clazz, that.clazz) && Objects.equals(qualifier, that.qualifier) && Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(clazz, qualifier, object);
        result = 31 * result + Arrays.hashCode(types);
        return result;
    }

    @Override
    public String toString() {
        return "ComponentDescription{" +
                "types=" + Arrays.toString(types) +
                ", aClass=" + clazz +
                ", qualifier='" + qualifier + '\'' +
                ", object=" + object +
                '}';
    }
}
