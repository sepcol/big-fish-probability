package com.bigfishprobability;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BigFishProbabilityPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BigFishProbabilityPlugin.class);
		RuneLite.main(args);
	}
}