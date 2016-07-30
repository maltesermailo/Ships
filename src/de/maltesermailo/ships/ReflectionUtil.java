package de.maltesermailo.ships;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.Bukkit;

public final class ReflectionUtil {
	
	public static Object[] emtyObjectArray() {
		return new Object[0];
	}
	
	public static Class<?>[] emtyClassArray() {
		return new Class<?>[0];
	}
	
	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	
	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ReflectException("Could not get the class '" + className + "'!");
		} 
	}
	
	public static Class<?> getNMSClass(String className) {
		try {
			return Class.forName("net.minecraft.server." + ReflectionUtil.VERSION + "." + className);
		} catch (ClassNotFoundException e) {
			throw new ReflectException("Could not get the class '" + className + "'!");
		}
	}
	
	public static Class<?> getCraftBukkitClass(String className) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.VERSION + "." + className);
		} catch (ClassNotFoundException e) {
			throw new ReflectException("Could not get the class '" + className + "'!");
		}
	}
	
	public static Field getDeclaredField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw new ReflectException("Could not find the field '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		} 
	}
	
	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw new ReflectException("Could not find the field '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		} 
	}
	
	public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>[] array) {
		try {
			return clazz.getDeclaredMethod(name, array);
		} catch (NoSuchMethodException e) {
			throw new ReflectException("Could not find the method '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		}
	}
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>[] array) {
		try {
			return clazz.getMethod(name, array);
		} catch (NoSuchMethodException e) {
			throw new ReflectException("Could not find the method '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		}
	}
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] array) {
		try {
			return clazz.getConstructor(array);
		} catch (NoSuchMethodException e) {
			throw new ReflectException("Could not find the constructor '" + Arrays.asList(array).toString() + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		}
	}
	
	public static Object createStaticObject(Field field) {
		return ReflectionUtil.createObject(field, null);
	}
	
	public static Object createStaticObject(Method method, Object... parameters) {
		return ReflectionUtil.createObject(method, null, parameters);
	}
	
	public static Object createStaticObject(Method method) {
		return ReflectionUtil.createObject(method, null, ReflectionUtil.emtyObjectArray());
	}
	
	public static Object createObject(Field field, Object from) {
		try {
			return field.get(from);
		} catch (IllegalArgumentException e) {
			throw new ReflectException("Could not get the field '" + field.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the field '" + field.getName() + "'!");
		}
	}
	
	public static Object createObject(Method method, Object from) {
		return ReflectionUtil.createObject(method, from, ReflectionUtil.emtyObjectArray());
	}
	
	public static Object createObject(Method method, Object from, Object... parameters) {
		try {
			return method.invoke(from, parameters);
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		}
	}
	
	public static Object createObject(Constructor<?> from) {
		return ReflectionUtil.createObject(from, ReflectionUtil.emtyObjectArray());
	}
	
	public static Object createObject(Constructor<?> from, Object... parameters) {
		try {
			return from.newInstance(parameters);
		} catch (InstantiationException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (IllegalArgumentException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		}
	}
	
	public static Object createObject(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new ReflectException("An error occured while creating an instance of the class '" + clazz.getName() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("An error occured while creating an instance of the class '" + clazz.getName() + "'!");
		}
	}
	
	public static void invokeStaticMethod(Method method, Object... parameters) {
		ReflectionUtil.invokeMethod(method, null, parameters);
	}
	
	public static void invokeStaticMethod(Method method) {
		ReflectionUtil.invokeMethod(method, null, ReflectionUtil.emtyObjectArray());
	}
	
	public static void invokeMethod(Method method, Object from) {
		ReflectionUtil.invokeMethod(method, from, ReflectionUtil.emtyObjectArray());
	}
	
	public static void invokeMethod(Method method, Object from, Object... parameters) {
		try {
			method.invoke(from, parameters);
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		}
	}
	
	public static void setStaticValue(Field field, Object value) {
		ReflectionUtil.setValue(field, null, value);
	}
	
	public static void setValue(Field field, Object from, Object value) {
		try {
			field.set(from, value);
		} catch (IllegalArgumentException e) {
			throw new ReflectException("Could not set the field '" + field.getName() + "' of '" + from.getClass().getName() + "' to '" + value.toString() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the field '" + field.getName() + "'!");
		}
	}
	
	@SuppressWarnings("serial")
	public static class ReflectException extends RuntimeException {
		
		public ReflectException(String s) {
			super(s);
		}
		
	}
	
}