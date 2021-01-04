# Somnus API [![](http://cf.way2muchnoise.eu/versions/somnus.svg)](https://www.curseforge.com/minecraft/mc-mods/somnus) [![](http://cf.way2muchnoise.eu/short_somnus_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/somnus/files) [![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg?&style=flat-square)](https://www.gnu.org/licenses/lgpl-3.0) [![](https://img.shields.io/discord/500852157503766538.svg?color=green&label=Discord&style=flat-square)](https://discord.gg/JWgrdwt)

## Overview

Somnus is a lightweight library that adds a few events for sleeping in Minecraft to allow mod
developers easy and compatible ways of implementing their sleep mechanics.

### Features
- Determining the time of day for the world when players wake up
- Preventing or hooking into sleep attempts
- Determining valid times of day for sleeping

## Adding to Your Project:

Add the following to your build.gradle file:

```
repositories {
    maven {
        url = "https://maven.theillusivec4.top"
    }
}

dependencies {
    modImplementation "top.theillusivec4.somnus:somnus-fabric:${version}"
}
```

Replace ${version} with the version of Somnus that you want to use.
