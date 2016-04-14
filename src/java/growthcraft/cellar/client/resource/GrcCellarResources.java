package growthcraft.cellar.client.resource;

import growthcraft.cellar.client.model.ModelCultureJar;
import growthcraft.cellar.client.model.ModelFruitPresser;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GrcCellarResources
{
	public static GrcCellarResources INSTANCE;

	static final String DOMAIN = "grccellar";

	/// Guis
	public final ResourceLocation textureGuiBrewKettle = new ResourceLocation(DOMAIN, "textures/guis/brewkettle_gui.png");
	public final ResourceLocation textureGuiFermentBarrel = new ResourceLocation(DOMAIN, "textures/guis/fermentbarrel_gui.png");
	public final ResourceLocation textureGuiCultureJar = new ResourceLocation(DOMAIN, "textures/guis/gui_ferment_jar.png");
	public final ResourceLocation textureGuiFruitPress = new ResourceLocation(DOMAIN, "textures/guis/fruitpress_gui.png");

	public GrcCellarResources()
	{
		INSTANCE = this;
	}
}
