/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.hops.integration;

import growthcraft.hops.GrowthCraftHops;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.event.FMLInterModComms;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftHops.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", GrowthCraftHops.hopVine.asStack(1, 3));

		ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftHops.hops.asStack(), new AspectList().add(Aspect.PLANT, 1));

		for (int i = 0; i < GrowthCraftHops.hopAleBooze.length; ++i)
		{
			if (i == 0 || i == 4)
			{
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAle.asStack(1, i), new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAleBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAleBuckets[i].asStack(), new AspectList().add(Aspect.WATER, 2));
			}
			else
			{
				final int m = i == 2 ? 4 : 2;
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAle.asStack(1, i), new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAleBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopAleBuckets[i].asStack(), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
			}
		}
	}
}
