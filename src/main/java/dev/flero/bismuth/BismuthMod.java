package dev.flero.bismuth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BismuthMod implements ClientModInitializer {
	public static @Nullable String version = null;
	public static List<String> contributors = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		ModContainer container = FabricLoader.getInstance().getModContainer("bismuth").get();

		ModMetadata metadata = container.getMetadata();
		version = metadata.getVersion().getFriendlyString();
		contributors = metadata.getContributors().stream().map(Person::getName).collect(Collectors.toList());
	}
}
