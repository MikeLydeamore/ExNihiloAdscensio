package exnihiloadscensio.items.ore;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Helper class for those who are using a {@link RegistryNamespaced} to store objects that are used for
 * sub items in an ItemStack.
 *
 * @param <T> - The {@link IForgeRegistryEntry} type that the registry contains.
 */
public class ItemFMLRegistryWrapper<T extends IForgeRegistryEntry<T>> {

	 public static final int INVALID_ID = Short.MAX_VALUE - 1;
    private final RegistryNamespaced<ResourceLocation, T> registry;
    private final Item item;
    private String defaultPrefix = "minecraft";

    /**
     * @param registry - The registry to get data from.
     * @param item     - The Item to create a stack for
     */
    public ItemFMLRegistryWrapper(RegistryNamespaced<ResourceLocation, T> registry, Item item) {
        this.registry = registry;
        this.item = item;
    }

    /**
     * @see #getStack(ResourceLocation, int)
     */
    public ItemStack getStack(String name) {
        return getStack(name, 1);
    }

    /**
     * @see #getStack(ResourceLocation, int)
     */
    public ItemStack getStack(String name, int amount) {
        return getStack(name.contains(":") ? new ResourceLocation(name) : new ResourceLocation(getDefaultPrefix(), name), amount);
    }

    /**
     * @see #getStack(ResourceLocation, int)
     */
    public ItemStack getStack(ResourceLocation objectKey) {
        return getStack(objectKey, 1);
    }

    /**
     * Creates an ItemStack based on the provided {@link T}'s int ID in the registry.
     *
     * @param objectKey - The registry key used for the type.
     * @param amount    - The size of the ItemStack
     *
     * @return the ItemStack for the Entry
     */
    public ItemStack getStack(ResourceLocation objectKey, int amount) {
        int meta = getRegistry().getIDForObject(getRegistry().getObject(objectKey));
        return new ItemStack(getItem(), amount, meta == -1 ? INVALID_ID : meta);
    }

    /**
     * Creates an ItemStack based on the provided {@link T}'s int ID in the registry.
     *
     * @param customItem - A custom item.
     * @param objectKey  - The registry key used for the type.
     * @param amount     - The size of the ItemStack
     *
     * @return the ItemStack for the Entry
     */
    public ItemStack getStack(Item customItem, ResourceLocation objectKey, int amount) {
        int meta = getRegistry().getIDForObject(getRegistry().getObject(objectKey));
        return new ItemStack(customItem, amount, meta == -1 ? INVALID_ID : meta);
    }

    @Nullable
    public T getType(ItemStack stack) {
        return getRegistry().getObjectById(stack.getItemDamage());
    }

    /**
     * @return the default prefix for ResourceLocations
     */
    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    /**
     * Sets a default {@link ResourceLocation} prefix to use when using Strings to lookup entries.
     *
     * Allows lookups via {@code key_name} instead of {@code prefix:key_name}
     *
     * @param defaultPrefix - The default prefix to use for ResourceLocations.
     * @return self for chaining.
     */
    public ItemFMLRegistryWrapper<T> setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
        return this;
    }

    /**
     * @return the used registry.
     */
    public RegistryNamespaced<ResourceLocation, T> getRegistry() {
        return registry;
    }

    /**
     * @return the provided item.
     */
    public Item getItem() {
        return item;
    }
}