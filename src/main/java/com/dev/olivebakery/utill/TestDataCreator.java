package com.dev.olivebakery.utill;

import javax.persistence.GeneratedValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by YoungMan on 2019-04-18.
 */

public class TestDataCreator {

	public static <T> T dummy(Class<T> clz) {
		return TestDataCreator.dummy(clz, null);
	}

	public static <T> T dummy(Class<T> clz, Map<String, Object> defaultValue) {
		Object returnObj;

		try {
			returnObj = TestDataCreator.newInstance(clz);

			TestDataCreator.setFieldValues(returnObj, defaultValue);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return (T) returnObj;
	}

	private static Object newInstance(Class clz) throws Exception {
		Object returnObj;
		try {
			// Lombok @Builder support
			Method method = clz.getDeclaredMethod("builder");
			Object builderObj = method.invoke(null);
			returnObj = builderObj.getClass().getDeclaredMethod("build").invoke(builderObj);
		} catch (Exception e) {
			returnObj = clz.newInstance();
		}
		return returnObj;
	}

	private static void setFieldValues(Object object, Map<String, Object> defaultValue) throws IllegalAccessException {

		List<Field> fields = new ArrayList(Arrays.asList(object.getClass().getDeclaredFields()));

		Class parent = object.getClass().getSuperclass();
		if (parent != null) {
			fields.addAll(Arrays.asList(parent.getDeclaredFields()));
		}

		for (Field field : fields) {
			field.setAccessible(true);
			Class fieldClz = field.getType();

			Annotation[] annotations = field.getDeclaredAnnotations();
			boolean hasGeneratedValue = false;
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(GeneratedValue.class)) {
					hasGeneratedValue = true;
					break;
				}
			}

			if (hasGeneratedValue) {
				continue;
			}

			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}

			Object value = TestDataCreator.generateRandomValue(fieldClz);

			if (value != null) {
				field.set(object, value);
			}

			if (defaultValue != null && defaultValue.size() > 0 && defaultValue.containsKey(field.getName())) {
				field.set(object, defaultValue.get(field.getName()));
			}
		}
	}

	private static Object generateRandomValue(Class fieldClz) {
		Object value = null;
		if (fieldClz == Integer.class || fieldClz == int.class) {
			value = randInt();
		} else if (fieldClz == Float.class || fieldClz == float.class) {
			value = randFloat();
		} else if (fieldClz == Long.class || fieldClz == long.class) {
			value = randlong();
		} else if (fieldClz == Double.class || fieldClz == double.class) {
			value = randDouble();
		} else if (fieldClz == String.class) {
			value = randString();
		} else if (fieldClz == Boolean.class || fieldClz == boolean.class) {
			value = randBoolean();
		} else if (fieldClz == LocalDate.class) {
			value = LocalDate.now();
		} else if (fieldClz == LocalTime.class) {
			value = LocalTime.now();
		} else if (fieldClz == LocalDateTime.class) {
			value = LocalDateTime.now();
		} else {
			value = randEnum(fieldClz, value);
		}

		return value;
	}

	private static Object randEnum(Class fieldClz, Object value) {
		Object[] consts = fieldClz.getEnumConstants();
		if (consts != null && consts.length > 0) {
			value = consts[randInt(consts.length)];
		}
		return value;
	}

	private static int randInt() {
		return new Random().nextInt(Integer.MAX_VALUE);
	}

	private static int randInt(int max) {
		return new Random().nextInt(max);
	}

	private static int randInt(int min, int max) {
		return new Random().nextInt(max - min) + min;
	}

	private static long randlong() {
		return Math.abs(new Random().nextLong());
	}

	private static double randDouble() {
		return Math.abs(new Random().nextDouble());
	}

	private static boolean randBoolean() {
		return new Random().nextBoolean();
	}

	private static String randString() {
		return randString(randInt(5, 20));
	}

	private static String randString(int length) {
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			sb.append(AB.charAt(new Random().nextInt(AB.length())));
		}

		return sb.toString();
	}

	private static Float randFloat() {
		return Math.abs(new Random().nextFloat());
	}

}
