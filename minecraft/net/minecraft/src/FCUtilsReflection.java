package net.minecraft.src;

public class FCUtilsReflection {
	private static boolean isObfuscated = false;

	public static boolean isObfuscated() {
		return isObfuscated;
	}

	public static void setObfuscated(boolean isObfuscated) {
		FCUtilsReflection.isObfuscated = isObfuscated;
	}
}
