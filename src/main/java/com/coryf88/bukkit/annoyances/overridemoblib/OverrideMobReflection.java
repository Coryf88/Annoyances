package com.coryf88.bukkit.annoyances.overridemoblib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class OverrideMobReflection {
	// Method
	public static <T extends Object> T invokeDeclaredMethod(Class<T> returnType, Object obj, String methodName, Object... values) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.invokeDeclaredMethod(obj, obj.getClass(), methodName, values));
	}

	public static <T extends Object> T invokeSuperDeclaredMethod(Class<T> returnType, Object obj, String methodName, Object... values) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.invokeDeclaredMethod(obj, obj.getClass().getSuperclass(), methodName, values));
	}

	public static <T extends Object> T invokeDeclaredMethod(Class<T> returnType, Object obj, Class<?> clazz, String methodName, Object... values) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.invokeDeclaredMethod(obj, clazz, methodName, values));
	}

	public static Object invokeDeclaredMethod(Object obj, Class<?> clazz, String methodName, Object... values) throws OverrideMobException {
		try {
			Class<?>[] parameterTypes = new Class<?>[values.length];
			for (int i = 0; i < values.length; i++) {
				parameterTypes[i] = values[i].getClass();
			}

			Method method = (clazz == null ? obj.getClass() : clazz).getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(obj, values);
		} catch (Exception e) {
			throw new OverrideMobException("Failed to call method '" + methodName + "' from " + obj.getClass(), e);
		}
	}

	// Set Field
	public static void setDeclaredField(Object obj, String fieldName, Object value) throws OverrideMobException {
		OverrideMobReflection.setDeclaredField(obj, obj.getClass(), fieldName, value);
	}

	public static void setSuperDeclaredField(Object obj, String fieldName, Object value) throws OverrideMobException {
		OverrideMobReflection.setDeclaredField(obj, obj.getClass().getSuperclass(), fieldName, value);
	}

	public static void setDeclaredField(Object obj, Class<?> clazz, String fieldName, Object value) throws OverrideMobException {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			throw new OverrideMobException("Failed to get field '" + fieldName + "' from " + obj.getClass(), e);
		}
	}

	// Get Field
	public static <T extends Object> T getDeclaredField(Class<T> returnType, Object obj, String fieldName) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.getDeclaredField(obj, obj.getClass(), fieldName));
	}

	public static <T extends Object> T getSuperDeclaredField(Class<T> returnType, Object obj, String fieldName) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.getDeclaredField(obj, obj.getClass().getSuperclass(), fieldName));
	}

	public static <T extends Object> T getDeclaredField(Class<T> returnType, Object obj, Class<?> clazz, String fieldName) throws OverrideMobException {
		return returnType.cast(OverrideMobReflection.getDeclaredField(obj, clazz, fieldName));
	}

	public static Object getDeclaredField(Object obj, Class<?> clazz, String fieldName) throws OverrideMobException {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new OverrideMobException("Failed to get field '" + fieldName + "' from " + obj.getClass(), e);
		}
	}
}
