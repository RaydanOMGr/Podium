package me.andreasmelone.podium;

import me.andreasmelone.podium.transformer.PostLaunchChecksTransformer;
import me.andreasmelone.podium.transformer.PreLaunchChecksTransformer;
import net.caffeinemc.mods.sodium.client.compatibility.environment.OsUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

// Using a mixin plugin as a main/init class is usually an awful idea, but
// this is the earliest I can call any code, which is required to ensure that
// sodium classes are not loaded yet so I can define them myself
public class PodiumMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // nobody would ever want to mixin into this class, right?
        // well, not like they can do that anyway...
        ClassLoader loader = OsUtils.class.getClassLoader();

        System.out.println("[Podium] Trying to load and transform " + PreLaunchChecksTransformer.CLASSNAME_DOTTED);
        Util.tryFindClass(PreLaunchChecksTransformer.CLASSNAME_SLASHED).ifPresentOrElse(
                bytes -> {
                    try {
                        Util.defineClass(
                                loader,
                                PreLaunchChecksTransformer.CLASSNAME_DOTTED,
                                new PreLaunchChecksTransformer().transform(bytes)
                        );
                    } catch (Exception e) {
                        System.out.println("[Podium] Was unable to define class " + PreLaunchChecksTransformer.CLASSNAME_DOTTED);
                        e.printStackTrace();
                    }
                },
                () -> System.out.println("[Podium] Class " + PreLaunchChecksTransformer.CLASSNAME_DOTTED + " not found!")
        );

        System.out.println("[Podium] Trying to load and transform " + PostLaunchChecksTransformer.CLASSNAME_DOTTED);
        Util.tryFindClass(PostLaunchChecksTransformer.CLASSNAME_SLASHED).ifPresentOrElse(
                bytes -> {
                    try {
                        Util.defineClass(
                                loader,
                                PostLaunchChecksTransformer.CLASSNAME_DOTTED,
                                new PostLaunchChecksTransformer().transform(bytes)
                        );
                    } catch (Exception e) {
                        System.out.println("[Podium] Was unable to define class " + PostLaunchChecksTransformer.CLASSNAME_DOTTED);
                        e.printStackTrace();
                    }
                },
                () -> System.out.println("[Podium] Class " + PostLaunchChecksTransformer.CLASSNAME_DOTTED + " not found!")
        );
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
