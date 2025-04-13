![icon](https://raw.githubusercontent.com/RaydanOMGr/Podium/refs/heads/master/src/main/resources/assets/podium/icon.png)

# Podium  
> **Po**jav + So**dium** = **Podium**

A tiny Mixin mod that disables [Sodium](https://github.com/CaffeineMC/sodium-fabric)'s [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) check.


## Disclaimer
This doesn’t magically make [Sodium](https://github.com/CaffeineMC/sodium-fabric) work on all devices or renderers. You’re still on your own when it comes to compatibility issues.


## Requirements
- **Minecraft:** 1.20+
- **Loader:** [Fabric](https://fabricmc.net/use/) (0.15 or newer)
- **Java:** 21 or newer
- **[Sodium](https://github.com/CaffeineMC/sodium-fabric):** 0.5.13 or newer
- **Launcher:** [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) with MobileGlues or LTW


## Installation
1. Download the latest release of **Podium** from [Modrinth](https://modrinth.com/mod/podium) or [Curseforge](https://curseforge.com/minecraft/mc-mods/podium-sodium).
2. Make sure you have the [Fabric Loader](https://fabricmc.net/use/) installed on your [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) instance.
3. Put the `podium-x.y.z.jar` into your `mods` folder alongside [Sodium](https://github.com/CaffeineMC/sodium-fabric).
4. Use a supported renderer like **LTW** or **MobileGlues**.
5. Launch the game. If everything works, Sodium will no longer crash due to the Pojav check.


## What's the deal with Sodium and PojavLauncher?
[Sodium](https://github.com/CaffeineMC/sodium-fabric) replaces Minecraft’s rendering engine with something far more efficient, but that comes at a cost. [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) is known to struggle with mods that mess with rendering in non-standard ways which is exactly what Sodium does.

These incompatibilities often led to crashes or visual glitches when running Sodium on Pojav. As a result, people (mostly kids) flooded the Sodium support channels asking for help with issues that weren’t actually Sodium’s fault. This got annoying quickly, so the Sodium team added a launcher check.

Originally, it was just a warning. As of Sodium 0.6.13, it crashes the game.


## So why does *this* mod exist?
Good question.

Yes, Sodium and Pojav have been historically incompatible, but that’s changing. Pojav relies on something called *renderers*, which act as a translation/compatibility layer between the game’s graphics code and mobile hardware. Old renderers were notoriously bad: either super slow or completely broken with anything beyond vanilla Minecraft.

But now we have **MobileGlues** and **LTW**, two newer renderers that massively improve both compatibility and performance:
- **MobileGlues**: Developed by [Swung0x48](https://github.com/Swung0x48), offers solid compatibility and decent performance.
- **LTW**: Developed by [Artdell](https://github.com/artdeell), amazing performance and great compatibility with Sodium.

Thanks to these, running [Sodium](https://github.com/CaffeineMC/sodium-fabric) on [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) is no longer guaranteed to cause crashes and visual glitches. Podium simply removes the crash mechanism.


## License
MIT. Do whatever you want.
