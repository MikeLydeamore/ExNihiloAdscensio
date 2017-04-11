package exnihiloadscensio.texturing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@AllArgsConstructor
public class SpriteColor {
    @Getter
	private TextureAtlasSprite sprite;
    @Getter
	private Color color;
}
