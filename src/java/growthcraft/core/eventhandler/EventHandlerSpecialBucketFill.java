/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.core.eventhandler;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Similar to the regular EventHandlerBucketFill, but is specialized
 */
public class EventHandlerSpecialBucketFill
{
	public static interface IBucketEntry
	{
		ItemStack getItemStack();
		boolean matches(@Nonnull World world, @Nonnull MovingObjectPosition pos);
		void commit(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull MovingObjectPosition pos);
	}

	private static EventHandlerSpecialBucketFill INSTANCE = new EventHandlerSpecialBucketFill();
	private List<IBucketEntry> buckets = new ArrayList<IBucketEntry>();

	public static EventHandlerSpecialBucketFill instance()
	{
		return INSTANCE;
	}

	public void addEntry(@Nonnull IBucketEntry entry)
	{
		buckets.add(entry);
	}

	private ItemStack fillCustomBucket(FillBucketEvent event)
	{
		for (IBucketEntry entry : buckets)
		{
			if (entry.matches(event.world, event.target))
			{
				entry.commit(event.entityPlayer, event.world, event.target);
				return entry.getItemStack();
			}
		}
		return null;
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		final ItemStack result = fillCustomBucket(event);

		if (result == null) return;

		event.result = result.copy();
		event.setResult(Result.ALLOW);
	}
}
